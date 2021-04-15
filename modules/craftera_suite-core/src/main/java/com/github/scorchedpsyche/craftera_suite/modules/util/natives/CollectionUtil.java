package com.github.scorchedpsyche.craftera_suite.modules.util.natives;

import java.util.Collection;
import java.util.HashMap;

public class CollectionUtil {
    public static boolean isNullOrEmpty(Collection<?> collection)
    {
        if ( collection == null || collection.isEmpty() )
        {
            return true;
        }

        return false;
    }

    public static boolean isNullOrEmpty(HashMap<?, ?> collection)
    {
        if ( collection == null || collection.isEmpty() )
        {
            return true;
        }

        return false;
    }
}
