package com.github.scorchedpsyche.craftera_suite.modules.interfaces;

import java.sql.Connection;

public interface IDatabase
{
    public Connection createOrRetrieveDatabase();
    public void createOrRetrieveTable(String name);
}
