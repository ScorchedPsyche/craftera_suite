package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
        CreateOrRetrieveDatabase();
    }

    @Override
    public void CreateOrRetrieveDatabase()
    {
        String url = "jdbc:sqlite:" + databasePath.toString();

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                cesCore.consoleUtils.logSuccess(
                        "A new SQL has been created at: " + databasePath.toString() );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
