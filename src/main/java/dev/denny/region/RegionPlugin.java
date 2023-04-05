package dev.denny.region;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import dev.denny.region.command.RegionCommand;
import dev.denny.region.manager.RegionManager;
import lombok.Getter;

public class RegionPlugin extends PluginBase implements Listener {

    @Getter
    public static RegionManager manager;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        manager = new RegionManager(this);

        getServer().getCommandMap().register("", new RegionCommand());
    }
}