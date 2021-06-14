package com.github.scorchedpsyche.craftera_suite.modules.model;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeasonModel {
    private Integer id;
    private Integer number;
    private String title;
    private String subtitle;
    private Integer status;
    private Integer account;
    private long date_start;
    private long date_end;
    private String minecraft_version_start;
    private String minecraft_version_end;

    @Nullable
    public SeasonModel loadDataFromResultSet(ResultSet rs)
    {
        try {
            id = rs.getInt(1);
            number = rs.getInt(2);
            title = rs.getString(3);
            subtitle = rs.getString(4);
            status = rs.getInt(5);
            account = rs.getInt(6);
            date_start = rs.getInt(7);
            date_end = rs.getInt(8);
            minecraft_version_start = rs.getString(9);
            minecraft_version_end = rs.getString(10);

            return this;
        } catch (SQLException e)
        {
            ConsoleUtil.logError(
                    SuitePluginManager.SpectatorMode.Name.full,
                    "Failed to load player data from ResultSet. TRACE:");
            e.printStackTrace();
        }

        return null;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() {
        return subtitle;
    }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getAccount() {
        return account;
    }
    public void setAccount(int account){ this.account = account; }

    public long getDate_start() {
        return date_start;
    }
    public void setDate_start(long date_start){ this.date_start = date_start; }

    public long getDate_end() {
        return date_end;
    }
    public void setDate_end(long date_end){ this.date_end = date_end; }

    public String getMinecraft_version_start() {
        return minecraft_version_start;
    }
    public void setMinecraft_version_start(String minecraft_version_start){ this.minecraft_version_start = minecraft_version_start; }

    public String getMinecraft_version_end() {
        return minecraft_version_end;
    }
    public void setMinecraft_version_end(String minecraft_version_end){ this.minecraft_version_end = minecraft_version_end; }
}
