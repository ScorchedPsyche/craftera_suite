package com.github.scorchedpsyche.craftera_suite.modules.interfaces;

import java.sql.Connection;

public interface IDatabase
{
    void createOrRetrieveDatabase();
    boolean executeSqlAndDisplayErrorIfNeeded(String sqlStatement);
    boolean tableExists(String tableName);
    String getDatabaseUrl();
}
