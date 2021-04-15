package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.types.SQLiteDatabase;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.FolderUtil;

import java.io.File;

public class DatabaseManager
{
    public enum DatabaseType {
        SQLite,
        MySQL
    }

    public DatabaseManager(DatabaseType databaseType)
    {
        switch(databaseType)
        {
            case MySQL:
                break;

            default: // SQLite
                database = new SQLiteDatabase(
                        FolderUtil.getOrCreateCesRootFolder() + File.separator + "craftera_suite.db" );
                break;
        }
    }

    public static IDatabase database;
}
