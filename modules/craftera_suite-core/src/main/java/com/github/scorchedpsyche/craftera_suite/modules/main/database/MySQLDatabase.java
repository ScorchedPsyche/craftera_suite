package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;

import java.sql.Connection;

public class MySQLDatabase implements IDatabase
{

    @Override
    public Connection createOrRetrieveDatabase()
    {
        return null;
    }

    @Override
    public void createOrRetrieveTable(String name)
    {

    }
}
