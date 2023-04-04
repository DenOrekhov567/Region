package dev.denny.region.manager;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;
import lombok.Getter;

import java.util.List;

public class ConfigManager {

    private Config config;

    @Getter
    public List commandsData;

    public ConfigManager(Plugin plugin) {
        plugin.saveDefaultConfig();

        config = plugin.getConfig();

        /*
         * Структура кофигурации
         * Первое слово - то, с чем связан ключ(command, event)
         *
         */
    }

}
