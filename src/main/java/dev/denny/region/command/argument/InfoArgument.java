package dev.denny.region.command.argument;

import dev.denny.account.player.Gamer;
import dev.denny.region.RegionPlugin;
import dev.denny.region.manager.RegionManager;
import dev.denny.region.utils.Region;
import lombok.Getter;

public class InfoArgument extends Argument {

    @Getter
    public final String name = "info";

    @Override
    public Boolean execute(Gamer sender, String[] args) {
        RegionManager manager = RegionPlugin.getManager();

        if (args.length == 0) {
            Region region = manager.getRegion(sender.getLocation());

            if (region == null) {
                    sender.sendMessage("§7> §fРегиона §aв этом месте §fнет");

                    return false;
            }
            sender.sendMessage(
                    "§7> §fИнформация о регионе §a" + region.getName() + "\n" +
                            "§7- §fНазвание: §a" + region.getName() + "\n" +
                            "§7- §fВладелец: §a" + region.getOwnerName()
            );
            return true;
        } else {
            String regionName = args[0];
            Region region = manager.getRegion(regionName);

            if (region == null) {
                sender.sendMessage("§7> §fРегиона §a" + regionName + " §fне существует");

                return false;
            }

            sender.sendMessage(
                    "§7> §fИнформация о регионе §a" + region.getName() + "\n" +
                            "§7- §fНазвание: §a" + region.getName() + "\n" +
                            "§7- §fВладелец: §a" + region.getOwnerName()
            );
            return true;
        }
    }
}