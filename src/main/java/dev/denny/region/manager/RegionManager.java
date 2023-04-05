package dev.denny.region.manager;

import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.Plugin;
import dev.denny.account.player.Gamer;
import dev.denny.region.RegionPlugin;
import dev.denny.region.utils.Region;
import lombok.Getter;

public class RegionManager {

    @Getter
    public final ConfigManager configManager;

    @Getter
    public final DatabaseManager databaseManager;

    public RegionManager(Plugin plugin) {
        configManager = new ConfigManager(plugin);
        databaseManager = new DatabaseManager();
    }

    //Проверяет нет ли в названии региона символы, отличные от пробела, английских букв, нижних подчёркиваний
    public boolean isValidName(String name) {
        return name.matches("^[a-zA-Z0-9_]+$");
    }

    //Получить регион в позиции Position
    public Region getRegion(Position position) {
        String request = "SELECT * FROM regions WHERE (minX <= %1$s AND %1$s <= maxX) AND (minY <= %2$s AND %2$s <= maxY) AND (minZ <= %3$s AND %3$s <= maxZ) AND worldName = '%4$s';";
        request = String.format(request, position.getX(), position.getY(), position.getZ(), position.getLevel().getFolderName());

        return getDatabaseManager().queryRegions(request);
    }

    //Получить регион c именем name
    public Region getRegion(String name) {
        String request = "SELECT * FROM regions WHERE name = '%1$s';";
        request = String.format(request, name);

        return getDatabaseManager().queryRegions(request);
    }

    //Не перекесается ли границы нового региона с границами других регионов
    public boolean isNewRegionIncludeOther(Block block) {
        Position privateBlockPosition = block.getLocation();
        Integer radius = RegionPlugin.getManager().getConfigManager().getRadius(block);

        double minX = privateBlockPosition.getX() - radius;
        double minY = privateBlockPosition.getY() - radius;
        double minZ = privateBlockPosition.getZ() - radius;

        double maxX = privateBlockPosition.getX() + radius;
        double maxY = privateBlockPosition.getY() + radius;
        double maxZ = privateBlockPosition.getZ() + radius;

        String worldName = block.getLocation().getLevel().getFolderName();

        String request = "SELECT * FROM regions WHERE (maxX >= %1$s AND minX <= %2$s) AND (maxY >= %3$s AND minY <= %4$s) AND (maxZ >= %5$s AND minZ <= %6$s) AND worldName = '%7$s';";
        request = String.format(request, minX, maxX, minY, maxY, minZ, maxZ, worldName);

        return getDatabaseManager().queryRegions(request) != null;
    }

    //Существует ли регион с именем regionName
    public boolean isRegionNameExists(String regionName) {
        String request = "SELECT * FROM regions WHERE name = '%1$s';";
        request = String.format(request, regionName);

        return getDatabaseManager().queryRegions(request) != null;
    }

    //Может ли этот игрок создавать приваты?
    //Данный метод создан сециально для моего плагина на права - Rank
    public boolean isCanCreate(Gamer player) {
        return true;
    }

    //Метод для создания региона
    public void createRegion(Gamer player, String name, Block block) {
        Position privateBlockPosition = block.getLocation();
        Integer radius = RegionPlugin.getManager().getConfigManager().getRadius(block);

        double minX = privateBlockPosition.getX() - radius;
        double minY = privateBlockPosition.getY() - radius;
        double minZ = privateBlockPosition.getZ() - radius;

        double maxX = privateBlockPosition.getX() + radius;
        double maxY = privateBlockPosition.getY() + radius;
        double maxZ = privateBlockPosition.getZ() + radius;

        String worldName = block.getLocation().getLevel().getFolderName();

        String request = "INSERT INTO regions(name, ownerName, minX, minY, minZ, maxX, maxY, maxZ, worldName) VALUES ('%1$s', '%2$s', %3$s, %4$s, %5$s, %6$s, %7$s, %8$s, '%9$s');";
        request = String.format(request, name, player.getName().toLowerCase(), minX, minY, minZ, maxX, maxY, maxZ, worldName);

        getDatabaseManager().queryEmpty(request);
    }

    //Метод для удаления региона
    public void deleteRegion(String regionName) {
        String request = "DELETE FROM regions WHERE name = '%1$s'";
        request = String.format(request, regionName);

        getDatabaseManager().queryEmpty(request);
    }
}