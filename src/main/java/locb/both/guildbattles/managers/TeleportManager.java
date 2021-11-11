package locb.both.guildbattles.managers;

import locb.both.guildbattles.GuildBattles;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportManager {
    private final GuildBattles pl;
    private List<Player> tpPlayers = new ArrayList<>();

    int startScheduler;


    public TeleportManager() {
        this.pl = GuildBattles.getInstance();
    }

    public void start(int delay){

        startScheduler = pl.getServer().getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
            int stopwatch = 0;
            boolean duIt;
            @Override
            public void run() {
                if(stopwatch > delay) {
                    stop(startScheduler);

                }
                stopwatch++;
            }
        }, 0L, 20L);
    }

    public void teleport() {
        pl.getServer().getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
            @Override
            public void run() {
                tpPlayers.removeIf(tpp -> tpp.getPlayer() == null);
            }
        }, 0L, 20L);
    }


    public void stop(int scheduler) {
        pl.getServer().getScheduler().cancelTask(scheduler);
    }



}
