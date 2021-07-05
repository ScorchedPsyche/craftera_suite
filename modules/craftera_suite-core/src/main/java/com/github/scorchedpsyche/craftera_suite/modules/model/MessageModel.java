package com.github.scorchedpsyche.craftera_suite.modules.model;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageModel {
    public MessageModel()
    {

    }
    public MessageModel(int type, boolean pending, String message)
    {
        this.type = type;
        this.pending = pending;
        this.message = message;
        this.player_uuid = "";
        this.date_end = 0;
    }
    public MessageModel(int type, boolean pending, long date_end, String message)
    {
        this.type = type;
        this.pending = pending;
        this.message = message;
        this.player_uuid = "";
        this.date_end = date_end;
    }
    public MessageModel(int type, boolean pending, String player_uuid, long date_end, String message)
    {
        this.type = type;
        this.pending = pending;
        this.message = message;
        this.player_uuid = player_uuid;
        this.date_end = date_end;
    }

    private int id;
    private int type;
    private boolean pending;
    private String player_uuid;
    private long date_end;
    private String message;
    private String message_cached;

    public MessageModel loadFromResultSet(ResultSet rs)
    {
        try {
            this.id = rs.getInt(1);
            this.type = rs.getInt(2);
            this.pending = rs.getBoolean(3);
            this.player_uuid = rs.getString(4);
            this.date_end = rs.getLong(5);
            this.message = rs.getString(6);
            this.message_cached = rs.getString(7);

            return this;
        } catch (SQLException e)
        {
            ConsoleUtil.logError(
                    SuitePluginManager.Core.Name.full,
                    "Failed to messages from ResultSet. TRACE:");
            e.printStackTrace();
        }

        return null;
    }

    public int getId() {
        return id;
    }
    public int getType() {
        return type;
    }
    public boolean isPending() {
        return pending;
    }
    public void setPending(boolean pending) {
        this.pending = pending;
    }
    public String getPlayer_uuid() {
        return player_uuid;
    }
    public long getDate_end() {
        return date_end;
    }
    public String getMessage() {
        return message;
    }
    public String getMessage_cached()
    {
        return this.message_cached;
    }
    public MessageModel cacheMessage(String date, String time)
    {
        message_cached = message.replace("{date}", date);
        message_cached = message_cached.replace("{time}", time);

        return this;
    }
}
