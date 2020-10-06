package com.github.scorchedpsyche.craftera_suite.modules.main.database.types;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;

import java.sql.*;

public class SQLiteDatabase implements IDatabase
{
    CraftEraSuiteCore cesCore;
    String databaseUrl;

    public SQLiteDatabase(CraftEraSuiteCore cesCore, String pathWithFileName)
    {
        this.cesCore = cesCore;
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
                cesCore.consoleUtils.logSuccess(
                        "A new SQLite database has been created at: " + databaseUrl);
                return conn;
            }
        } catch (SQLException e) {
            cesCore.consoleUtils.logError(
                    "SQLite database creation failed. Check folder write permissions at: " + databaseUrl);
            cesCore.consoleUtils.logError( e.getMessage() );
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
            cesCore.consoleUtils.logError(
                    "SQLite sql execution failed: " + sqlStatement);
            cesCore.consoleUtils.logError( e.getMessage() );
        }

        return false;
    }
}