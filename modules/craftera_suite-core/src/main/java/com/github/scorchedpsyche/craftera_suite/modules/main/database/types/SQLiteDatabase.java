package com.github.scorchedpsyche.craftera_suite.modules.main.database.types;

import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase implements IDatabase
{
    String databaseUrl;

    public SQLiteDatabase(String pathWithFileName)
    {
        databaseUrl = "jdbc:sqlite:" + pathWithFileName;
        createOrRetrieveDatabase();
    }

    @Override
    public String getDatabaseUrl()
    {
        return databaseUrl;
    }

    @Override
    public Connection createOrRetrieveDatabase()
    {
        try (Connection conn = DriverManager.getConnection(databaseUrl)) {
            if (conn != null) {
                ConsoleUtils.logSuccess(
                        "Connection to SQLite has been established at: " + databaseUrl);
                return conn;
            }
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite database connection failed. Check folder write permissions at: " + databaseUrl);
            ConsoleUtils.logError( e.getMessage() );
        }

        return null;
    }

    @Override
    public boolean executeSql(String sqlStatement)
    {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlStatement);
            return true;
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite sql execution failed: " + sqlStatement);
            ConsoleUtils.logError( e.getMessage() );
        }

        return false;
    }
}
