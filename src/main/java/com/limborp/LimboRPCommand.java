package com.limborp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LimboRPCommand implements CommandExecutor, TabCompleter {
    
    private final LimboRP plugin;
    
    public LimboRPCommand(LimboRP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("limborp.admin")) {
            sender.sendMessage(ChatColor.RED + "У вас нет прав для использования этой команды.");
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
                
            case "status":
                handleStatus(sender);
                break;
                
            case "check":
                handleCheck(sender, args);
                break;
                
            case "debug":
                handleDebug(sender);
                break;
                
            case "test":
                handleTest(sender, args);
                break;
                
            default:
                sendHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void handleReload(CommandSender sender) {
        plugin.reloadConfig();
        plugin.getSymbolReplacer().reloadReplacements();
        sender.sendMessage(ChatColor.GREEN + "[LimboRP] " + ChatColor.WHITE + "Конфигурация перезагружена!");
        plugin.getLogger().info("Configuration reloaded by " + sender.getName());
    }
    
    private void handleStatus(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эта команда только для игроков!");
            return;
        }
        
        Player player = (Player) sender;
        ResourcePackManager.ResourcePackStatus status = 
            plugin.getPackManager().getStatus(player);
        boolean hasPack = plugin.getPackManager().hasResourcePack(player);
        
        sender.sendMessage(ChatColor.YELLOW + "=== LimboRP Статус ===");
        sender.sendMessage(ChatColor.WHITE + "Статус ресурспака: " + 
            ChatColor.AQUA + status.name());
        sender.sendMessage(ChatColor.WHITE + "Ресурспак загружен: " + 
            (hasPack ? ChatColor.GREEN + "Да" : ChatColor.RED + "Нет"));
        sender.sendMessage(ChatColor.WHITE + "Замена символов: " + 
            (!hasPack ? ChatColor.GREEN + "Активна" : ChatColor.RED + "Отключена"));
        
        // Тест замены
        String testText = "Привет, мир! Hello, world! 123";
        String replaced = plugin.getSymbolReplacer().replaceSymbols(testText);
        sender.sendMessage(ChatColor.WHITE + "Тест замены: " + 
            ChatColor.GRAY + testText + ChatColor.WHITE + " -> " + 
            ChatColor.GOLD + replaced);
    }
    
    private void handleCheck(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Использование: /limborp check <игрок>");
            return;
        }
        
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target != null) {
            ResourcePackManager.ResourcePackStatus status = 
                plugin.getPackManager().getStatus(target);
            boolean hasPack = plugin.getPackManager().hasResourcePack(target);
            
            sender.sendMessage(ChatColor.YELLOW + "=== LimboRP Статус игрока " + 
                target.getName() + " ===");
            sender.sendMessage(ChatColor.WHITE + "Статус ресурспака: " + 
                ChatColor.AQUA + status.name());
            sender.sendMessage(ChatColor.WHITE + "Ресурспак загружен: " + 
                (hasPack ? ChatColor.GREEN + "Да" : ChatColor.RED + "Нет"));
        } else {
            sender.sendMessage(ChatColor.RED + "Игрок не найден!");
        }
    }
    
    private void handleDebug(CommandSender sender) {
        boolean currentDebug = plugin.getConfig().getBoolean("debug", false);
        plugin.getConfig().set("debug", !currentDebug);
        plugin.saveConfig();
        
        String status = !currentDebug ? ChatColor.GREEN + "включен" : ChatColor.RED + "выключен";
        sender.sendMessage(ChatColor.YELLOW + "[LimboRP] " + ChatColor.WHITE + 
            "Режим отладки " + status);
    }
    
    private void handleTest(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Использование: /limborp test <текст>");
            return;
        }
        
        // Собираем текст из аргументов
        StringBuilder testText = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            testText.append(args[i]);
            if (i < args.length - 1) {
                testText.append(" ");
            }
        }
        
        String original = testText.toString();
        String replaced = plugin.getSymbolReplacer().replaceSymbols(original);
        
        sender.sendMessage(ChatColor.YELLOW + "=== Тест замены ===");
        sender.sendMessage(ChatColor.WHITE + "Оригинал: " + ChatColor.GRAY + original);
        sender.sendMessage(ChatColor.WHITE + "Результат: " + ChatColor.GOLD + replaced);
        
        if (original.equals(replaced)) {
            sender.sendMessage(ChatColor.RED + "Символы не были заменены! Проверьте конфигурацию.");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Замена выполнена успешно!");
        }
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "=== LimboRP Помощь ===");
        sender.sendMessage(ChatColor.WHITE + "/limborp reload " + ChatColor.GRAY + "- Перезагрузить конфигурацию");
        sender.sendMessage(ChatColor.WHITE + "/limborp status " + ChatColor.GRAY + "- Показать ваш статус");
        sender.sendMessage(ChatColor.WHITE + "/limborp check <игрок> " + ChatColor.GRAY + "- Проверить статус игрока");
        sender.sendMessage(ChatColor.WHITE + "/limborp debug " + ChatColor.GRAY + "- Вкл/выкл режим отладки");
        sender.sendMessage(ChatColor.WHITE + "/limborp test <текст> " + ChatColor.GRAY + "- Тестировать замену символов");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            String[] subCommands = {"reload", "status", "check", "debug", "test"};
            for (String sub : subCommands) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("check")) {
            String partialName = args[1].toLowerCase();
            completions = plugin.getServer().getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(partialName))
                .collect(Collectors.toList());
        }
        
        return completions;
    }
}
