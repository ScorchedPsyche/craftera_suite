package com.github.scorchedpsyche.craftera_suite.modules.utils.natives;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class FolderUtils {
    private static File pluginsFolder;
    private static File cesRootFolder;

//    private CraftEraSuiteCore plugin;

//    public FolderUtils(CraftEraSuiteCore plugin) {
//        this.plugin = plugin;
//        pluginsFolder = getPluginsFolder();
//        cesRootFolder = getOrCreateCesRootFolder();
//    }
    public static synchronized void setup(File dataFolder)
    {
        pluginsFolder = getPluginsFolder(dataFolder);
        cesRootFolder = getOrCreateCesRootFolder();
    }

    public static File getOrCreatePluginSubfolder(String pluginName)
    {
        File pluginSubfolder = new File(cesRootFolder.toString() + File.separator + pluginName.split("-")[1]);

        if( !pluginSubfolder.exists() )
        {
            if( !pluginSubfolder.mkdirs() )
            {
                ConsoleUtils.logError(
                        "Plugin configuration folder failed to be created: check folder write permissions or try to " +
                                "create the folder manually. If everything looks OK and the issue still persists, " +
                                "report this to the developer. FOLDER PATH STRUCTURE THAT SHOULD HAVE BEEN CREATED: " +
                                ChatColor.YELLOW + pluginSubfolder.toString());
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

        String cesRootPath = pluginsFolder.toString();
        cesRootPath += File.separator + "craftera_suite";
        File cesFolder = new File( cesRootPath );

        if ( !cesFolder.exists() )
        {
            if (!cesFolder.mkdirs()) {
                ConsoleUtils.logError(
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
    private static synchronized File getPluginsFolder(File dataFolder)
    {
        if( pluginsFolder != null )
        {
            return pluginsFolder;
        }

        String path;
        try {
            path = dataFolder.getCanonicalPath();
        } catch( IOException ex ) {
            path = dataFolder.getAbsolutePath();
        }

        String pattern = Pattern.quote(File.separator);
        String[] pathSplit = path.split(pattern);
        path = "";

        for ( int i = 0; i < pathSplit.length - 1; i++ )
        {
            path += pathSplit[i] + File.separator;
        }

        return new File( path );
    }
}