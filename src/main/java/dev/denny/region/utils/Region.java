package dev.denny.region.utils;

import cn.nukkit.Server;
import dev.denny.account.player.Gamer;
import dev.denny.region.RegionPlugin;
import lombok.Getter;

import java.util.List;

public class Region {

    @Getter
    public String name;

    @Getter
    public String ownerName;

    @Getter
    public int minX;

    @Getter
    public int minY;

    @Getter
    public int minZ;

    @Getter
    public int maxX;

    @Getter
    public int maxY;

    @Getter
    public int maxZ;

    @Getter
    public String worldName;

    public Boolean isOwner(Gamer player) {
        return player.getName().toLowerCase().equals(ownerName);
    }

    public Boolean isMember(Gamer player) {
        boolean value = false;

        List<Member> members = getMembers();
        if(members != null && !members.isEmpty()) {
            for (Member member : members) {
                if (player.getName().toLowerCase().equals(member.getName())) {
                    value = true;

                    break;
                }
            }
        }
        return value;
    }

    public Boolean isMember(String memberName) {
        boolean value = false;

        List<Member> members = getMembers();
        if(members != null && !members.isEmpty()) {
            for (Member member : members) {
                if (memberName.toLowerCase().equals(member.getName())) {
                    value = true;

                    break;
                }
            }
        }
        return value;
    }

    public List<Member> getMembers() {
        String request = String.format("SELECT * FROM region_members WHERE regionName = '%1$s';", name);

        return RegionPlugin.getManager().getDatabaseManager().queryMembers(request);
    }

    public void addMember(Gamer player) {
        RegionPlugin.getManager().getDatabaseManager().queryEmpty(String.format("INSERT INTO region_members(regionName, name) VALUES ('%1$s', '%2$s');", name, player.getName().toLowerCase()));
    }

    public void removeMember(Gamer player) {
        RegionPlugin.getManager().getDatabaseManager().queryEmpty(String.format("DELETE FROM region_members WHERE name = '%1$s'", player.getName().toLowerCase()));
    }

    public void removeMember(String memberName) {
        RegionPlugin.getManager().getDatabaseManager().queryEmpty(String.format("DELETE FROM region_members WHERE name = '%1$s'", memberName.toLowerCase()));
    }
}