package com.limborp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {
    
    private final LimboRP plugin;
    
    public PlayerListener(LimboRP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Отправляем ресурспак при входе (если настроен)
        String resourcePackUrl = plugin.getConfig().getString("resource-pack-url");
        String resourcePackHash = plugin.getConfig().getString("resource-pack-hash");
        
        if (resourcePackUrl != null && !resourcePackUrl.isEmpty()) {
            player.setResourcePack(resourcePackUrl, 
                                  resourcePackHash != null ? resourcePackHash : "");
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("Sending resource pack to " + player.getName());
            }
        }
    }
    
    @EventHandler
    public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        
        switch (event.getStatus()) {
            case ACCEPTED:
                plugin.getPackManager().setPlayerStatus(player, 
                    ResourcePackManager.ResourcePackStatus.ACCEPTED);
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info(playerName + " accepted resource pack, waiting for download...");
                }
                break;
                
            case DECLINED:
                plugin.getPackManager().setPlayerStatus(player, 
                    ResourcePackManager.ResourcePackStatus.DECLINED);
                player.sendMessage(ChatColor.RED + "[LimboRP] " + ChatColor.WHITE + 
                    "Вы отклонили ресурспак. Текст будет отображаться в стандартном виде.");
                plugin.getLogger().warning(playerName + " declined resource pack!");
                break;
                
            case FAILED_DOWNLOAD:
                plugin.getPackManager().setPlayerStatus(player, 
                    ResourcePackManager.ResourcePackStatus.FAILED_DOWNLOAD);
                player.sendMessage(ChatColor.RED + "[LimboRP] " + ChatColor.WHITE + 
                    "Не удалось загрузить ресурспак. Текст будет отображаться в стандартном виде.");
                plugin.getLogger().warning("Failed to download resource pack for " + playerName);
                break;
                
            case SUCCESSFULLY_LOADED:
                plugin.getPackManager().setPlayerStatus(player, 
                    ResourcePackManager.ResourcePackStatus.SUCCESSFULLY_LOADED);
                player.sendMessage(ChatColor.GREEN + "[LimboRP] " + ChatColor.WHITE + 
                    "Ресурспак успешно загружен!");
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info(playerName + " successfully loaded resource pack");
                }
                break;
                
            case DOWNLOADING:
                plugin.getPackManager().setPlayerStatus(player, 
                    ResourcePackManager.ResourcePackStatus.DOWNLOADING);
                break;
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        
        // Проверяем, есть ли у игрока право на обход
        if (player.hasPermission("limborp.bypass")) {
            return;
        }
        
        // Если ресурспак не загружен, заменяем символы
        if (!plugin.getPackManager().hasResourcePack(player)) {
            String originalMessage = event.getMessage();
            String replacedMessage = plugin.getSymbolReplacer().replaceSymbols(originalMessage);
            
            if (!originalMessage.equals(replacedMessage)) {
                event.setMessage(replacedMessage);
                
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info("Replaced message for " + player.getName() + 
                        ": " + originalMessage + " -> " + replacedMessage);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        
        // Проверяем, есть ли у игрока право на обход
        if (player.hasPermission("limborp.bypass")) {
            return;
        }
        
        // Если ресурспак не загружен, заменяем символы в командах
        if (!plugin.getPackManager().hasResourcePack(player)) {
            String message = event.getMessage();
            String replacedMessage = plugin.getSymbolReplacer().replaceSymbols(message);
            
            if (!message.equals(replacedMessage)) {
                event.setMessage(replacedMessage);
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getPackManager().removePlayer(event.getPlayer());
    }
}
