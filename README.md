# OtherLeavesCore

**OtherLeavesCore** - это форк проекта Leaves для Minecraft 1.21.4.

## Основные возможности

- Кастомный F3 брендинг: "Powered by OtherLeavesCore"
- Команда /olc для управления ядром
- Полная совместимость с Paper/Leaves плагинами
- Оптимизированная производительность
- Расширенное логирование

## Установка

1. Соберите проект: `mvn clean package`
2. Скопируйте JAR файл в папку plugins
3. Перезапустите сервер

## Команды

- `/olc` - Основная информация о ядре
- `/olc info` - Детальная статистика
- `/olc reload` - Перезагрузка конфигурации
- `/olc help` - Справка

## Конфигурация

Основные настройки в файле `otherleavescore.yml`:

```yaml
server-name: "OtherLeavesCore"
version: "1.21.4"
enable-custom-branding: true
enable-olc-command: true
```

Версия: 1.21.4-b04ebcb
Основан на: Leaves/Paper

