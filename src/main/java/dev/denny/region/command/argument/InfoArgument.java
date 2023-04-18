package dev.denny.region.command.argument;

import cn.nukkit.Player;
import dev.denny.region.RegionPlugin;
import dev.denny.region.manager.RegionManager;
import dev.denny.region.utils.MemberData;
import dev.denny.region.utils.RegionData;
import lombok.Getter;

import java.util.List;

public class InfoArgument extends Argument {

    @Getter
    public final String name = "info";

    @Override
    public Boolean execute(Player sender, String[] args) {
        RegionManager manager = RegionPlugin.getManager();

        if (args.length == 0) {
            RegionData region = manager.getRegion(sender.getLocation());

            if (region == null) {
                    sender.sendMessage(getPrefixResponse() + "§fРегиона §aв этом месте §fнет");

                    return false;
            }

            String allMemberNames;
            List<MemberData> list = region.getMembers();
            StringBuilder sb = new StringBuilder();

            int count = 1;
            for (MemberData member : list) {
                String permission = member.getPermission();
                String entry;
                if (permission.equals("owner")) {
                    entry = "\n§f" + count + ". §a" + member.getPlayer() + " §f— Владелец, ";
                    sb.insert(0, entry); // добавляем строку в начало списка
                } else {
                    entry = "\n§f" + count + ". §7" + member.getPlayer() + " §f— Участник, ";
                    sb.append(entry); // добавляем строку в конец списка
                }
                count++;
            }
            allMemberNames = sb.toString().trim();
            allMemberNames = allMemberNames.substring(0, allMemberNames.length() - 1); // удаляем последнюю запятую

            sender.sendMessage(
                    getPrefixResponse() + "§fИнформация о регионе §a" + region.getName() + "\n" +
                            "§7• §fНазвание: §a" + region.getName() + "\n" +
                            "§7• §fУчастники: §a\n" + allMemberNames
            );
            return true;
        }

        String regionName = args[0];
        RegionData region = manager.getRegion(regionName);

        if (region == null) {
            sender.sendMessage(getPrefixResponse() + "§fРегиона §a" + regionName + " §fне существует");

            return false;
        }

        String allMemberNames;
        List<MemberData> list = region.getMembers();
        StringBuilder sb = new StringBuilder();

        int count = 1;
        for (MemberData member : list) {
            String permission = member.getPermission();
            String entry;
            if (permission.equals("owner")) {
                entry = "\n§f" + count + ". §a" + member.getPlayer() + " §f— Владелец, ";
                sb.insert(0, entry); // добавляем строку в начало списка
            } else {
                entry = "\n§f" + count + ". §7" + member.getPlayer() + " §f— Участник, ";
                sb.append(entry); // добавляем строку в конец списка
            }
            count++;
        }
        allMemberNames = sb.toString().trim();
        allMemberNames = allMemberNames.substring(0, allMemberNames.length() - 1); // удаляем последнюю запятую

        sender.sendMessage(
                "§7> §fИнформация о регионе §a" + region.getName() + "\n" +
                    "§7• §fНазвание: §a" + region.getName() + "\n" +
                    "§7• §fУчастники: §a\n" + allMemberNames
        );
        return true;
    }
}