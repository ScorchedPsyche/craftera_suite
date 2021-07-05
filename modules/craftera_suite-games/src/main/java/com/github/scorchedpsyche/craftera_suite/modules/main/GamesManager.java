package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.games.raid.EnderDragonRaid;
import com.github.scorchedpsyche.craftera_suite.modules.model.GameModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.TitleSubtitleModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.ServerUtil;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GamesManager {
    public Player playerExecutingCommand;
    public String playerUUID;
    public HashMap<String, GameModel> playerGames = new HashMap<>();

    public void prepareGame(SuitePluginManager.Games.Type gameType)
    {
        // Check if player is preparing a game
        if( !playerGames.containsKey(playerUUID) )
        {
            // No game being prepared. Configure new game
            playerGames.put(playerUUID, new EnderDragonRaid(playerExecutingCommand, gameType));

            sendMessageToPlayerWithPluginPrefix( ChatColor.AQUA + playerGames.get(playerUUID).getName() + ChatColor.RESET
                    + " is now being prepared.");
        } else {
            // Warns player to run the cancel command
            sendMessageToPlayerWithPluginPrefix( new StringFormattedModel().redR("Game ")
                .aquaR( playerGames.get(playerUUID).getName() ).redR(" is already being prepared! Run command \"")
                .goldR("/ces games cancel").redR(" to cancel the game which enables you to prepare another one.").toString() );
        }
    }

    private void sendMessageToPlayerWithPluginPrefix(String message)
    {
        PlayerUtil.sendMessageWithPluginPrefix(playerExecutingCommand, SuitePluginManager.Games.Name.compact,
                message);
    }

    public void cancelGame() {
        // Check if player is preparing a game
        if( playerGames.containsKey(playerUUID) )
        {
            // Cancel game
            sendMessageToPlayerWithPluginPrefix( new StringFormattedModel()
                    .aquaR( playerGames.get(playerUUID).getName() ).redR(" was cancelled.").toString() );
            playerGames.remove(playerUUID);
        } else {
            // Nothing to cancel
            sendMessageToPlayerWithPluginPrefix( new StringFormattedModel().whiteR("There's nothing to cancel!").toString() );
        }
    }

    public void openSubscriptions() {
        // Check if player is preparing a game
        if( playerGames.containsKey(playerUUID) )
        {
            GameModel game = playerGames.get(playerUUID);

            // Attempt to change the game status
            if( game.statusOpenForSubscription() )
            {
                // Successfully changed the game's status
//                TextComponent beingText = new TextComponent( new StringFormattedModel()
//                    .aquaR( game.getName() ).add(" by ").yellowR(game.owner.getDisplayName()).add(" is now ")
//                    .greenR("open for subscriptions").add("! ").toString() );
//
//                TextComponent clickable = new TextComponent( new StringFormattedModel()
//                    .green("Click here to join").toString());
//                clickable.setClickEvent(
//                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ces games join " + game.owner.getDisplayName()));
//                clickable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,(new ComponentBuilder("Click to join!")).create()));
//
//                TextComponent endlText = new TextComponent(new StringFormattedModel()
//                    .white(" or type:").nl().gold("/ces games join ").add(game.owner.getDisplayName()).toString() );

                TextComponent message = new TextComponent( new StringFormattedModel().aquaR( game.getName() )
                        .add(" by ").yellowR(game.owner.getDisplayName()).add(" is now open for subscriptions")
                        .add("!").bold().greenR(" Click here to join").white(" or type:").nl()
                        .yellow("/ces games join ").add(game.owner.getDisplayName()).toString() );
                message.setClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ces games join " + game.owner.getDisplayName()));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,(new ComponentBuilder("Click to join!")).create()));

                ServerUtil.broadcastWithPluginPrefix(SuitePluginManager.Games.Name.compact, message );

//                ServerUtil.broadcastWithPluginPrefix( new StringFormattedModel().aquaR( game.getName() )
//                    .add(" by ").yellowR(game.owner.getDisplayName()).add(" is now ").greenR("open for subscriptions")
//                    .add("!").green(" Click here to join").white(" or type:").nl()
//                        .gold("/ces games join ").add(game.owner.getDisplayName()).toString() );
            } else {
                // Failed to change the game's status
                sendMessageToPlayerWithPluginPrefix( new StringFormattedModel()
                        .aqua( playerGames.get(playerUUID).getName() ).red(" must be on the ")
                        .yellow(SuitePluginManager.Games.Stage.Preparing.toString()).red(" stage!").toString() );
            }
        } else {
            // Nothing to cancel
            sendMessageToPlayerWithPluginPrefix( new StringFormattedModel().whiteR("There's nothing to open subscriptions for!").toString() );
        }
    }

    public void announce() {
        // Check if player is preparing a game
        if( playerGames.containsKey(playerUUID) )
        {
            GameModel game = playerGames.get(playerUUID);
             // Check if game is on the preparing stage
            if( game.getStage() == SuitePluginManager.Games.Stage.Subscribing )
            {
                // Correct stage. Announce for all players

                ServerUtil.broadcastWithPluginPrefix( SuitePluginManager.Games.Name.compact,
                        new StringFormattedModel()
                    .aquaR(game.getName() ).add(" by ").yellowR(game.owner.getDisplayName())
                    .add(" is open for subscriptions!").green(" Click here to join").white(" or type:").nl()
                    .gold("/ces games join ").add(game.owner.getDisplayName()).toString() );
                ServerUtil.playSoundForAllPlayers(game.sound);
            } else {
                // Not on the subscribing stage
                sendMessageToPlayerWithPluginPrefix( new StringFormattedModel()
                        .aqua( playerGames.get(playerUUID).getName() ).red(" must be on the ")
                        .yellow(SuitePluginManager.Games.Stage.Subscribing.toString()).red(" stage!").toString() );
            }
        } else {
            // Nothing to cancel
            sendMessageToPlayerWithPluginPrefix( new StringFormattedModel().whiteR("There's nothing to announce!").toString() );
        }
    }

    public void join(@NotNull Player targetPlayer) {
        String targetPlayerUUID = targetPlayer.getUniqueId().toString();

        // Check if player has a game that's open for subscription
        if( playerGames.containsKey(targetPlayerUUID) )
        {
            GameModel game = playerGames.get(targetPlayerUUID);
            // Check if game is on the preparing stage
            if( game.getStage() == SuitePluginManager.Games.Stage.Subscribing )
            {
                // Correct stage. Join the event
                game.addParticipant(playerExecutingCommand);

//                sendMessageToPlayerWithPluginPrefix( new StringFormattedModel().red("You have joined ").aquaR(game.getName())
//                    .add(" by ").gold( game.owner.getDisplayName() ).red(" is not open for subscription!").toString() );
            } else {
                // Not on the subscribing stage
                sendMessageToPlayerWithPluginPrefix( new StringFormattedModel()
                        .aqua( game.owner.getName() ).red("'s game is not open for subscription!").toString() );
            }
        } else {
            // Nothing to cancel
            sendMessageToPlayerWithPluginPrefix( new StringFormattedModel().yellowR(targetPlayer.getDisplayName())
                    .add(" has no games at the moment!").toString() );
        }
    }

    public void playerQuitServer(Player player) {
        for (GameModel game : this.playerGames.values()) {
            game.removeParticipant(player);
        }
    }

    public void start(int countdownInSeconds)
    {
        // Check if player has a game that's open for subscription
        if( playerGames.containsKey(playerUUID) )
        {
            GameModel game = playerGames.get(playerUUID);

            // Check if game has any participants
            if( game.getParticipants().size() > 0 )
            {
                // Check if game is on the preparing stage
                if (game.getStage() == SuitePluginManager.Games.Stage.Subscribing)
                {
                    game.startCountdown(countdownInSeconds);
                } else {
                    // Not on the subscribing stage
                    sendMessageToPlayerWithPluginPrefix( new StringFormattedModel()
                            .red("Your game is not on the subscription stage!").toString() );
                }
            } else {
                // No participants
                sendMessageToPlayerWithPluginPrefix( new StringFormattedModel()
                        .red("There are no participants on your game!").toString() );
            }
        }
    }
}
