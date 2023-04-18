package dev.denny.region.manager;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.Plugin;
import dev.denny.database.DatabasePlugin;
import dev.denny.database.utils.Database;
import dev.denny.region.RegionPlugin;
import dev.denny.region.utils.MemberData;
import dev.denny.region.utils.RegionData;
import lombok.Getter;

import java.util.List;

public class RegionManager {

    @Getter
    public final ConfigManager config;

    public RegionManager(Plugin plugin) {
        config = new ConfigManager(plugin);

        DatabaseManager.initTables();
    }

    public boolean isValidName(String name) {
        return name.matches("^[a-zA-Z0-9_]+$");
    }

    public RegionData getRegion(Position position) {
        String request = "SELECT * FROM regions WHERE (minX <= %1$s AND %1$s <= maxX) AND (minY <= %2$s AND %2$s <= maxY) AND (minZ <= %3$s AND %3$s <= maxZ) AND world = '%4$s';";
        request = String.format(request, position.getX(), position.getY(), position.getZ(), position.getLevel().getFolderName());

        List<RegionData> response = DatabasePlugin.getDatabase().query(request, RegionData.class);

        return response != null ? response.get(0) : null;
    }

    public RegionData getRegion(String name) {
        String request = "SELECT * FROM regions WHERE LOWER(name) = LOWER('%1$s');";
        request = String.format(request, name);

        List<RegionData> response = DatabasePlugin.getDatabase().query(request, RegionData.class);

        return response != null ? response.get(0) : null;
    }

    public boolean isNewRegionIncludeOther(Block block) {
        Position privateBlockPosition = block.getLocation();
        Integer radius = config.getRadius(block);

        double minX = privateBlockPosition.getX() - radius;
        double minY = privateBlockPosition.getY() - radius;
        double minZ = privateBlockPosition.getZ() - radius;

        double maxX = privateBlockPosition.getX() + radius;
        double maxY = privateBlockPosition.getY() + radius;
        double maxZ = privateBlockPosition.getZ() + radius;

        String world = block.getLocation().getLevel().getFolderName();

        String request = "SELECT * FROM regions WHERE (maxX >= %1$s AND minX <= %2$s) AND (maxY >= %3$s AND minY <= %4$s) AND (maxZ >= %5$s AND minZ <= %6$s) AND world = '%7$s';";
        request = String.format(request, minX, maxX, minY, maxY, minZ, maxZ, world);

        return DatabasePlugin.getDatabase().query(request, RegionData.class) != null;
    }

    public boolean isRegionNameExists(String name) {
        String request = "SELECT * FROM regions WHERE LOWER(name) = LOWER('%1$s');";
        request = String.format(request, name);

        return DatabasePlugin.getDatabase().query(request, RegionData.class) != null;
    }

    public boolean isCanCreate(Player player) {
        String request = "SELECT * FROM region_members WHERE LOWER(player) = LOWER('%1$s') AND permission = '%2$s';";
        request = String.format(request, player.getName(), "owner");

        List<MemberData> response = DatabasePlugin.getDatabase().query(request, MemberData.class);

        if(response != null) return response.size() < 3;

        return true;
    }

    public void createRegion(Player player, String name, Block block) {
        Position privateBlockPosition = block.getLocation();
        Integer radius = RegionPlugin.getManager().getConfig().getRadius(block);

        double minX = privateBlockPosition.getX() - radius;
        double minY = privateBlockPosition.getY() - radius;
        double minZ = privateBlockPosition.getZ() - radius;

        double maxX = privateBlockPosition.getX() + radius;
        double maxY = privateBlockPosition.getY() + radius;
        double maxZ = privateBlockPosition.getZ() + radius;

        String world = block.getLocation().getLevel().getFolderName();

        Database database = DatabasePlugin.getDatabase();

        String request = "INSERT INTO regions(name, minX, minY, minZ, maxX, maxY, maxZ, world) VALUES ('%1$s', %2$s, %3$s, %4$s, %5$s, %6$s, %7$s, '%8$s');";
        request = String.format(request, name, minX, minY, minZ, maxX, maxY, maxZ, world);
        database.query(request);

        request = "INSERT INTO region_members(name, player, permission) VALUES ('%1$s', '%2$s', '%3$s');";
        request = String.format(request, name, player.getName(), "owner");
        database.query(request);
    }
}