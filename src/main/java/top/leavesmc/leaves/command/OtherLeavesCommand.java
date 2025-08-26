package top.leavesmc.leaves.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.leavesmc.leaves.OtherLeavesConfig;
import top.leavesmc.leaves.OtherLeavesLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Команда /olc (OtherLeavesCore)
 * Показывает информацию о ядре сервера и доступные подкоманды
 */
public class OtherLeavesCommand implements CommandExecutor, TabCompleter {
    
    private static final String PERMISSION_BASE = "otherleavescore.command";
    private static final String PERMISSION_RELOAD = "otherleavescore.command.reload";
    private static final String PERMISSION_INFO = "otherleavescore.command.info";
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        // Проверка базового разрешения
        if (!sender.hasPermission(PERMISSION_BASE)) {
            sender.sendMessage(Component.text("У вас нет прав для использования этой команды!", NamedTextColor.RED));
            return true;
        }
        
        if (args.length == 0) {
            sendMainInfo(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "info":
            case "version":
                if (!sender.hasPermission(PERMISSION_INFO)) {
                    sender.sendMessage(Component.text("У вас нет прав для просмотра информации о ядре!", NamedTextColor.RED));
                    return true;
                }
                sendDetailedInfo(sender);
                break;
                
            case "reload":
                if (!sender.hasPermission(PERMISSION_RELOAD)) {
                    sender.sendMessage(Component.text("У вас нет прав для перезагрузки конфигурации!", NamedTextColor.RED));
                    return true;
                }
                reloadConfig(sender);
                break;
                
            case "help":
                sendHelp(sender);
                break;
                
            default:
                sender.sendMessage(Component.text("Неизвестная подкоманда! Используйте /olc help для справки.", NamedTextColor.RED));
                break;
        }
        
        return true;
    }
    
    /**
     * Отправка основной информации о ядре
     */
    private void sendMainInfo(@NotNull CommandSender sender) {
        sender.sendMessage(Component.text("═══════════════════════════════════════", NamedTextColor.GOLD, TextDecoration.BOLD));
        sender.sendMessage(Component.text("         OtherLeavesCore", NamedTextColor.GREEN, TextDecoration.BOLD));
        sender.sendMessage(Component.text("═══════════════════════════════════════", NamedTextColor.GOLD, TextDecoration.BOLD));
        sender.sendMessage(Component.text("Версия: ", NamedTextColor.YELLOW).append(Component.text(OtherLeavesConfig.version, NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("Основан на: ", NamedTextColor.YELLOW).append(Component.text("Leaves/Paper", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("Minecraft: ", NamedTextColor.YELLOW).append(Component.text("1.21.4", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("Коммит: ", NamedTextColor.YELLOW).append(Component.text("b04ebcb", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text(""));
        sender.sendMessage(Component.text("Используйте ", NamedTextColor.GRAY).append(Component.text("/olc help", NamedTextColor.AQUA)).append(Component.text(" для справки", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("═══════════════════════════════════════", NamedTextColor.GOLD, TextDecoration.BOLD));
    }
    
    /**
     * Отправка детальной информации о ядре
     */
    private void sendDetailedInfo(@NotNull CommandSender sender) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        sender.sendMessage(Component.text("═══════════════════════════════════════", NamedTextColor.GOLD, TextDecoration.BOLD));
        sender.sendMessage(Component.text("    Детальная информация о ядре", NamedTextColor.GREEN, TextDecoration.BOLD));
        sender.sendMessage(Component.text("═══════════════════════════════════════", NamedTextColor.GOLD, TextDecoration.BOLD));
        sender.sendMessage(Component.text("Ядро: ", NamedTextColor.YELLOW).append(Component.text("OtherLeavesCore v" + OtherLeavesConfig.version, NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("Сервер: ", NamedTextColor.YELLOW).append(Component.text(Bukkit.getVersion(), NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("Онлайн: ", NamedTextColor.YELLOW).append(Component.text(Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(), NamedTextColor.WHITE)));
        sender.sendMessage(Component.text(""));
        sender.sendMessage(Component.text("Память:", NamedTextColor.AQUA, TextDecoration.BOLD));
        sender.sendMessage(Component.text("  Использовано: ", NamedTextColor.YELLOW).append(Component.text(usedMemory + " MB", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("  Всего: ", NamedTextColor.YELLOW).append(Component.text(totalMemory + " MB", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("  Максимум: ", NamedTextColor.YELLOW).append(Component.text(maxMemory + " MB", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text(""));
        sender.sendMessage(Component.text("Java: ", NamedTextColor.YELLOW).append(Component.text(System.getProperty("java.version"), NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("ОС: ", NamedTextColor.YELLOW).append(Component.text(System.getProperty("os.name"), NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("═══════════════════════════════════════", NamedTextColor.GOLD, TextDecoration.BOLD));
    }
    
    /**
     * Перезагрузка конфигурации
     */
    private void reloadConfig(@NotNull CommandSender sender) {
        try {
            OtherLeavesConfig.reload();
            sender.sendMessage(Component.text("Конфигурация OtherLeavesCore успешно перезагружена!", NamedTextColor.GREEN));
            OtherLeavesLogger.info("Конфигурация перезагружена пользователем " + sender.getName());
        } catch (Exception e) {
            sender.sendMessage(Component.text("Ошибка при перезагрузке конфигурации!", NamedTextColor.RED));
            OtherLeavesLogger.error("Ошибка при перезагрузке конфигурации", e);
        }
    }
    
    /**
     * Отправка справки по командам
     */
    private void sendHelp(@NotNull CommandSender sender) {
        sender.sendMessage(Component.text("═══════════════════════════════════════", NamedTextColor.GOLD, TextDecoration.BOLD));
        sender.sendMessage(Component.text("        Справка по командам /olc", NamedTextColor.GREEN, TextDecoration.BOLD));
        sender.sendMessage(Component.text("═══════════════════════════════════════", NamedTextColor.GOLD, TextDecoration.BOLD));
        sender.sendMessage(Component.text("/olc", NamedTextColor.AQUA).append(Component.text(" - Основная информация о ядре", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/olc info", NamedTextColor.AQUA).append(Component.text(" - Детальная информация и статистика", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/olc reload", NamedTextColor.AQUA).append(Component.text(" - Перезагрузить конфигурацию", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/olc help", NamedTextColor.AQUA).append(Component.text(" - Показать эту справку", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text(""));
        sender.sendMessage(Component.text("Права доступа:", NamedTextColor.YELLOW, TextDecoration.BOLD));
        sender.sendMessage(Component.text("  otherleavescore.command", NamedTextColor.GRAY).append(Component.text(" - Базовое использование", NamedTextColor.DARK_GRAY)));
        sender.sendMessage(Component.text("  otherleavescore.command.info", NamedTextColor.GRAY).append(Component.text(" - Просмотр информации", NamedTextColor.DARK_GRAY)));
        sender.sendMessage(Component.text("  otherleavescore.command.reload", NamedTextColor.GRAY).append(Component.text(" - Перезагрузка конфига", NamedTextColor.DARK_GRAY)));
        sender.sendMessage(Component.text("═══════════════════════════════════════", NamedTextColor.GOLD, TextDecoration.BOLD));
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("info", "version", "reload", "help");
            String partial = args[0].toLowerCase();
            
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(partial)) {
                    // Проверяем права доступа для каждой подкоманды
                    switch (subCommand) {
                        case "info":
                        case "version":
                            if (sender.hasPermission(PERMISSION_INFO)) {
                                completions.add(subCommand);
                            }
                            break;
                        case "reload":
                            if (sender.hasPermission(PERMISSION_RELOAD)) {
                                completions.add(subCommand);
                            }
                            break;
                        case "help":
                            if (sender.hasPermission(PERMISSION_BASE)) {
                                completions.add(subCommand);
                            }
                            break;
                    }
                }
            }
        }
        
        return completions;
    }
}