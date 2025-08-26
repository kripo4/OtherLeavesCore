package top.leavesmc.leaves.branding;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.leavesmc.leaves.OtherLeavesConfig;
import top.leavesmc.leaves.OtherLeavesLogger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Менеджер брендинга для OtherLeavesCore
 * Отвечает за изменение брендинга в F3 экране и других местах
 */
public class BrandingManager implements Listener {
    
    private static BrandingManager instance;
    private boolean brandingApplied = false;
    
    public BrandingManager() {
        instance = this;
    }
    
    /**
     * Применить кастомный брендинг
     */
    public void applyBranding() {
        if (!OtherLeavesConfig.enableCustomBranding) {
            return;
        }
        
        try {
            // Попытка установить брендинг через системные свойства
            System.setProperty("paper.brand", OtherLeavesConfig.getF3Branding());
            System.setProperty("minecraft.brand", OtherLeavesConfig.getF3Branding());
            
            // Дополнительные методы для установки брендинга
            setBrandingReflection();
            
            brandingApplied = true;
            OtherLeavesLogger.info("Брендинг успешно применен: " + OtherLeavesConfig.getF3Branding());
            
        } catch (Exception e) {
            OtherLeavesLogger.error("Ошибка при применении брендинга", e);
        }
    }
    
    /**
     * Установка брендинга через рефлексию (для совместимости с разными версиями)
     */
    private void setBrandingReflection() {
        try {
            // Попытка найти и изменить поля брендинга в CraftServer
            Class<?> craftServerClass = Bukkit.getServer().getClass();
            
            // Поиск поля с брендингом
            Field[] fields = craftServerClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == String.class) {
                    field.setAccessible(true);
                    Object value = field.get(Bukkit.getServer());
                    
                    if (value instanceof String && 
                        (((String) value).contains("Paper") || 
                         ((String) value).contains("Leaves") || 
                         ((String) value).contains("Minecraft"))) {
                        
                        field.set(Bukkit.getServer(), OtherLeavesConfig.getF3Branding());
                        OtherLeavesLogger.debug("Брендинг установлен через рефлексию в поле: " + field.getName());
                    }
                }
            }
            
        } catch (Exception e) {
            OtherLeavesLogger.debug("Не удалось установить брендинг через рефлексию: " + e.getMessage());
        }
    }
    
    /**
     * Обработчик входа игрока для отправки кастомной информации о сервере
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!brandingApplied) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // Отправляем информацию о ядре администраторам
        if (player.hasPermission("otherleavescore.admin")) {
            Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("OtherLeavesCore"), () -> {
                player.sendMessage("§6[OtherLeavesCore] §aСервер работает на " + OtherLeavesConfig.getF3Branding());
                player.sendMessage("§6[OtherLeavesCore] §7Используйте /olc для получения информации о ядре");
            }, 20L); // Задержка в 1 секунду
        }
    }
    
    /**
     * Получить текущий брендинг
     */
    public String getCurrentBranding() {
        return OtherLeavesConfig.getF3Branding();
    }
    
    /**
     * Проверить применен ли брендинг
     */
    public boolean isBrandingApplied() {
        return brandingApplied;
    }
    
    /**
     * Получить экземпляр менеджера
     */
    public static BrandingManager getInstance() {
        return instance;
    }
    
    /**
     * Сбросить брендинг к стандартному
     */
    public void resetBranding() {
        try {
            System.clearProperty("paper.brand");
            System.clearProperty("minecraft.brand");
            brandingApplied = false;
            OtherLeavesLogger.info("Брендинг сброшен к стандартному");
        } catch (Exception e) {
            OtherLeavesLogger.error("Ошибка при сбросе брендинга", e);
        }
    }
}