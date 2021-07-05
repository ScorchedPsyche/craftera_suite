package com.github.scorchedpsyche.craftera_suite.modules.model;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteGames;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class GameModel
{
    public GameModel(Player owner, SuitePluginManager.Games.Type type, String name, Sound sound)
    {
        this.owner = owner;
        this.type = type;
        this.name = name;
        this.sound = sound;
    }

    private Integer id;
    private SuitePluginManager.Games.Type type;
    private String name;
    public Player owner;
    protected SuitePluginManager.Games.Stage stage = SuitePluginManager.Games.Stage.Preparing;
    private HashMap<String, Player> participants = new HashMap<>();
    public Sound sound;

    public String getName() {
        return name;
    }
    public SuitePluginManager.Games.Stage getStage() {
        return stage;
    }
    public boolean statusOpenForSubscription()
    {
        this.stage = SuitePluginManager.Games.Stage.Subscribing;

        return true;
    }
    public void addParticipant(@NotNull Player player)
    {
        // Check if player is not already participating
        if( !participants.containsKey(player.getUniqueId().toString()) )
        {
            // Not participating. Join in
            participants.put(player.getUniqueId().toString(), player);
            sendMessageToParticipants(new StringFormattedModel()
                    .yellowR(player.getDisplayName()).add(" has ").greenR("joined").add(" the game ").aquaR(this.name).add(" by ")
                    .yellowR(owner.getDisplayName()).add(" for a total of ").greenR(String.valueOf(participants.size()))
                    .add(" participants!").toString());
        } else {
            // Player already participating on this event
            PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Games.Name.compact,
                new StringFormattedModel().add("You are already participating in ").aquaR(this.name)
                .add(" by ").yellowR(owner.getDisplayName()).add("!").toString());
        }
    }
    public HashMap<String, Player> getParticipants()
    {
        return this.participants;
    }
    public void sendMessageToParticipants(String msg)
    {
        for (Player participant : this.participants.values()) {
            PlayerUtil.sendMessageWithPluginPrefix(participant, SuitePluginManager.Games.Name.compact, msg);
        }
    }
    public void sendTitleAndSubtitleToParticipants(TitleSubtitleModel titleSubtitle)
    {
        for (Player participant : this.participants.values()) {
            CraftEraSuiteCore.playerManagerList.get(participant.getUniqueId().toString())
                    .titleSubtitleManager.titleSubtitleModel = titleSubtitle;
        }
    }

    public void removeParticipant(Player player)
    {
        if( this.participants.remove(player) != null )
        {
            sendMessageToParticipants(new StringFormattedModel()
                    .yellowR(player.getDisplayName()).add(" has ").redR("left").add(" the game ").aquaR(this.name).add(" by ")
                    .yellowR(owner.getDisplayName()).add(" for a total of ").red(String.valueOf(participants.size()))
                    .add(" participants!").toString());
        }
    }

    public abstract void start();
    public void startCountdown(int countdownInSeconds)
    {
        if( countdownInSeconds > 0)
        {
            TitleSubtitleModel titleSubtitle = new TitleSubtitleModel(2, 18, 2);
            titleSubtitle.title.bold().redR(String.valueOf(countdownInSeconds));
            titleSubtitle.subtitle.aquaR(name);

            sendTitleAndSubtitleToParticipants(titleSubtitle);
            Bukkit.getScheduler().scheduleSyncDelayedTask(CraftEraSuiteGames.getPlugin(CraftEraSuiteGames.class),
                    new Runnable() {
                        @Override
                        public void run() {
                            startCountdown(countdownInSeconds - 1);
                        }
                    }, 20L); //20 Tick (1 Second) delay before run() is called
        } else {
            stage = SuitePluginManager.Games.Stage.Running;
            start();
        }
//        if( countdownInSeconds <= 0)
//        {
//            this.start();
//        } else {
//            TitleSubtitleModel titleSubtitle = new TitleSubtitleModel(2, 18, 2);
//            titleSubtitle.title.bold().redR(String.valueOf(countdownInSeconds));
//            titleSubtitle.subtitle.aquaR(name);
//
//            CraftEraSuiteCore.playerManagerList.get(owner.getUniqueId().toString())
//                .titleSubtitleManager.titleSubtitleModel = titleSubtitle;
//            Bukkit.getScheduler().scheduleSyncDelayedTask(CraftEraSuiteGames.getPlugin(CraftEraSuiteGames.class),
//                    new Runnable() {
//                @Override
//                public void run() {
//                    startCountdown(countdownInSeconds - 1);
//                }
//            }, 20L); //20 Tick (1 Second) delay before run() is called
//        }
    }

    public SuitePluginManager.Games.Type getType() {
        return type;
    }

    public void finish()
    {
        TitleSubtitleModel titleSubtitle = new TitleSubtitleModel(10, 100, 10);
        titleSubtitle.title.bold().green("Finished!");
        titleSubtitle.subtitle.aqua(name);

        sendTitleAndSubtitleToParticipants(titleSubtitle);

        StringFormattedModel message = new StringFormattedModel().add("Game ").aquaR(name).add(" finished with ")
            .greenR(String.valueOf(participants.size())).add(" participants:").nl();

        int totalAdded = 0;

        for(Player participant : participants.values())
        {
            totalAdded++;
            message.yellowR(participant.getDisplayName());
            if( totalAdded == participants.size() )
            {
                message.add(".");
            } else {
                message.add(", ");
            }
        }

        sendMessageToParticipants(message.toString());

        stage = SuitePluginManager.Games.Stage.Finished;
    }
}
