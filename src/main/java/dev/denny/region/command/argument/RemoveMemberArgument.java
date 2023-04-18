package dev.denny.region.command.argument;

import cn.nukkit.Player;
import dev.denny.region.RegionPlugin;
import dev.denny.region.manager.RegionManager;
import dev.denny.region.utils.RegionData;
import lombok.Getter;

public class RemoveMemberArgument extends Argument {

    @Getter
    public final String name = "remmember";

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
        if (recipientName.equals(sender.getName())) {
            sender.sendMessage(getPrefixResponse() + "§fНельзя добавить в регион §aсебя");

            return false;
        }

        //Если игрока с именем во втором пост аргументе нет в сети
        if (recipient == null) {
            //Если игрок, который указан во втором пост аргументе, является участником региона, указанному в первом пост аргументе
            if (region.isMember(recipientName)) {
                sender.sendMessage(getPrefixResponse() + "§a" + recipientName + " §fуже и так участник региона §a" + regionName);

                return false;
            }

            region.getMember(recipientName).delete();

            sender.sendMessage(getPrefixResponse() + "§a" + recipientName + " §fбыл удален из региона §a" + regionName);

            return true;
        }

        if (!region.isMember(recipient)) {
            sender.sendMessage(getPrefixResponse() + "§a" + recipient.getName() + " §fне участник региона §a" + regionName);

            return false;
        }

        region.getMember(recipient).delete();

        sender.sendMessage(getPrefixResponse() + "§a" + recipient.getName() + " §fбыл удален из §a" + regionName);
        recipient.sendMessage(getPrefixResponse() + "§a" + sender.getName() + " удалил §aтебя §fиз §a" + regionName);

        return true;
    }
}