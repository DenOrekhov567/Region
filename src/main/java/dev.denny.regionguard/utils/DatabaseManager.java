package dev.denny.regionguard.utils;

import dev.denny.database.DatabasePlugin;
import dev.denny.database.utils.Database;
import dev.denny.regionguard.RegionGuardPlugin;

import org.sql2o.Connection;

import java.util.List;

public class DatabaseManager {

    Connection connection;

    public DatabaseManager() {
        //Конструктор...
        DatabasePlugin plugin = (DatabasePlugin) RegionGuardPlugin.getInstance().getServer().getPluginManager().getPlugin("Database");
        Database database = plugin.getDatabase();
        database.executeSchematic("CREATE TABLE IF NOT EXISTS regions(" +
                "name TEXT NOT NULL," +
                "ownerName TEXT NOT NULL," +
                "minX INTEGER NOT NULL," +
                "minY INTEGER NOT NULL," +
                "minZ INTEGER NOT NULL," +
                "maxX INTEGER NOT NULL," +
                "maxY INTEGER NOT NULL," +
                "maxZ INTEGER NOT NULL," +
                "worldName TEXT NOT NULL" +
                ");"
        );
        database.executeSchematic("CREATE TABLE IF NOT EXISTS regions_members(" +
                "regionName TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                ");"
        );

        connection = database.getConnection();

        //Инициализация таблицы базы данных
        //database.getScematic.query
    }

    //Метод для запроса типа SELECT, возращает объект класса ResponseRegion с нужными полями из запроса
    public ResponseRegion queryRegions(String request) {
        List<ResponseRegion> readyList = connection.createQuery(request).executeAndFetch(ResponseRegion.class);
        if(!readyList.isEmpty()) {
            return readyList.get(0);
        }
        return null;
    }

    public List<ResponseRegionMembers> queryMembers(String request) {
        List<ResponseRegionMembers> readyList = connection.createQuery(request).executeAndFetch(ResponseRegionMembers.class);
        if(!readyList.isEmpty()) {
            return readyList;
        }
        return null;
    }

    public void queryEmpty(String request, boolean returned) {
        connection.createQuery(request).executeUpdate();
    }

}