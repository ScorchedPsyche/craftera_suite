package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase implements IDatabase
{
    CraftEraSuiteCore cesCore;
    File databasePath;

    public SQLiteDatabase(CraftEraSuiteCore cesCore, File pathWithFileName)
    {
        this.cesCore = cesCore;
        databasePath = pathWithFileName;
        createOrRetrieveDatabase();
    }


    @Override
    public Connection createOrRetrieveDatabase()
    {
        String url = "jdbc:sqlite:" + databasePath.toString();

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                cesCore.consoleUtils.logSuccess(
                        "A new SQLite database has been created at: " + databasePath.toString() );
                return conn;
            }
        } catch (SQLException e) {
            cesCore.consoleUtils.logError(
                    "SQLite database creation failed. Check folder write permissions at: " + databasePath.toString() );
            cesCore.consoleUtils.logError( "ERROR: " + e.getMessage() );
        }

        return null;
    }

    @Override
    public void createOrRetrieveTable(String name)
    {
        // TODO: Implement CreateOrRetrieveTable
    }
}
