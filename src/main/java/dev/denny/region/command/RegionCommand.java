package dev.denny.region.command;

import cn.nukkit.command.CommandSender;
import dev.denny.account.player.Gamer;
import dev.denny.rank.RankPlugin;

public class RegionCommand extends CommandBase {

    public RegionCommand() {
        super(
                "rg",
                "Регионы",
                "§7> Использовать: §a/rg <команда:список> <аргумент или пусто> <аргумент или пусто>"
        );

        setPermission("command.region.use");
        RankPlugin.getManager().addToPermissionList("command.region.use", 0);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        return executeSafe((Gamer) sender, args);
    }
}