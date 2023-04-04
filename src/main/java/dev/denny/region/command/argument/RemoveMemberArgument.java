package dev.denny.region.command.argument;

import dev.denny.account.player.Gamer;
import dev.denny.region.RegionPlugin;
import dev.denny.region.manager.RegionManager;
import dev.denny.region.utils.Region;
import lombok.Getter;

public class RemoveMemberArgument extends Argument {

    @Getter
    public final String name = "remmember";

    @Override
    public Boolean execute(Gamer sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§7> §fИспользовать: §a/rg remmember <регион:слово> <игрок:список>");

            return false;
        }

        String regionName = args[0];
        String recipientName = args[1];

        RegionManager manager = RegionPlugin.getManager();
        Region region = manager.getRegion(regionName);

        Gamer recipient = (Gamer) sender.getServer().getPlayer(recipientName);

        //Если региона, имя которого в первом пост аргументе не существует
        if(region == null) {
            sender.sendMessage("§7> §fРегиона §a" + regionName + " §fне существует");

            return false;
        }

        //Если игрок, отправивший команду не является владельцем региона в первом пост аргументе
        if(!region.isOwner(sender)) {
            sender.sendMessage("§7> §aТы §fне можешь добавить в регион §aучастников");

            return false;
        }

        //Если имя игрока во втором пост аргументе - это имя игрока, отправившего команду
        if(recipientName.toLowerCase().equals(sender.getName().toLowerCase())) {
            sender.sendMessage("§7> §fНельзя добавить в регион §aсебя");

            return false;
        }

        //Если игрока с именем во втором пост аргументе нет в сети
        if(recipient == null) {
            //Если игрок, который указан во втором пост аргументе, является участником региона, указанному в первом пост аргументе
            if (region.isMember(recipientName)) {
                sender.sendMessage("§7> §a" + recipientName + " §fуже и так участник региона §a" + regionName);

                return false;
            }

            region.removeMember(recipientName);

            sender.sendMessage("§7> §a" + recipientName + " §fбыл удален из региона §a" + regionName);

            return true;
        }

        if(!region.isMember(recipient)) {
            sender.sendMessage("§7> §a" + recipient.getName() + " §fне участник региона §a" + regionName);

            return false;
        }

        region.removeMember(recipient);

        sender.sendMessage("§7> §a" + recipient.getName() + " §fбыл удален из региона §a" + regionName);
        recipient.sendMessage("§7> §a" + sender.getName() + " удалил §aтебя §fиз §a" + regionName);

        return true;
    }
}