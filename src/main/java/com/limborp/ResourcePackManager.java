package com.limborp;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResourcePackManager {
    
    private final LimboRP plugin;
    private final Map<UUID, ResourcePackStatus> playerPackStatus;
    
    public ResourcePackManager(LimboRP plugin) {
        this.plugin = plugin;
        this.playerPackStatus = new HashMap<>();
    }
    
    public void setPlayerStatus(Player player, ResourcePackStatus status) {
        playerPackStatus.put(player.getUniqueId(), status);
        
        // Логируем статус для отладки
        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Player " + player.getName() + 
                                   " resource pack status: " + status.name());
        }
    }
    
    public boolean hasResourcePack(Player player) {
        ResourcePackStatus status = playerPackStatus.get(player.getUniqueId());
        return status == ResourcePackStatus.SUCCESSFULLY_LOADED;
    }
    
    public boolean isPending(Player player) {
        ResourcePackStatus status = playerPackStatus.get(player.getUniqueId());
        return status == ResourcePackStatus.ACCEPTED;
    }
    
    public ResourcePackStatus getStatus(Player player) {
        return playerPackStatus.getOrDefault(player.getUniqueId(), 
                                            ResourcePackStatus.NOT_SET);
    }
    
    public void removePlayer(Player player) {
        playerPackStatus.remove(player.getUniqueId());
    }
    
    public int getPlayerCount() {
        return playerPackStatus.size();
    }
    
    public enum ResourcePackStatus {
        NOT_SET,
        ACCEPTED,
        DECLINED,
        FAILED_DOWNLOAD,
        SUCCESSFULLY_LOADED
    }
}
