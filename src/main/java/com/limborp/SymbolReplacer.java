package com.limborp;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class SymbolReplacer {
    
    private final LimboRP plugin;
    private final Map<Character, String> replacements;
    private final Map<String, String> wordReplacements;
    
    public SymbolReplacer(LimboRP plugin) {
        this.plugin = plugin;
        this.replacements = new HashMap<>();
        this.wordReplacements = new HashMap<>();
        loadReplacements();
    }
    
    public void loadReplacements() {
        replacements.clear();
        wordReplacements.clear();
        
        // Загружаем замены из config.yml
        if (plugin.getConfig().contains("replacements")) {
            for (String key : plugin.getConfig().getConfigurationSection("replacements").getKeys(false)) {
                String value = plugin.getConfig().getString("replacements." + key);
                
                if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
                    continue;
                }
                
                // Переводим цветовые коды (&a, &l, и т.д.)
                String coloredValue = ChatColor.translateAlternateColorCodes('&', value);
                
                if (key.length() == 1) {
                    // Замена одного символа
                    replacements.put(key.charAt(0), coloredValue);
                } else {
                    // Замена слова/фразы
                    wordReplacements.put(key, coloredValue);
                }
            }
        }
        
        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Loaded " + replacements.size() + " character replacements");
            plugin.getLogger().info("Loaded " + wordReplacements.size() + " word replacements");
            
            // Выводим все загруженные замены для отладки
            for (Map.Entry<Character, String> entry : replacements.entrySet()) {
                plugin.getLogger().info("  Char replacement: '" + entry.getKey() + 
                    "' (U+" + String.format("%04X", (int)entry.getKey()) + 
                    ") -> '" + entry.getValue() + "'");
            }
            
            for (Map.Entry<String, String> entry : wordReplacements.entrySet()) {
                plugin.getLogger().info("  Word replacement: '" + entry.getKey() + 
                    "' -> '" + entry.getValue() + "'");
            }
        }
    }
    
    public String replaceSymbols(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        String result = text;
        
        // Сначала заменяем слова/фразы
        for (Map.Entry<String, String> entry : wordReplacements.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        
        // Затем заменяем отдельные символы
        StringBuilder finalResult = new StringBuilder();
        for (char c : result.toCharArray()) {
            String replacement = replacements.get(c);
            if (replacement != null) {
                finalResult.append(replacement);
            } else {
                finalResult.append(c);
            }
        }
        
        return finalResult.toString();
    }
    
    public void reloadReplacements() {
        loadReplacements();
    }
    
    public int getReplacementCount() {
        return replacements.size() + wordReplacements.size();
    }
}
