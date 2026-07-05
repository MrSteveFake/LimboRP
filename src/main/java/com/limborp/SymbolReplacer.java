package com.limborp;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolReplacer {
    
    private final LimboRP plugin;
    private final Map<Character, String> symbolToLetter;
    private final Map<String, String> wordReplacements;
    
    public SymbolReplacer(LimboRP plugin) {
        this.plugin = plugin;
        this.symbolToLetter = new HashMap<>();
        this.wordReplacements = new HashMap<>();
        
        // Загружаем замены
        loadReplacements();
    }
    
    private void loadReplacements() {
        // Стандартные замены символов
        symbolToLetter.clear();
        
        // Русские буквы
        symbolToLetter.put('А', "А");
        symbolToLetter.put('Б', "Б");
        symbolToLetter.put('В', "В");
        symbolToLetter.put('Г', "Г");
        symbolToLetter.put('Д', "Д");
        symbolToLetter.put('Е', "Е");
        symbolToLetter.put('Ё', "Ё");
        symbolToLetter.put('Ж', "Ж");
        symbolToLetter.put('З', "З");
        symbolToLetter.put('И', "И");
        symbolToLetter.put('Й', "Й");
        symbolToLetter.put('К', "К");
        symbolToLetter.put('Л', "Л");
        symbolToLetter.put('М', "М");
        symbolToLetter.put('Н', "Н");
        symbolToLetter.put('О', "О");
        symbolToLetter.put('П', "П");
        symbolToLetter.put('Р', "Р");
        symbolToLetter.put('С', "С");
        symbolToLetter.put('Т', "Т");
        symbolToLetter.put('У', "У");
        symbolToLetter.put('Ф', "Ф");
        symbolToLetter.put('Х', "Х");
        symbolToLetter.put('Ц', "Ц");
        symbolToLetter.put('Ч', "Ч");
        symbolToLetter.put('Ш', "Ш");
        symbolToLetter.put('Щ', "Щ");
        symbolToLetter.put('Ъ', "Ъ");
        symbolToLetter.put('Ы', "Ы");
        symbolToLetter.put('Ь', "Ь");
        symbolToLetter.put('Э', "Э");
        symbolToLetter.put('Ю', "Ю");
        symbolToLetter.put('Я', "Я");
        
        symbolToLetter.put('а', "а");
        symbolToLetter.put('б', "б");
        symbolToLetter.put('в', "в");
        symbolToLetter.put('г', "г");
        symbolToLetter.put('д', "д");
        symbolToLetter.put('е', "е");
        symbolToLetter.put('ё', "ё");
        symbolToLetter.put('ж', "ж");
        symbolToLetter.put('з', "з");
        symbolToLetter.put('и', "и");
        symbolToLetter.put('й', "й");
        symbolToLetter.put('к', "к");
        symbolToLetter.put('л', "л");
        symbolToLetter.put('м', "м");
        symbolToLetter.put('н', "н");
        symbolToLetter.put('о', "о");
        symbolToLetter.put('п', "п");
        symbolToLetter.put('р', "р");
        symbolToLetter.put('с', "с");
        symbolToLetter.put('т', "т");
        symbolToLetter.put('у', "у");
        symbolToLetter.put('ф', "ф");
        symbolToLetter.put('х', "х");
        symbolToLetter.put('ц', "ц");
        symbolToLetter.put('ч', "ч");
        symbolToLetter.put('ш', "ш");
        symbolToLetter.put('щ', "щ");
        symbolToLetter.put('ъ', "ъ");
        symbolToLetter.put('ы', "ы");
        symbolToLetter.put('ь', "ь");
        symbolToLetter.put('э', "э");
        symbolToLetter.put('ю', "ю");
        symbolToLetter.put('я', "я");
        
        // Английские буквы
        symbolToLetter.put('A', "A");
        symbolToLetter.put('B', "B");
        symbolToLetter.put('C', "C");
        symbolToLetter.put('D', "D");
        symbolToLetter.put('E', "E");
        symbolToLetter.put('F', "F");
        symbolToLetter.put('G', "G");
        symbolToLetter.put('H', "H");
        symbolToLetter.put('I', "I");
        symbolToLetter.put('J', "J");
        symbolToLetter.put('K', "K");
        symbolToLetter.put('L', "L");
        symbolToLetter.put('M', "M");
        symbolToLetter.put('N', "N");
        symbolToLetter.put('O', "O");
        symbolToLetter.put('P', "P");
        symbolToLetter.put('Q', "Q");
        symbolToLetter.put('R', "R");
        symbolToLetter.put('S', "S");
        symbolToLetter.put('T', "T");
        symbolToLetter.put('U', "U");
        symbolToLetter.put('V', "V");
        symbolToLetter.put('W', "W");
        symbolToLetter.put('X', "X");
        symbolToLetter.put('Y', "Y");
        symbolToLetter.put('Z', "Z");
        
        symbolToLetter.put('a', "a");
        symbolToLetter.put('b', "b");
        symbolToLetter.put('c', "c");
        symbolToLetter.put('d', "d");
        symbolToLetter.put('e', "e");
        symbolToLetter.put('f', "f");
        symbolToLetter.put('g', "g");
        symbolToLetter.put('h', "h");
        symbolToLetter.put('i', "i");
        symbolToLetter.put('j', "j");
        symbolToLetter.put('k', "k");
        symbolToLetter.put('l', "l");
        symbolToLetter.put('m', "m");
        symbolToLetter.put('n', "n");
        symbolToLetter.put('o', "o");
        symbolToLetter.put('p', "p");
        symbolToLetter.put('q', "q");
        symbolToLetter.put('r', "r");
        symbolToLetter.put('s', "s");
        symbolToLetter.put('t', "t");
        symbolToLetter.put('u', "u");
        symbolToLetter.put('v', "v");
        symbolToLetter.put('w', "w");
        symbolToLetter.put('x', "x");
        symbolToLetter.put('y', "y");
        symbolToLetter.put('z', "z");
        
        // Цифры
        symbolToLetter.put('0', "0");
        symbolToLetter.put('1', "1");
        symbolToLetter.put('2', "2");
        symbolToLetter.put('3', "3");
        symbolToLetter.put('4', "4");
        symbolToLetter.put('5', "5");
        symbolToLetter.put('6', "6");
        symbolToLetter.put('7', "7");
        symbolToLetter.put('8', "8");
        symbolToLetter.put('9', "9");
        
        // Специальные символы
        symbolToLetter.put('!', "!");
        symbolToLetter.put('?', "?");
        symbolToLetter.put('.', ".");
        symbolToLetter.put(',', ",");
        symbolToLetter.put(':', ":");
        symbolToLetter.put(';', ";");
        symbolToLetter.put('-', "-");
        symbolToLetter.put('+', "+");
        symbolToLetter.put('=', "=");
        symbolToLetter.put('(', "(");
        symbolToLetter.put(')', ")");
        symbolToLetter.put('[', "[");
        symbolToLetter.put(']', "]");
        symbolToLetter.put('{', "{");
        symbolToLetter.put('}', "}");
        symbolToLetter.put('<', "<");
        symbolToLetter.put('>', ">");
        symbolToLetter.put('/', "/");
        symbolToLetter.put('\\', "\\");
        symbolToLetter.put('|', "|");
        symbolToLetter.put('@', "@");
        symbolToLetter.put('#', "#");
        symbolToLetter.put('$', "$");
        symbolToLetter.put('%', "%");
        symbolToLetter.put('^', "^");
        symbolToLetter.put('&', "&");
        symbolToLetter.put('*', "*");
        symbolToLetter.put('_', "_");
        symbolToLetter.put('~', "~");
        symbolToLetter.put('`', "`");
        symbolToLetter.put('\'', "'");
        symbolToLetter.put('"', "\"");
        symbolToLetter.put(' ', " ");
    }
    
    public String replaceSymbols(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        StringBuilder result = new StringBuilder();
        
        // Сначала проверяем замены слов/фраз
        String processedText = text;
        for (Map.Entry<String, String> entry : wordReplacements.entrySet()) {
            processedText = processedText.replace(entry.getKey(), entry.getValue());
        }
        
        // Затем заменяем отдельные символы
        for (char c : processedText.toCharArray()) {
            String replacement = symbolToLetter.get(c);
            if (replacement != null) {
                result.append(replacement);
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    public void reloadReplacements() {
        loadReplacements();
        
        // Загружаем дополнительные замены из конфига
        if (plugin.getConfig().contains("replacements")) {
            for (String key : plugin.getConfig().getConfigurationSection("replacements").getKeys(false)) {
                String value = plugin.getConfig().getString("replacements." + key);
                
                if (key.length() == 1 && value != null) {
                    // Если ключ - один символ, а значение - строка
                    if (value.length() == 1) {
                        // Простая замена символ на символ
                        symbolToLetter.put(key.charAt(0), value);
                    } else {
                        // Замена символа на строку (с поддержкой цветов)
                        symbolToLetter.put(key.charAt(0), 
                            ChatColor.translateAlternateColorCodes('&', value));
                    }
                } else if (key.length() > 1 && value != null) {
                    // Замена слова/фразы
                    wordReplacements.put(key, 
                        ChatColor.translateAlternateColorCodes('&', value));
                }
            }
        }
        
        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Loaded " + symbolToLetter.size() + " symbol replacements");
            plugin.getLogger().info("Loaded " + wordReplacements.size() + " word replacements");
        }
    }
}
