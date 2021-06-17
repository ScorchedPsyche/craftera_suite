package craftera_suite.craftera_suite_statistics.model;

import com.github.scorchedpsyche.craftera_suite.modules.util.DateUtil;
import org.bukkit.entity.Player;

public class PlayerLoginModel {
    private Player player;
    public Integer id_from_DB;
    public long login_time_start;
    public long login_time_end;
    public long login_time_total;

    public PlayerLoginModel(Player player) {
        this.player = player;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public PlayerLoginModel timerStart()
    {
        if( id_from_DB != null )
        {
            id_from_DB = null;
            login_time_end = 0;
            login_time_total = 0;
        }

        login_time_start = DateUtil.Time.getUnixNow();

        return this;
    }

    public PlayerLoginModel updateLoginTimer()
    {
        login_time_end = DateUtil.Time.getUnixNow();
        login_time_total = login_time_end - login_time_start;

        return this;
    }
}
