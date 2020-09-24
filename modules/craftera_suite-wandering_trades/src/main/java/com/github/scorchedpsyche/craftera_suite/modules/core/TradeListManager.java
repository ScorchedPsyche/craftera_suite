package com.github.scorchedpsyche.craftera_suite.modules.core;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteWanderingTrades;
import com.github.scorchedpsyche.craftera_suite.modules.models.TradeEntryModel;
import com.github.scorchedpsyche.craftera_suite.modules.models.TradeModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;

public class TradeListManager
{
    public TradeListManager(CraftEraSuiteWanderingTrades plugin)
    {
        this.plugin = plugin;
        setup();
        loadFiles();
    }

    public TradeModel Trades = new TradeModel();

    private File listsFolder;

    private CraftEraSuiteWanderingTrades plugin;

    public void loadFiles()
    {
        File[] files = listsFolder.listFiles();
        if( files != null )
        {
            Arrays.stream(files).forEach(file -> {
                try
                {
                    TradeEntryModel[] json = new Gson().fromJson(new FileReader(file), TradeEntryModel[].class);

                    if (json != null)
                    {
                        Collections.addAll(Trades.offers, json);

                        LoggerCore.Log( "LOADED FILE: " + file.getName() );
                    }
                } catch (FileNotFoundException ex)
                {
                    ex.printStackTrace();
                }
            });
        }
    }

    public void setup()
    {
        listsFolder = new File( plugin.getDataFolder(), "trade_lists" );

        // Create folder to stores lists in
        if( listsFolder.mkdirs() )
        {
            plugin.saveResource("trade_lists/heads_decoration.json", false);
            plugin.saveResource("trade_lists/heads_players.json", false);
            plugin.saveResource("trade_lists/items.json", false);
            LoggerCore.Log( "DONE: Trade Lists folder created" );
        }
    }
}
