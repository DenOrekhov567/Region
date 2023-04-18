package dev.denny.region.utils;

import dev.denny.database.DatabasePlugin;
import lombok.Getter;

public class MemberData {

    @Getter
    public Integer id;

    @Getter
    public String name;

    @Getter
    public String player;

    @Getter
    public String permission;

    public void delete() {
        String request = "DELETE FROM region_members WHERE name = '%1$s' AND player = '%2$s'";
        request = String.format(request, name, player);

        DatabasePlugin.getDatabase().query(request);
    }
}