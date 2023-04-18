package dev.denny.region.command.argument;

import cn.nukkit.Player;
import dev.denny.region.RegionPlugin;
import dev.denny.region.manager.RegionManager;
import dev.denny.region.utils.RegionData;
import lombok.Getter;

public class AddMemberArgument extends Argument {

    @Getter
    public final String name = "addmember";

    @Override
    public Boolean execute(Player sender, String[] args) {
        String regionName = args[0];
        String recipientName = args[1];

        RegionManager manager = RegionPlugin.getManager();
        RegionData region = manager.getRegion(regionName);

        Player recipient = sender.getServer().getPlayer(recipientName);

        //Если региона, имя которого в первом пост аргументе не существует
        if (region == null) {
            sender.sendMessage(getPrefixResponse() + "§fРегиона §a" + regionName + " §fне существует");

            return false;
        }

        //Если игрок, отправивший команду не является владельцем региона в первом пост аргументе
        if (!region.isOwner(sender)) {
            sender.sendMessage(getPrefixResponse() + "§aТы §fне можешь добавить в регион §aучастников");

            return false;
        }

        //Если имя игрока во втором пост аргументе - это имя игрока, отправившего команду
        if (recipientName.equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(getPrefixResponse() + "§fНельзя добавить в регион §aсебя");

            return false;
        }

        //Если игрок, который указал во втором пост аргументе, не в сети
        if (recipient == null) {
            sender.sendMessage(getPrefixResponse() + "§a" + recipientName + " §fне в сети");

            return false;
        }

        //Если игрок, который указан во втором пост аргументе, является участником региона, указанному в первом пост аргументе
        if (region.isMember(recipient)) {
            sender.sendMessage(getPrefixResponse() + "§a" + recipient.getName() + " §fуже и так участник региона §a" + regionName);

            return false;
        }

        region.addMember(recipient);

        sender.sendMessage(getPrefixResponse() + "§a" + recipient.getName() + " §fбыл добавлен в регион §a" + regionName);
        recipient.sendMessage(getPrefixResponse() + "§a" + sender.getName() + " добавил §aтебя §fв регион §a" + regionName);

        return true;
    }
}