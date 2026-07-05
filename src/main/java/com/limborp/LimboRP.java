package com.limborp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class LimboRP extends JavaPlugin {
    
    private static LimboRP instance;
    private ResourcePackManager packManager;
    private SymbolReplacer symbolReplacer;
    private BukkitRunnable tabUpdater;
    private BukkitRunnable scoreboardUpdater;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Сохраняем конфигурацию по умолчанию
        saveDefaultConfig();
        
        // Инициализируем менеджеры
        packManager = new ResourcePackManager(this);
        symbolReplacer = new SymbolReplacer(this);
        
        // Регистрируем слушатели
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        
        // Регистрируем команды
        getCommand("limborp").setExecutor(new LimboRPCommand(this));
        
        // Запускаем обновление таба и scoreboard
        startUpdaters();
        
        getLogger().info("LimboRP v" + getDescription().getVersion() + " has been enabled!");
    }
    
    @Override
    public void onDisable() {
        // Останавливаем задачи
        if (tabUpdater != null) {
            tabUpdater.cancel();
        }
        if (scoreboardUpdater != null) {
            scoreboardUpdater.cancel();
        }
        
        getLogger().info("LimboRP v" + getDescription().getVersion() + " has been disabled!");
        instance = null;
    }
    
    private void startUpdaters() {
        int interval = getConfig().getInt("update-interval", 20);
        
        // Обновление таба
        tabUpdater = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("limborp.bypass")) {
                        continue;
                    }
                    
                    if (!packManager.hasResourcePack(player)) {
                        // Обновляем имя в табе
                        String playerListName = player.getPlayerListName();
                        if (playerListName != null && !playerListName.isEmpty()) {
                            String replaced = symbolReplacer.replaceSymbols(playerListName);
                            if (!playerListName.equals(replaced)) {
                                player.setPlayerListName(replaced);
                            }
                        }
                        
                        // Обновляем заголовок таба
                        String header = player.getPlayerListHeader();
                        if (header != null && !header.isEmpty()) {
                            String replaced = symbolReplacer.replaceSymbols(header);
                            if (!header.equals(replaced)) {
                                player.setPlayerListHeader(replaced);
                            }
                        }
                        
                        // Обновляем подвал таба
                        String footer = player.getPlayerListFooter();
                        if (footer != null && !footer.isEmpty()) {
                            String replaced = symbolReplacer.replaceSymbols(footer);
                            if (!footer.equals(replaced)) {
                                player.setPlayerListFooter(replaced);
                            }
                        }
                    }
                }
            }
        };
        tabUpdater.runTaskTimer(this, interval, interval);
        
        // Обновление scoreboard
        scoreboardUpdater = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("limborp.bypass")) {
                        continue;
                    }
                    
                    if (!packManager.hasResourcePack(player)) {
                        Scoreboard scoreboard = player.getScoreboard();
                        
                        // Обновляем все objectives
                        for (Objective objective : scoreboard.getObjectives()) {
                            // Название objective
                            String displayName = objective.getDisplayName();
                            if (displayName != null && !displayName.isEmpty()) {
                                String replaced = symbolReplacer.replaceSymbols(displayName);
                                if (!displayName.equals(replaced)) {
                                    objective.setDisplayName(replaced);
                                }
                            }
                            
                            // Строки scoreboard
                            for (String entry : scoreboard.getEntries()) {
                                Score score = objective.getScore(entry);
                                String replacedEntry = symbolReplacer.replaceSymbols(entry);
                                
                                if (!entry.equals(replacedEntry)) {
                                    int scoreValue = score.getScore();
                                    scoreboard.resetScores(entry);
                                    Score newScore = objective.getScore(replacedEntry);
                                    newScore.setScore(scoreValue);
                                }
                            }
                        }
                        
                        // Обновляем команды (teams)
                        for (Team team : scoreboard.getTeams()) {
                            String prefix = team.getPrefix();
                            if (prefix != null && !prefix.isEmpty()) {
                                String replaced = symbolReplacer.replaceSymbols(prefix);
                                if (!prefix.equals(replaced)) {
                                    team.setPrefix(replaced);
                                }
                            }
                            
                            String suffix = team.getSuffix();
                            if (suffix != null && !suffix.isEmpty()) {
                                String replaced = symbolReplacer.replaceSymbols(suffix);
                                if (!suffix.equals(replaced)) {
                                    team.setSuffix(replaced);
                                }
                            }
                            
                            String displayName = team.getDisplayName();
                            if (displayName != null && !displayName.isEmpty()) {
                                String replaced = symbolReplacer.replaceSymbols(displayName);
                                if (!displayName.equals(replaced)) {
                                    team.setDisplayName(replaced);
                                }
                            }
                        }
                    }
                }
            }
        };
        scoreboardUpdater.runTaskTimer(this, interval, interval);
        
        getLogger().info("Tab and Scoreboard updaters started (interval: " + interval + " ticks)");
    }
    
    public static LimboRP getInstance() {
        return instance;
    }
    
    public ResourcePackManager getPackManager() {
        return packManager;
    }
    
    public SymbolReplacer getSymbolReplacer() {
        return symbolReplacer;
    }
}
