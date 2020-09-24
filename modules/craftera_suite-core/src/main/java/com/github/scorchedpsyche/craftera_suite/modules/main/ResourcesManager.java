package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ResourcesManager
{
    public ResourcesManager()
    {
        this.consoleUtils = new ConsoleUtils();
    }

    public File pluginRootFolder;

    private ConsoleUtils consoleUtils;

    public void copyResourcesToServer(JavaPlugin plugin, ArrayList<String> files)
    {
        // Loops through resources list
        for (String file : files)
        {
            InputStream resourceToBeCopied = plugin.getResource(file);

            // Check if resource was found on .jar
            if( resourceToBeCopied != null )
            {
                // Resource found!

                // Rebuilds file path with OS file separator
                String[] pathSplit = file.split("/");
                String destinationPath = pluginRootFolder + File.separator;

                for(int i = 0; i < pathSplit.length; i++)
                {
                    destinationPath += pathSplit[i];

                    if(i != pathSplit.length)
                    {
                        destinationPath += File.separator;
                    }
                }

                File destinationFile = new File(destinationPath);

                try {
                    FileUtils.copyInputStreamToFile(resourceToBeCopied, destinationFile);
                } catch (IOException e) {
                    // Failed to create file
                    consoleUtils.logError(
                            plugin,
                            "Failed to create file. Check write permissions for folder: " + ChatColor.YELLOW + destinationFile);
                }
            } else {
                // Resource not found, must display error
                consoleUtils.logError(
                    plugin,
                    "Resource not found on .jar. Report this to the developer! RESOURCE: " + ChatColor.YELLOW + file);
            }

            // Closes the resource's InputStream
            try {
                resourceToBeCopied.close();
            } catch (IOException e) {
                // Failed to close file
                consoleUtils.logError(
                        plugin,
                        "Failed to close file. Report this to the developer! RESOURCE: " + ChatColor.YELLOW + resourceToBeCopied);
            }
        }
    }
}
