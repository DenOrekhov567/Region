package dev.denny.region.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import dev.denny.dcore.dCorePlugin;

public class RegionCommand extends CommandBase {

    public RegionCommand() {
        super(
                "rg",
                "Регионы",
                "§7> §fИспользовать: §a/rg <команда:список> <аргумент или пусто> <аргумент или пусто>"
        );

        setPermission("command.region.use");
        dCorePlugin.getInstance().getManager().addToPermissionList("command.region.use", 0);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        return executeSafe((Player) sender, args);
    }
}