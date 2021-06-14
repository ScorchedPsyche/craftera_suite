package com.github.scorchedpsyche.craftera_suite.modules.util;

public class CommandUtil {
    public static String captureStringsStartingFromArgPositionAndRemoveDoubleQuotes(String[] args, int start_position)
    {
        StringBuilder title = new StringBuilder(""); // /ces seasons edit_selected title

        if ( args.length > start_position )
        {
            for(int i = start_position; i < args.length; i++)
            {
                title.append(args[i]).append(" ");
            }
            title = new StringBuilder(title.toString().trim());
        }

        return title.toString().replaceAll("^\"|\"$", "");
    }
}
