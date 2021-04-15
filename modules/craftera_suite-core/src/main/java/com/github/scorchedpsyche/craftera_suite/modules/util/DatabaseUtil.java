package com.github.scorchedpsyche.craftera_suite.modules.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil
{
    public static boolean isResultSetNullOrEmpty(ResultSet rs) throws SQLException
    {
        // isBeforeFirst() returns
        //   - true if the cursor is before the first row;
        //   - false if the cursor is at any other  position or the result set contains no rows
        return !rs.isBeforeFirst();
    }
}
