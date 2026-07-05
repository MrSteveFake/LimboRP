package com.limborp;

import org.bukkit.plugin.java.JavaPlugin;

public class LimboRP extends JavaPlugin {
    
    private static LimboRP instance;
    private ResourcePackManager packManager;
    private SymbolReplacer symbolReplacer;
    
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
        
        getLogger().info("LimboRP v" + getDescription().getVersion() + " has been enabled!");
        getLogger().info("Resource pack URL: " + getConfig().getString("resource-pack-url", "not set"));
    }
    
    @Override
    public void onDisable() {
        getLogger().info("LimboRP v" + getDescription().getVersion() + " has been disabled!");
        instance = null;
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
