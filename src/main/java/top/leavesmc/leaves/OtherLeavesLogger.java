package top.leavesmc.leaves;

import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Логгер для OtherLeavesCore
 * Обеспечивает централизованное логирование с кастомным префиксом
 */
public class OtherLeavesLogger {
    
    private static final Logger logger = Logger.getLogger("OtherLeavesCore");
    private static final String PREFIX = "[OtherLeavesCore] ";
    
    /**
     * Логирование информационного сообщения
     */
    public static void info(String message) {
        logger.info(PREFIX + message);
        if (Bukkit.getServer() != null) {
            Bukkit.getServer().getLogger().info(PREFIX + message);
        }
    }
    
    /**
     * Логирование предупреждения
     */
    public static void warning(String message) {
        logger.warning(PREFIX + message);
        if (Bukkit.getServer() != null) {
            Bukkit.getServer().getLogger().warning(PREFIX + message);
        }
    }
    
    /**
     * Логирование ошибки
     */
    public static void error(String message) {
        logger.severe(PREFIX + message);
        if (Bukkit.getServer() != null) {
            Bukkit.getServer().getLogger().severe(PREFIX + message);
        }
    }
    
    /**
     * Логирование ошибки с исключением
     */
    public static void error(String message, Throwable throwable) {
        logger.log(Level.SEVERE, PREFIX + message, throwable);
        if (Bukkit.getServer() != null) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, PREFIX + message, throwable);
        }
    }
    
    /**
     * Логирование отладочной информации
     */
    public static void debug(String message) {
        if (isDebugEnabled()) {
            logger.info(PREFIX + "[DEBUG] " + message);
            if (Bukkit.getServer() != null) {
                Bukkit.getServer().getLogger().info(PREFIX + "[DEBUG] " + message);
            }
        }
    }
    
    /**
     * Проверка включен ли режим отладки
     */
    private static boolean isDebugEnabled() {
        return System.getProperty("otherleavescore.debug", "false").equalsIgnoreCase("true");
    }
    
    /**
     * Логирование стартового сообщения
     */
    public static void logStartup() {
        info("===========================================");
        info("  OtherLeavesCore v" + OtherLeavesConfig.version);
        info("  Fork of Leaves/Paper for Minecraft 1.21.4");
        info("  Commit: b04ebcb");
        info("===========================================");
    }
}