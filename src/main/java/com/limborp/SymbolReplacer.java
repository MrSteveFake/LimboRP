package com.limborp;

import java.util.HashMap;
import java.util.Map;

public class SymbolReplacer {
    
    private final LimboRP plugin;
    private final Map<Character, Character> symbolToLetter;
    
    public SymbolReplacer(LimboRP plugin) {
        this.plugin = plugin;
        this.symbolToLetter = new HashMap<>();
        
        // Загружаем замены
        loadReplacements();
    }
    
    private void loadReplacements() {
        // Русские буквы
        symbolToLetter.put('\u0410', 'А'); // А
        symbolToLetter.put('\u0411', 'Б'); // Б
        symbolToLetter.put('\u0412', 'В'); // В
        symbolToLetter.put('\u0413', 'Г'); // Г
        symbolToLetter.put('\u0414', 'Д'); // Д
        symbolToLetter.put('\u0415', 'Е'); // Е
        symbolToLetter.put('\u0401', 'Ё'); // Ё
        symbolToLetter.put('\u0416', 'Ж'); // Ж
        symbolToLetter.put('\u0417', 'З'); // З
        symbolToLetter.put('\u0418', 'И'); // И
        symbolToLetter.put('\u0419', 'Й'); // Й
        symbolToLetter.put('\u041A', 'К'); // К
        symbolToLetter.put('\u041B', 'Л'); // Л
        symbolToLetter.put('\u041C', 'М'); // М
        symbolToLetter.put('\u041D', 'Н'); // Н
        symbolToLetter.put('\u041E', 'О'); // О
        symbolToLetter.put('\u041F', 'П'); // П
        symbolToLetter.put('\u0420', 'Р'); // Р
        symbolToLetter.put('\u0421', 'С'); // С
        symbolToLetter.put('\u0422', 'Т'); // Т
        symbolToLetter.put('\u0423', 'У'); // У
        symbolToLetter.put('\u0424', 'Ф'); // Ф
        symbolToLetter.put('\u0425', 'Х'); // Х
        symbolToLetter.put('\u0426', 'Ц'); // Ц
        symbolToLetter.put('\u0427', 'Ч'); // Ч
        symbolToLetter.put('\u0428', 'Ш'); // Ш
        symbolToLetter.put('\u0429', 'Щ'); // Щ
        symbolToLetter.put('\u042A', 'Ъ'); // Ъ
        symbolToLetter.put('\u042B', 'Ы'); // Ы
        symbolToLetter.put('\u042C', 'Ь'); // Ь
        symbolToLetter.put('\u042D', 'Э'); // Э
        symbolToLetter.put('\u042E', 'Ю'); // Ю
        symbolToLetter.put('\u042F', 'Я'); // Я
        
        symbolToLetter.put('\u0430', 'а'); // а
        symbolToLetter.put('\u0431', 'б'); // б
        symbolToLetter.put('\u0432', 'в'); // в
        symbolToLetter.put('\u0433', 'г'); // г
        symbolToLetter.put('\u0434', 'д'); // д
        symbolToLetter.put('\u0435', 'е'); // е
        symbolToLetter.put('\u0451', 'ё'); // ё
        symbolToLetter.put('\u0436', 'ж'); // ж
        symbolToLetter.put('\u0437', 'з'); // з
        symbolToLetter.put('\u0438', 'и'); // и
        symbolToLetter.put('\u0439', 'й'); // й
        symbolToLetter.put('\u043A', 'к'); // к
        symbolToLetter.put('\u043B', 'л'); // л
        symbolToLetter.put('\u043C', 'м'); // м
        symbolToLetter.put('\u043D', 'н'); // н
        symbolToLetter.put('\u043E', 'о'); // о
        symbolToLetter.put('\u043F', 'п'); // п
        symbolToLetter.put('\u0440', 'р'); // р
        symbolToLetter.put('\u0441', 'с'); // с
        symbolToLetter.put('\u0442', 'т'); // т
        symbolToLetter.put('\u0443', 'у'); // у
        symbolToLetter.put('\u0444', 'ф'); // ф
        symbolToLetter.put('\u0445', 'х'); // х
        symbolToLetter.put('\u0446', 'ц'); // ц
        symbolToLetter.put('\u0447', 'ч'); // ч
        symbolToLetter.put('\u0448', 'ш'); // ш
        symbolToLetter.put('\u0449', 'щ'); // щ
        symbolToLetter.put('\u044A', 'ъ'); // ъ
        symbolToLetter.put('\u044B', 'ы'); // ы
        symbolToLetter.put('\u044C', 'ь'); // ь
        symbolToLetter.put('\u044D', 'э'); // э
        symbolToLetter.put('\u044E', 'ю'); // ю
        symbolToLetter.put('\u044F', 'я'); // я
        
        // Английские буквы
        symbolToLetter.put('\uFF21', 'A'); // Ａ
        symbolToLetter.put('\uFF22', 'B'); // Ｂ
        symbolToLetter.put('\uFF23', 'C'); // Ｃ
        symbolToLetter.put('\uFF24', 'D'); // Ｄ
        symbolToLetter.put('\uFF25', 'E'); // Ｅ
        symbolToLetter.put('\uFF26', 'F'); // Ｆ
        symbolToLetter.put('\uFF27', 'G'); // Ｇ
        symbolToLetter.put('\uFF28', 'H'); // Ｈ
        symbolToLetter.put('\uFF29', 'I'); // Ｉ
        symbolToLetter.put('\uFF2A', 'J'); // Ｊ
        symbolToLetter.put('\uFF2B', 'K'); // Ｋ
        symbolToLetter.put('\uFF2C', 'L'); // Ｌ
        symbolToLetter.put('\uFF2D', 'M'); // Ｍ
        symbolToLetter.put('\uFF2E', 'N'); // Ｎ
        symbolToLetter.put('\uFF2F', 'O'); // Ｏ
        symbolToLetter.put('\uFF30', 'P'); // Ｐ
        symbolToLetter.put('\uFF31', 'Q'); // Ｑ
        symbolToLetter.put('\uFF32', 'R'); // Ｒ
        symbolToLetter.put('\uFF33', 'S'); // Ｓ
        symbolToLetter.put('\uFF34', 'T'); // Ｔ
        symbolToLetter.put('\uFF35', 'U'); // Ｕ
        symbolToLetter.put('\uFF36', 'V'); // Ｖ
        symbolToLetter.put('\uFF37', 'W'); // Ｗ
        symbolToLetter.put('\uFF38', 'X'); // Ｘ
        symbolToLetter.put('\uFF39', 'Y'); // Ｙ
        symbolToLetter.put('\uFF3A', 'Z'); // Ｚ
        
        symbolToLetter.put('\uFF41', 'a'); // ａ
        symbolToLetter.put('\uFF42', 'b'); // ｂ
        symbolToLetter.put('\uFF43', 'c'); // ｃ
        symbolToLetter.put('\uFF44', 'd'); // ｄ
        symbolToLetter.put('\uFF45', 'e'); // ｅ
        symbolToLetter.put('\uFF46', 'f'); // ｆ
        symbolToLetter.put('\uFF47', 'g'); // ｇ
        symbolToLetter.put('\uFF48', 'h'); // ｈ
        symbolToLetter.put('\uFF49', 'i'); // ｉ
        symbolToLetter.put('\uFF4A', 'j'); // ｊ
        symbolToLetter.put('\uFF4B', 'k'); // ｋ
        symbolToLetter.put('\uFF4C', 'l'); // ｌ
        symbolToLetter.put('\uFF4D', 'm'); // ｍ
        symbolToLetter.put('\uFF4E', 'n'); // ｎ
        symbolToLetter.put('\uFF4F', 'o'); // ｏ
        symbolToLetter.put('\uFF50', 'p'); // ｐ
        symbolToLetter.put('\uFF51', 'q'); // ｑ
        symbolToLetter.put('\uFF52', 'r'); // ｒ
        symbolToLetter.put('\uFF53', 's'); // ｓ
        symbolToLetter.put('\uFF54', 't'); // ｔ
        symbolToLetter.put('\uFF55', 'u'); // ｕ
        symbolToLetter.put('\uFF56', 'v'); // ｖ
        symbolToLetter.put('\uFF57', 'w'); // ｗ
        symbolToLetter.put('\uFF58', 'x'); // ｘ
        symbolToLetter.put('\uFF59', 'y'); // ｙ
        symbolToLetter.put('\uFF5A', 'z'); // ｚ
        
        // Цифры
        symbolToLetter.put('\uFF10', '0'); // ０
        symbolToLetter.put('\uFF11', '1'); // １
        symbolToLetter.put('\uFF12', '2'); // ２
        symbolToLetter.put('\uFF13', '3'); // ３
        symbolToLetter.put('\uFF14', '4'); // ４
        symbolToLetter.put('\uFF15', '5'); // ５
        symbolToLetter.put('\uFF16', '6'); // ６
        symbolToLetter.put('\uFF17', '7'); // ７
        symbolToLetter.put('\uFF18', '8'); // ８
        symbolToLetter.put('\uFF19', '9'); // ９
        
        // Специальные символы
        symbolToLetter.put('\uFF0B', '+'); // ＋
        symbolToLetter.put('\uFF0D', '-'); // －
        symbolToLetter.put('\uFF1D', '='); // ＝
        symbolToLetter.put('\uFF08', '('); // （
        symbolToLetter.put('\uFF09', ')'); // ）
        symbolToLetter.put('\uFF20', '@'); // ＠
        symbolToLetter.put('\uFF01', '!'); // ！
        symbolToLetter.put('\uFF1F', '?'); // ？
        symbolToLetter.put('\uFF03', '#'); // ＃
        symbolToLetter.put('\uFF04', '$'); // ＄
        symbolToLetter.put('\uFF05', '%'); // ％
        symbolToLetter.put('\uFF3E', '^'); // ＾
        symbolToLetter.put('\uFF06', '&'); // ＆
        symbolToLetter.put('\uFF0A', '*'); // ＊
    }
    
    public String replaceSymbols(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        StringBuilder result = new StringBuilder();
        
        for (char c : text.toCharArray()) {
            Character replacement = symbolToLetter.get(c);
            if (replacement != null) {
                result.append(replacement);
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    public void reloadReplacements() {
        symbolToLetter.clear();
        loadReplacements();
        
        // Если есть дополнительные замены в конфиге
        if (plugin.getConfig().contains("replacements")) {
            for (String key : plugin.getConfig().getConfigurationSection("replacements").getKeys(false)) {
                String value = plugin.getConfig().getString("replacements." + key);
                if (key.length() == 1 && value != null && value.length() == 1) {
                    symbolToLetter.put(key.charAt(0), value.charAt(0));
                }
            }
        }
    }
}
