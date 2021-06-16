package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteSeasons;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.AchievementModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.*;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class AchievementsDatabaseApi
{
    public boolean setupAndVerifySqlTable()
    {
        // Check if Achievements table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.Achievements.achievements_TABLENAME ) )
        {
            String sql = "CREATE TABLE "
                    + DatabaseTables.Achievements.achievements_TABLENAME + "(\n"
                    + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                    + "	" + DatabaseTables.Achievements.Table.player_uuid + " TEXT NOT NULL,\n"
                    + "	" + DatabaseTables.Achievements.Table.achievement + " TEXT NOT NULL,\n"
                    + "	" + DatabaseTables.Achievements.Table.date + " INTEGER,\n"
                    + "	" + DatabaseTables.Achievements.Table.server_or_season_first + " INTEGER";

            if( SuitePluginManager.Seasons.isEnabled() )
            {
                sql += ",\n"
                    + "	" + DatabaseTables.Achievements.Table.season + " INTEGER DEFAULT 0";
            }

            sql += "\n);";

            // Doesn't exists. Create it
            if ( DatabaseManager.database.executeSql(sql) )
            {
                // Successfully created table
                ConsoleUtil.logMessage(SuitePluginManager.Achievements.Name.full,
                                        "Table successfully created: " + DatabaseTables.Achievements.achievements_TABLENAME);
                return true;
            }

            // If we got here table creation failed
            ConsoleUtil.logError( SuitePluginManager.Achievements.Name.full,
                                   "Failed to create table: " + DatabaseTables.Achievements.achievements_TABLENAME);

            return false;
        }

        // If we got here table exists
        return true;
    }

    public void addAchievementForPlayerIfNotExists(UUID playerUUID, AchievementModel achievement)
    {
        String sql = "SELECT * FROM " + DatabaseTables.Achievements.achievements_TABLENAME + " WHERE "
            + DatabaseTables.Achievements.Table.player_uuid + " = '" + playerUUID + "' AND \n"
            + DatabaseTables.Achievements.Table.achievement + " = '" + achievement.path + "'\n";

        if( SuitePluginManager.Seasons.isEnabled() && CraftEraSuiteSeasons.seasonManager.current != null )
        {
            sql += " AND \n"
                    + DatabaseTables.Achievements.Table.season + " = " + CraftEraSuiteSeasons.seasonManager.current.getId() + "\n";
        }

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            // Check if the achievement is already added
            if( DatabaseUtil.isResultSetNullOrEmpty(rs) )
            {
                // Not added. Must add it
                sql = "INSERT INTO " + DatabaseTables.Achievements.achievements_TABLENAME + " (\n"
                        + DatabaseTables.Achievements.Table.player_uuid + ",\n"
                        + DatabaseTables.Achievements.Table.achievement + ",\n"
                        + DatabaseTables.Achievements.Table.server_or_season_first + ",\n"
                        + DatabaseTables.Achievements.Table.date;

                if( SuitePluginManager.Seasons.isEnabled() && CraftEraSuiteSeasons.seasonManager.current != null )
                {
                    sql += ",\n"
                            + DatabaseTables.Achievements.Table.season + "\n";
                }

                sql += ") VALUES (\n"
                        + "'" + playerUUID + "',\n"
                        + "'" + achievement.path + "',\n"
                        + "'" + achievement.can_be_server_or_season_first + "',\n"
                        + DateUtil.Time.getUnixNow();

                if( SuitePluginManager.Seasons.isEnabled() && CraftEraSuiteSeasons.seasonManager.current != null )
                {
                    sql += ",\n"
                            + CraftEraSuiteSeasons.seasonManager.current.getId() + "\n";
                }

                sql += ");";

                // Try to add the achievement for the player
                if ( DatabaseManager.database.executeSql(sql) )
                {
                    // Achievement added successfully. Check if it's server first
                    Player player = ServerUtil.getPlayerByUUID(playerUUID);

                    if ( achievement.can_be_server_or_season_first && player != null )
                    {
                        // First set achievement on list to not be server/season first
                        achievement.can_be_server_or_season_first = false;

                        // Configure message
                        StringBuilder msg = new StringBuilder("");
                        msg.append(ChatColor.GREEN);
                        msg.append(player.getDisplayName());
                        msg.append(ChatColor.RESET);
                        if(SuitePluginManager.Seasons.isEnabled() && CraftEraSuiteSeasons.seasonManager.current != null)
                        {
                            msg.append(" season-first");
                        } else {
                            msg.append("server-first");
                        }
                        msg.append(" [");
                        msg.append(ChatColor.AQUA);
                        msg.append(achievement.title);
                        msg.append(ChatColor.RESET);
                        msg.append("]");

                        TextComponent textComponent = new TextComponent();
                        textComponent.setText(msg.toString());
                        textComponent.setHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(achievement.description))
                        );

                        // Send message
                        ServerUtil.broadcastWithPluginPrefix(SuitePluginManager.Achievements.Name.compact, textComponent );
                        ServerUtil.playSoundForAllPlayers(Sound.ENTITY_FIREWORK_ROCKET_BLAST);
                        ParticleUtil.spawnParticleAtEntity(player, Particle.FIREWORKS_SPARK, 20);
                    }
                } else {
                    // Failed to add achievement
                    ConsoleUtil.logError( SuitePluginManager.Achievements.Name.full,
                            "Failed to add achievement for player.");
                }
            }
        } catch (SQLException e) {
            ConsoleUtil.logError( SuitePluginManager.SpectatorMode.Name.full,
                    "SQLite query failed for 'fetchCurrentSeason': " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }
    }

    public boolean canAchievementBeServerOrSeasonFirst(String achievementPath)
    {
        // Check if the achievement is server/season first
        String sql = "SELECT * FROM " + DatabaseTables.Achievements.achievements_TABLENAME + " WHERE "
                + DatabaseTables.Achievements.Table.achievement + " = '" + achievementPath + "'\n";

        if( SuitePluginManager.Seasons.isEnabled() && CraftEraSuiteSeasons.seasonManager.current != null )
        {
            sql += " AND \n"
                    + DatabaseTables.Achievements.Table.season + " = " + CraftEraSuiteSeasons.seasonManager.current.getId() + "\n";
        }

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            return DatabaseUtil.isResultSetNullOrEmpty(rs);
        } catch (SQLException e) {
            ConsoleUtil.logError( SuitePluginManager.SpectatorMode.Name.full,
                    "SQLite query failed for 'fetchCurrentSeason': " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }

        return false;
    }
}
