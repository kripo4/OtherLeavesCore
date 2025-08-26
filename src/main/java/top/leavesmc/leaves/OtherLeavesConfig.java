package top.leavesmc.leaves;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Конфигурационный класс для OtherLeavesCore
 * Управляет настройками ядра и обеспечивает совместимость с Paper/Leaves
 */
public class OtherLeavesConfig {
    
    private static File CONFIG_FILE;
    private static YamlConfiguration config;
    
    // Настройки ядра
    public static String serverName = "OtherLeavesCore";
    public static String version = "1.21.4";
    public static boolean enableCustomBranding = true;
    public static boolean enableOlcCommand = true;
    
    /**
     * Инициализация конфигурации
     */
    public static void init() {
        CONFIG_FILE = new File("otherleavescore.yml");
        config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
        
        // Загружаем настройки
        serverName = config.getString("server-name", "OtherLeavesCore");
        version = config.getString("version", "1.21.4");
        enableCustomBranding = config.getBoolean("enable-custom-branding", true);
        enableOlcCommand = config.getBoolean("enable-olc-command", true);
        
        // Сохраняем конфигурацию если файл не существует
        if (!CONFIG_FILE.exists()) {
            saveConfig();
        }
        
        OtherLeavesLogger.info("OtherLeavesCore конфигурация загружена успешно");
    }
    
    /**
     * Сохранение конфигурации в файл
     */
    public static void saveConfig() {
        config.set("server-name", serverName);
        config.set("version", version);
        config.set("enable-custom-branding", enableCustomBranding);
        config.set("enable-olc-command", enableOlcCommand);
        
        try {
            config.save(CONFIG_FILE);
        } catch (IOException e) {
            OtherLeavesLogger.error("Не удалось сохранить конфигурацию OtherLeavesCore", e);
        }
    }
    
    /**
     * Перезагрузка конфигурации
     */
    public static void reload() {
        config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
        init();
    }
    
    /**
     * Получить брендинг для F3 экрана
     */
    public static String getF3Branding() {
        if (enableCustomBranding) {
            return "Powered by " + serverName;
        }
        return "Minecraft Server";
    }
}