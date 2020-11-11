package com.github.scorchedpsyche.craftera_suite.modules.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils
{
    public static boolean isResultSetEmpty(ResultSet rs) throws SQLException
    {
        return (!rs.isBeforeFirst() && rs.getRow() == 0);
    }
}
