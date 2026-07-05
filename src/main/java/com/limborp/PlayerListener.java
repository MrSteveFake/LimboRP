package com.limborp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.security.MessageDigest;

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
            try {
                if (resourcePackHash != null && !resourcePackHash.isEmpty()) {
                    // Конвертируем hex строку в byte[20]
                    String cleanHash = resourcePackHash.trim().toLowerCase().replaceAll("[^0-9a-f]", "");
                    
                    if (cleanHash.length() >= 40) {
                        byte[] hashBytes = new byte[20];
                        for (int i = 0; i < 20; i++) {
                            String hexByte = cleanHash.substring(i * 2, i * 2 + 2);
                            hashBytes[i] = (byte) Integer.parseInt(hexByte, 16);
                        }
                        player.setResourcePack(resourcePackUrl, hashBytes);
                    } else {
                        plugin.getLogger().warning("Invalid hash format! Using empty hash.");
                        player.setResourcePack(resourcePackUrl, new byte[20]);
                    }
                } else {
                    // Если хеш не указан, НЕ отправляем ресурспак
                    // Или отправляем с нулевым хешем
                    plugin.getLogger().warning("Resource pack hash is empty for player " + player.getName());
                    player.setResourcePack(resourcePackUrl, new byte[20]);
                }
                
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info("Sending resource pack to " + player.getName());
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Error sending resource pack to " + player.getName() + ": " + e.getMessage());
                // Отправляем ресурспак без хеша в случае ошибки
                try {
                    player.setResourcePack(resourcePackUrl, new byte[20]);
                } catch (Exception ex) {
                    plugin.getLogger().severe("Critical error: " + ex.getMessage());
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        
        if (status == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            plugin.getPackManager().setPlayerStatus(player, 
                ResourcePackManager.ResourcePackStatus.ACCEPTED);
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info(playerName + " accepted resource pack");
            }
        } else if (status == PlayerResourcePackStatusEvent.Status.DECLINED) {
            plugin.getPackManager().setPlayerStatus(player, 
                ResourcePackManager.ResourcePackStatus.DECLINED);
            player.sendMessage(ChatColor.RED + "[LimboRP] " + ChatColor.WHITE + 
                "Вы отклонили ресурспак. Текст будет отображаться в стандартном виде.");
            plugin.getLogger().warning(playerName + " declined resource pack!");
        } else if (status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            plugin.getPackManager().setPlayerStatus(player, 
                ResourcePackManager.ResourcePackStatus.FAILED_DOWNLOAD);
            player.sendMessage(ChatColor.RED + "[LimboRP] " + ChatColor.WHITE + 
                "Не удалось загрузить ресурспак. Текст будет отображаться в стандартном виде.");
            plugin.getLogger().warning("Failed to download resource pack for " + playerName);
        } else if (status == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            plugin.getPackManager().setPlayerStatus(player, 
                ResourcePackManager.ResourcePackStatus.SUCCESSFULLY_LOADED);
            player.sendMessage(ChatColor.GREEN + "[LimboRP] " + ChatColor.WHITE + 
                "Ресурспак успешно загружен!");
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info(playerName + " successfully loaded resource pack");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        
        if (player.hasPermission("limborp.bypass")) {
            return;
        }
        
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
        
        if (player.hasPermission("limborp.bypass")) {
            return;
        }
        
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
