package com.github.scorchedpsyche.craftera_suite.modules.interfaces;

import java.sql.Connection;

public interface IDatabase
{
    void createOrRetrieveDatabase();
    boolean executeSql(String sqlStatement);
    boolean tableExists(String tableName);
    String getDatabaseUrl();
}
