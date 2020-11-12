package com.github.scorchedpsyche.craftera_suite.modules.core;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.models.TradeEntryModel;
import com.github.scorchedpsyche.craftera_suite.modules.models.TradeModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
                    TradeEntryModel[] json = new Gson().fromJson(new FileReader(file), TradeEntryModel[].class);

                    if (json != null)
                    {
                        Collections.addAll(Trades.offers, json);

                        ConsoleUtils.logSuccess(SuitePluginManager.WanderingTrades.Name.full,
                                              "LOADED FILE: " + file.getName());
                    }
                } catch (FileNotFoundException ex)
                {
                    ex.printStackTrace();
                }
            }

            files = null;
        }
    }
}
