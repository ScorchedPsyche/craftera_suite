package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.database.CoreDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.model.MessageModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DateUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServerManager {
    public ServerManager(CoreDatabaseApi coreDatabaseApi)
    {
        this.coreDatabaseApi = coreDatabaseApi;
    }

    public List<MessageModel> messages;
    private final CoreDatabaseApi coreDatabaseApi;

    public ServerManager loadAndVerifyServerMessages()
    {
        messages = coreDatabaseApi.fetchPendingServerMessages();
        this.verifyAndRemoveExpiredMessages();

        return this;
    }

    private void verifyAndRemoveExpiredMessages()
    {
        List<MessageModel> messages_found = new ArrayList<>();
        if( !messages.isEmpty() ) {
            for (MessageModel message : messages) {
                if (message.getDate_end() < DateUtil.Time.getUnixNow()) {
                    messages_found.add(message);
                }
            }

            if( !messages_found.isEmpty() )
            {
                for(MessageModel message : messages_found)
                {
                    coreDatabaseApi.markMessageAsNotPending(message.getId());
                }

                messages.removeAll(messages_found);
            }
        }
    }

    ///ces core server messages new 01/07/2021-17:30 "Raid Ender Dragon nesse sábado {date} às {time}"
    public boolean newServerMessage(String[] args) {
        // TODO: better error messaging
//        LocalDate date;
        ZonedDateTime zonedDateTime;
        long endTimeUnix = 0;
        boolean permanentMessage = true;

        // Check if it's a permanent message
        if( !args[0].equals("-") )
        {
            // It's a date. Check if it's valid
            try
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y-H:m VV", Locale.ENGLISH);
                ZoneId zoneId = ZoneId.systemDefault();
                zonedDateTime = ZonedDateTime.parse(args[0] + " " + zoneId.toString(), formatter);
                endTimeUnix = zonedDateTime.toEpochSecond();

                // Check if date is in the future
                if(endTimeUnix < DateUtil.Time.getUnixNow())
                {
                    // Date is in the past and invalid
                    return false;
                }

                permanentMessage = false;
            } catch(Exception e)
            {
                ConsoleUtil.debugMessage(e.getMessage());
                // If an exception occurs, it's not a valid date
//                return false;
            }
        }

        // Capture the message (everything after the date)
        StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i++)
        {
            message.append(args[i]);
            if(i != args.length-1)
            {
                message.append(" ");
            }
        }

        // Is it a permanent message?
        String finalStr = message.toString();
        if( !permanentMessage )
        {
            // Not a permanent message. Then trim, remove quotes and replace placeholders
            finalStr = finalStr.trim();
            finalStr = StringUtil.removeEncasingDoubleQuotes(finalStr);
            if(endTimeUnix == 0)
            {
                // Code shouldn't have gotten here
                ConsoleUtil.logError(SuitePluginManager.Core.Name.full,
                        "newServerMessage from ServerManager return epoch 0. Report this to the developer");
            }
        } else {
            // A permanent message. Check if user placed placeholders
            if( finalStr.contains("{date}") || finalStr.contains("{time}") )
            {
                // Contains placeholders. Cancel operation
                return false;
            }
        }

        MessageModel newMessage = coreDatabaseApi.newServerMessage(
                new MessageModel(
                        SuitePluginManager.Core.Messages.Type.ServerMessageToAllPlayers.ordinal(),
                        true,
                        endTimeUnix,
                        finalStr
                ).cacheMessage(args[0].split("-")[0], args[0].split("-")[1]));

        if( newMessage != null )
        {
            messages.add(newMessage);
            return true;
        }

        return false;
    }
}
