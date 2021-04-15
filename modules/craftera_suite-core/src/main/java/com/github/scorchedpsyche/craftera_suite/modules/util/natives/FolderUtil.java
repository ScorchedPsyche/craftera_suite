package com.github.scorchedpsyche.craftera_suite.modules.util.natives;

import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class FolderUtil {
    private static File pluginsFolder;
    private static File cesRootFolder;

    public static synchronized void setup()
    {
        pluginsFolder = getPluginsFolder();
        cesRootFolder = getOrCreateCesRootFolder();
    }

    @Nullable
    public static File getOrCreatePluginSubfolder(String pluginName)
    {
        File pluginSubfolder = new File(cesRootFolder.toString() + File.separator + pluginName.split("-")[1]);

        if( !pluginSubfolder.exists() )
        {
            if( !pluginSubfolder.mkdirs() )
            {
                ConsoleUtil.logError(
                        "Plugin configuration folder failed to be created: check folder write permissions or try to " +
                                "create the folder manually. If everything looks OK and the issue still persists, " +
                                "report this to the developer. FOLDER PATH STRUCTURE THAT SHOULD HAVE BEEN CREATED: " +
                                ChatColor.YELLOW + pluginSubfolder.toString());
                return null;
            }
        }

        return pluginSubfolder;
    }

    /**
     * Retrieves or creates the root CraftEra Suite folder which holds all of the suite plugin's configuration files.
     * @return The root folder inside Plugins folder for the CraftEra Suite configurations
     */
    public static synchronized File getOrCreateCesRootFolder()
    {
        if( cesRootFolder != null )
        {
            return cesRootFolder;
        }

        String cesRootPath = getPluginsFolder().toString();
        cesRootPath += File.separator + "craftera_suite";
        File cesFolder = new File( cesRootPath );

        if ( !cesFolder.exists() )
        {
            if (!cesFolder.mkdirs()) {
                ConsoleUtil.logError(
                        "Main CraftEra Suite configuration folder failed to be created: check folder write " +
                                "permissions or try to create the folder manually. If everything looks OK and the " +
                                "issue still persists, report this to the developer. FOLDER PATH STRUCTURE THAT " +
                                "SHOULD HAVE BEEN CREATED: " + ChatColor.YELLOW + cesFolder.toString());
            }
        }

        return cesFolder;
    }

    /** 
     * @return Returns root Plugins folder.
    */
    @Nullable
    private static synchronized File getPluginsFolder()
    {
        if( pluginsFolder != null )
        {
            return pluginsFolder;
        }

        if(Bukkit.getPluginManager().isPluginEnabled("craftera_suite-core") )
        {
            File dataFolder = Bukkit.getPluginManager().getPlugin("craftera_suite-core").getDataFolder();

            StringBuilder path;
            try {
                path = new StringBuilder(dataFolder.getCanonicalPath());
            } catch( IOException ex ) {
                path = new StringBuilder(dataFolder.getAbsolutePath());
            }

            String pattern = Pattern.quote(File.separator);
            String[] pathSplit = path.toString().split(pattern);
            path = new StringBuilder();

            for ( int i = 0; i < pathSplit.length - 1; i++ )
            {
                path.append(pathSplit[i]).append(File.separator);
            }

            return new File(path.toString());
        }

        return null;
    }
}