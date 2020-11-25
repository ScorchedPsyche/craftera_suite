package com.github.scorchedpsyche.craftera_suite.modules.main.database.types;

import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;

import java.sql.*;

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
                ConsoleUtils.logMessage(
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
        try (Connection conn = DriverManager.getConnection(databaseUrl); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlStatement);
            return true;
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite sql execution failed: " + sqlStatement);
            ConsoleUtils.logError( e.getMessage() );
        }

        return false;
    }

    @Override
    public boolean tableExists(String tableName)
    {
        try (Connection conn = DriverManager.getConnection(databaseUrl); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                            "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';");

            return rs.next();
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite tableExists check failed for table: " + tableName);
            ConsoleUtils.logError( e.getMessage() );
        }

        return false;
    }
}
