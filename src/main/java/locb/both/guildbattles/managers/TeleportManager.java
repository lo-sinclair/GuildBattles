package locb.both.guildbattles.managers;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.gui.GuiToolKit;
import locb.both.guildbattles.workers.TeleportWorker;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    TeleportWorker getTeleportWorker(Player ps){
        return new TeleportWorker(ps);
    }

    public void callGuildAction(Player ps){
        TextComponent msg = GuiToolKit.confirmMessage(ps, "Вы уверены, что хотите созвать гильдию?",
                "/guild teleport members accept", "/guild teleport members deny");
        ps.spigot().sendMessage(msg);
    }


}
