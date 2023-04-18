package dev.denny.region;

import cn.nukkit.event.Listener;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import dev.denny.pattern.entity.EntityFactory;
import dev.denny.pattern.entity.Hologram;
import dev.denny.region.command.RegionCommand;
import dev.denny.region.manager.RegionManager;
import lombok.Getter;

public class RegionPlugin extends PluginBase implements Listener {

    @Getter
    public static RegionManager manager;

    @Override
    public void onEnable() {
        initComponents();
        initFlyTexts();
    }

    private void initComponents() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        manager = new RegionManager(this);

        getServer().getCommandMap().register("", new RegionCommand());
    }

    private void initFlyTexts() {
        Hologram hologram = EntityFactory.createHologram(new Location(-53.5, 24, -33.5, getServer().getLevelByName("spawn")), "Region_Hologram1");
        hologram.setText("§a§л3x3");
        hologram.build();

        hologram = EntityFactory.createHologram(new Location(-55.5, 24, -35.5, getServer().getLevelByName("spawn")), "Region_Hologram2");
        hologram.setText("§a§л5x5");
        hologram.build();

        hologram = EntityFactory.createHologram(new Location(-57.5, 24, -37.5, getServer().getLevelByName("spawn")), "Region_Hologram3");
        hologram.setText("§a§л7x7");
        hologram.build();

        hologram = EntityFactory.createHologram(new Location(-57.5, 24, -41.5, getServer().getLevelByName("spawn")), "Region_Hologram4");
        hologram.setText("§a§л10x10");
        hologram.build();

        hologram = EntityFactory.createHologram(new Location(-55.5, 24, -43.5, getServer().getLevelByName("spawn")), "Region_Hologram5");
        hologram.setText("§a§л15x15");
        hologram.build();

        hologram = EntityFactory.createHologram(new Location(-55.5, 26, -39.5, getServer().getLevelByName("spawn")), "Region_Hologram5");
        hologram.setText("§aПриватные блоки");
        hologram.build();
    }
}