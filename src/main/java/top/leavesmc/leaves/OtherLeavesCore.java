package top.leavesmc.leaves;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import top.leavesmc.leaves.branding.BrandingManager;
import top.leavesmc.leaves.command.OtherLeavesCommand;

/**
 * Основной класс ядра OtherLeavesCore
 * Инициализирует все компоненты ядра и обеспечивает совместимость с Paper/Leaves
 */
public class OtherLeavesCore extends JavaPlugin {
    
    private static OtherLeavesCore instance;
    private BrandingManager brandingManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Логируем запуск
        OtherLeavesLogger.logStartup();
        
        // Инициализируем конфигурацию
        OtherLeavesConfig.init();
        
        // Регистрируем команды
        registerCommands();
        
        // Инициализируем брендинг
        initializeBranding();
        
        OtherLeavesLogger.info("OtherLeavesCore успешно запущен!");
    }
    
    @Override
    public void onDisable() {
        if (brandingManager != null) {
            brandingManager.resetBranding();
        }
        OtherLeavesLogger.info("OtherLeavesCore отключен");
    }
    
    /**
     * Регистрация команд ядра
     */
    private void registerCommands() {
        if (OtherLeavesConfig.enableOlcCommand) {
            // Регистрируем команду /olc
            OtherLeavesCommand olcCommand = new OtherLeavesCommand();
            getCommand("olc").setExecutor(olcCommand);
            getCommand("olc").setTabCompleter(olcCommand);
            
            OtherLeavesLogger.info("Команда /olc зарегистрирована");
        }
    }
    
    /**
     * Инициализация кастомного брендинга
     */
    private void initializeBranding() {
        if (OtherLeavesConfig.enableCustomBranding) {
            // Создаем и инициализируем менеджер брендинга
            brandingManager = new BrandingManager();
            brandingManager.applyBranding();
            
            // Регистрируем слушатель событий для брендинга
            Bukkit.getPluginManager().registerEvents(brandingManager, this);
        }
    }
    
    /**
     * Получить экземпляр плагина
     */
    public static OtherLeavesCore getInstance() {
        return instance;
    }
    
    /**
     * Получить версию ядра
     */
    public static String getCoreVersion() {
        return OtherLeavesConfig.version;
    }
    
    /**
     * Получить название ядра
     */
    public static String getCoreName() {
        return OtherLeavesConfig.serverName;
    }
    
    /**
     * Получить менеджер брендинга
     */
    public BrandingManager getBrandingManager() {
        return brandingManager;
    }
}
