package com.github.scorchedpsyche.craftera_suite.modules.interfaces;

import java.sql.Connection;

public interface IDatabase
{
    Connection createOrRetrieveDatabase();
    boolean executeSql(String sqlStatement);
}
