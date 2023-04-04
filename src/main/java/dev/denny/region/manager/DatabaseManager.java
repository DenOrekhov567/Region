package dev.denny.region.manager;

import dev.denny.database.DatabasePlugin;
import dev.denny.database.utils.Database;

import dev.denny.region.utils.Region;
import dev.denny.region.utils.Member;
import org.sql2o.Connection;

import java.util.List;

public class DatabaseManager {

    Connection connection;

    public DatabaseManager() {
        Database database = DatabasePlugin.getDatabase();

        database.executeScheme(
                "CREATE TABLE IF NOT EXISTS regions(" +
                    "name VARCHAR(10) NOT NULL," +
                    "ownerName VARCHAR(16) NOT NULL," +
                    "minX INTEGER NOT NULL," +
                    "minY INTEGER NOT NULL," +
                    "minZ INTEGER NOT NULL," +
                    "maxX INTEGER NOT NULL," +
                    "maxY INTEGER NOT NULL," +
                    "maxZ INTEGER NOT NULL," +
                    "worldName VARCHAR(10) NOT NULL" +
                ");"
        );

        database.executeScheme(
                "CREATE TABLE IF NOT EXISTS region_members(" +
                    "regionName VARCHAR(10) NOT NULL, " +
                    "name VARCHAR(16) NOT NULL" +
                ");"
        );
        connection = database.getConnection();
    }

    //Метод для запроса типа SELECT, возращает объект класса ResponseRegion с нужными полями из запроса
    public Region queryRegions(String request) {
        List<Region> readyList = connection.createQuery(request).executeAndFetch(Region.class);
        if(!readyList.isEmpty()) {
            return readyList.get(0);
        }
        return null;
    }

    public List<Member> queryMembers(String request) {
        List<Member> readyList = connection.createQuery(request).executeAndFetch(Member.class);
        if(!readyList.isEmpty()) {
            return readyList;
        }
        return null;
    }

    public void queryEmpty(String request) {
        connection.createQuery(request).executeUpdate();
    }

}