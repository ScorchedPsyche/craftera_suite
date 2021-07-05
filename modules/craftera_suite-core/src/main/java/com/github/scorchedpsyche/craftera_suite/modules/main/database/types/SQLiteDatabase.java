package com.github.scorchedpsyche.craftera_suite.modules.main.database.types;

import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;

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
    public void createOrRetrieveDatabase()
    {
        try (Connection conn = DriverManager.getConnection(databaseUrl)) {
            if (conn != null) {
                ConsoleUtil.logMessage(
                        "Connection to SQLite has been established at: " + databaseUrl);
                conn.close();
            }
        } catch (SQLException e) {
            ConsoleUtil.logError(
                    "SQLite database connection failed. Check folder write permissions at: " + databaseUrl);
            ConsoleUtil.logError( e.getMessage() );
        }
    }

    @Override
    public boolean executeSqlAndDisplayErrorIfNeeded(String sqlStatement)
    {
        try (Connection conn = DriverManager.getConnection(databaseUrl); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlStatement);
            conn.close();
            return true;
        } catch (SQLException e) {
            ConsoleUtil.logErrorSQL( sqlStatement, e.getMessage() );
//            ConsoleUtil.logError( e.getMessage() );
        }

        return false;
    }

    @Override
    public boolean tableExists(String tableName)
    {
        try (Connection conn = DriverManager.getConnection(databaseUrl); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                            "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';");

            conn.close();
            return rs.next();
        } catch (SQLException e) {
            ConsoleUtil.logError(
                    "SQLite tableExists check failed for table: " + tableName);
            ConsoleUtil.logError( e.getMessage() );
        }

        return false;
    }
}
