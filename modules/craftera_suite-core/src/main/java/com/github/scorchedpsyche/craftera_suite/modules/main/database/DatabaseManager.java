package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.types.SQLiteDatabase;

import java.io.File;

public class DatabaseManager
{
    public enum DatabaseType {
        SQLite,
        MySQL
    }

    public DatabaseManager(CraftEraSuiteCore cesCore, DatabaseType databaseType)
    {
        switch(databaseType)
        {
            case MySQL:
                break;

            default: // SQLite
                database = new SQLiteDatabase(cesCore,
                                              cesCore.folderUtils.cesRootFolder + File.separator + "craftera_suite.db" );
                break;
        }
    }

    public IDatabase database;
}