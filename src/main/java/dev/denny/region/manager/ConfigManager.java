package dev.denny.region.manager;

import cn.nukkit.block.Block;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;

public class ConfigManager {

    @Getter
    private final File file;

    @Getter
    private final Config config;

    @Getter
    private final HashMap<Integer, HashMap<String, Integer>> mapBlocks = new HashMap<>();

    public ConfigManager(Plugin plugin) {
        file = new File(plugin.getDataFolder(), "/config.yml");
        config = new Config(file, Config.YAML);

        addDefaultParameters();

        HashMap<String, Integer> block1 = new HashMap<String, Integer>();
        block1.put("meta", 0);
        block1.put("radius", 3);

        HashMap<String, Integer> block2 = new HashMap<String, Integer>();
        block2.put("meta", 0);
        block2.put("radius", 5);

        HashMap<String, Integer> block3 = new HashMap<String, Integer>();
        block3.put("meta", 0);
        block3.put("radius", 7);

        HashMap<String, Integer> block4 = new HashMap<String, Integer>();
        block4.put("meta", 0);
        block4.put("radius", 10);

        HashMap<String, Integer> block5 = new HashMap<String, Integer>();
        block5.put("meta", 0);
        block5.put("radius", 15);

        mapBlocks.put(42, block1);
        mapBlocks.put(41, block2);
        mapBlocks.put(57, block3);
        mapBlocks.put(133, block4);
        mapBlocks.put(525, block5);
    }

    public Integer getRadius(Block block) {
        for(Integer id : mapBlocks.keySet()) {
            if(block.getId() == id) {
                return mapBlocks.get(id).get("radius");
            }
        }
        return null;
    }

    public void addDefaultParameters() {

    }

    public void addDefault(String path, Object object) {
        if(!config.exists(path)){
            config.set(path, object);
            config.save(file);
        }
    }
}