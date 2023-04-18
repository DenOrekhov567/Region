package dev.denny.region.manager;

import dev.denny.database.DatabasePlugin;
import dev.denny.database.utils.Database;

public class DatabaseManager {

    public static void initTables() {
        Database database = DatabasePlugin.getDatabase();

        String request = "CREATE TABLE IF NOT EXISTS regions(" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(10) NOT NULL," +
                "minX INTEGER NOT NULL," +
                "minY INTEGER NOT NULL," +
                "minZ INTEGER NOT NULL," +
                "maxX INTEGER NOT NULL," +
                "maxY INTEGER NOT NULL," +
                "maxZ INTEGER NOT NULL," +
                "world VARCHAR(10) NOT NULL" +
                ");"
        ;
        database.executeScheme(request);

        request = "CREATE TABLE IF NOT EXISTS region_members(" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(6) NOT NULL, " +
                "player VARCHAR(15) NOT NULL, " +
                "permission VARCHAR(10) NOT NULL" +
                ");"
        ;
        database.executeScheme(request);
    }
}