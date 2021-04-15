package com.github.scorchedpsyche.craftera_suite.modules.core;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.TradeEntryModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.TradeModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;

public class TradeListManager
{
    public TradeListManager(String tradeListsFolder)
    {
        loadTradeLists(tradeListsFolder);
    }

    public TradeModel Trades = new TradeModel();

    private void loadTradeLists(String tradeListsFolder)
    {
        File[] files = new File(tradeListsFolder).listFiles();
        if( files != null )
        {
            for(File file : files)
            {
                try
                {
                    Reader reader = Files.newBufferedReader(file.toPath());

                    TradeEntryModel[] json = new Gson().fromJson(reader, TradeEntryModel[].class);

                    if (json != null)
                    {
                        Collections.addAll(Trades.offers, json);

                        ConsoleUtil.logMessage(SuitePluginManager.WanderingTrades.Name.full,
                                              "LOADED FILE: " + file.getName());
                    }

                    reader.close();
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }

            files = null;
        }
    }
}
