package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import org.bukkit.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ResourcesManager
{
    public void copyResourcesToServer(JavaPlugin plugin, File pluginRootFolder, ArrayList<String> files)
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
                StringBuilder destinationPath = new StringBuilder(pluginRootFolder + File.separator);

                for (String partialPath : pathSplit)
                {
                    destinationPath.append(partialPath);
                    destinationPath.append(File.separator);

//                    if(i < pathSplit.length)
//                    {
//                        destinationPath.append(File.separator);
//                    }
                }

                File destinationFile = new File(destinationPath.toString());

                try {
                    if( !destinationFile.exists() )
                    {
                        FileUtils.copyInputStreamToFile(resourceToBeCopied, destinationFile);
                        ConsoleUtils.logMessage( plugin.getName(),
                                "File copied: " + ChatColor.YELLOW + destinationFile);
                    }
//                    else {
//                        ConsoleUtils.logMessage( plugin.getName(),
//                                "File exists and won't be copied: " + ChatColor.YELLOW + destinationFile);
//                    }
                } catch (IOException e) {
                    // Failed to create file
                    ConsoleUtils.logError( plugin.getName(),
                            "Failed to create file. Check write permissions for folder: " + ChatColor.YELLOW + destinationFile);
                }

                // Closes the resource's InputStream
                try {
                    resourceToBeCopied.close();
                } catch (IOException e) {
                    // Failed to close file
                    ConsoleUtils.logError( plugin.getName(),
                                           "Failed to close file. Report this to the developer! RESOURCE: " + ChatColor.YELLOW + resourceToBeCopied);
                }
            } else {
                // Resource not found, must display error
                ConsoleUtils.logError( plugin.getName(),
                    "Resource not found on .jar. Report this to the developer! RESOURCE: " + ChatColor.YELLOW + file);
            }
        }
    }
}
