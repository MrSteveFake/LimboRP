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
    private TabUpdater tabUpdater;
    private ScoreboardUpdater scoreboardUpdater;
    
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
        startTabUpdater();
        startScoreboardUpdater();
        
        getLogger().info("LimboRP v" + getDescription().getVersion() + " has been enabled!");
        getLogger().info("Tab and Scoreboard replacer activated!");
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
    
    private void startTabUpdater() {
        tabUpdater = new TabUpdater();
        tabUpdater.runTaskTimerAsynchronously(this, 20L, 20L); // Каждую секунду
    }
    
    private void startScoreboardUpdater() {
        scoreboardUpdater = new ScoreboardUpdater();
        scoreboardUpdater.runTaskTimer(this, 20L, 20L); // Каждую секунду
    }
    
    // Класс для обновления таба
    private class TabUpdater extends BukkitRunnable {
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
                            // Устанавливаем новое имя в табе (синхронно)
                            final String finalReplaced = replaced;
                            final Player finalPlayer = player;
                            Bukkit.getScheduler().runTask(getInstance(), () -> {
                                finalPlayer.setPlayerListName(finalReplaced);
                            });
                        }
                    }
                    
                    // Обновляем заголовок и подвал таба
                    String header = player.getPlayerListHeader();
                    String footer = player.getPlayerListFooter();
                    
                    if (header != null) {
                        String replacedHeader = symbolReplacer.replaceSymbols(header);
                        if (!header.equals(replacedHeader)) {
                            final String finalHeader = replacedHeader;
                            final Player finalPlayer = player;
                            Bukkit.getScheduler().runTask(getInstance(), () -> {
                                finalPlayer.setPlayerListHeader(finalHeader);
                            });
                        }
                    }
                    
                    if (footer != null) {
                        String replacedFooter = symbolReplacer.replaceSymbols(footer);
                        if (!footer.equals(replacedFooter)) {
                            final String finalFooter = replacedFooter;
                            final Player finalPlayer = player;
                            Bukkit.getScheduler().runTask(getInstance(), () -> {
                                finalPlayer.setPlayerListFooter(finalFooter);
                            });
                        }
                    }
                }
            }
        }
    }
    
    // Класс для обновления scoreboard
    private class ScoreboardUpdater extends BukkitRunnable {
        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("limborp.bypass")) {
                    continue;
                }
                
                if (!packManager.hasResourcePack(player)) {
                    Scoreboard scoreboard = player.getScoreboard();
                    Objective objective = scoreboard.getObjective("limborp_display");
                    
                    if (objective != null) {
                        // Обновляем название scoreboard
                        String displayName = objective.getDisplayName();
                        if (displayName != null && !displayName.isEmpty()) {
                            String replaced = symbolReplacer.replaceSymbols(displayName);
                            if (!displayName.equals(replaced)) {
                                objective.setDisplayName(replaced);
                            }
                        }
                        
                        // Обновляем строки scoreboard
                        for (String entry : scoreboard.getEntries()) {
                            Score score = objective.getScore(entry);
                            if (score != null && score.getScore() > 0) {
                                String replacedEntry = symbolReplacer.replaceSymbols(entry);
                                if (!entry.equals(replacedEntry)) {
                                    // Сбрасываем старый score и устанавливаем новый
                                    scoreboard.resetScores(entry);
                                    Score newScore = objective.getScore(replacedEntry);
                                    newScore.setScore(score.getScore());
                                }
                            }
                        }
                    }
                    
                    // Также проверяем другие objectives
                    for (Objective obj : scoreboard.getObjectives()) {
                        if (obj != objective) {
                            String objDisplayName = obj.getDisplayName();
                            if (objDisplayName != null) {
                                String replaced = symbolReplacer.replaceSymbols(objDisplayName);
                                if (!objDisplayName.equals(replaced)) {
                                    obj.setDisplayName(replaced);
                                }
                            }
                        }
                    }
                    
                    // Обновляем имена команд в scoreboard
                    for (Team team : scoreboard.getTeams()) {
                        // Префикс
                        String prefix = team.getPrefix();
                        if (prefix != null) {
                            String replacedPrefix = symbolReplacer.replaceSymbols(prefix);
                            if (!prefix.equals(replacedPrefix)) {
                                team.setPrefix(replacedPrefix);
                            }
                        }
                        
                        // Суффикс
                        String suffix = team.getSuffix();
                        if (suffix != null) {
                            String replacedSuffix = symbolReplacer.replaceSymbols(suffix);
                            if (!suffix.equals(replacedSuffix)) {
                                team.setSuffix(replacedSuffix);
                            }
                        }
                        
                        // Display name
                        String displayName = team.getDisplayName();
                        if (displayName != null) {
                            String replacedDisplay = symbolReplacer.replaceSymbols(displayName);
                            if (!displayName.equals(replacedDisplay)) {
                                team.setDisplayName(replacedDisplay);
                            }
                        }
                    }
                }
            }
        }
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
