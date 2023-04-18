package dev.denny.region.utils;

import cn.nukkit.Player;
import dev.denny.database.DatabasePlugin;
import lombok.Getter;

import java.util.List;

public class RegionData {

    @Getter
    public Integer id;

    @Getter
    public String name;

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
    public String world;

    public Boolean isOwner(Player player) {
        String request = "SELECT * FROM region_members WHERE name = '%1$s' AND LOWER(player) = LOWER('%2$s') AND permission = 'owner';";
        request = String.format(request, name, player.getName());

        return DatabasePlugin.getDatabase().query(request, MemberData.class) != null;
    }

    public Boolean isMember(Player player) {
        boolean value = false;

        List<MemberData> members = getMembers();
        if(members != null && !members.isEmpty()) {
            for (MemberData member : members) {
                if (player.getName().toLowerCase().equals(member.getName().toLowerCase())) {
                    value = true;

                    break;
                }
            }
        }
        return value;
    }

    public Boolean isMember(String memberName) {
        boolean value = false;

        List<MemberData> members = getMembers();
        if(members != null && !members.isEmpty()) {
            for (MemberData member : members) {
                if (memberName.toLowerCase().equals(member.getName().toLowerCase())) {
                    value = true;

                    break;
                }
            }
        }
        return value;
    }

    public List<MemberData> getMembers() {
        String request = "SELECT * FROM region_members WHERE name = '%1$s';";
        request = String.format(request, name);

        return DatabasePlugin.getDatabase().query(request, MemberData.class);
    }

    public MemberData getMember(Player player) {
        String request = "SELECT * FROM region_members WHERE name = '%1$s' AND LOWER(player) = LOWER('%1$s');";
        request = String.format(request, name, player.getName());

        List<MemberData> response = DatabasePlugin.getDatabase().query(request, MemberData.class);

        return response != null ? response.get(0) : null;
    }

    public MemberData getMember(String player) {
        String request = "SELECT * FROM region_members WHERE name = '%1$s' AND LOWER(player) = LOWER('%2$s');";
        request = String.format(request, name, player);

        List<MemberData> response = DatabasePlugin.getDatabase().query(request, MemberData.class);

        return response != null ? response.get(0) : null;
    }

    public void addMember(Player player) {
        String request = "INSERT INTO region_members(name, player, permission) VALUES ('%1$s', '%2$s', '%3$s');";
        request = String.format(request, name, player.getName(), "member");

        DatabasePlugin.getDatabase().query(request);
    }

    public void delete() {
        String request = "DELETE FROM regions WHERE name = '%1$s'";
        request = String.format(request, name);

        List<MemberData> members = getMembers();
        if(members != null && !members.isEmpty()) {
            for (MemberData member : members) {
                member.delete();
            }
        }
        DatabasePlugin.getDatabase().query(request);
    }
}