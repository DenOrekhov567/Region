package dev.denny.regionguard;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import dev.denny.regionguard.region.RegionManager;
import lombok.Getter;

public class RegionGuardPlugin extends PluginBase implements Listener {

    @Getter
    public static RegionGuardPlugin instance;
    @Getter
    public RegionManager manager;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        instance = this;
        manager = new RegionManager();
    }
}