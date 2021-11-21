package locb.both.guildbattles.workers;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Messages;
import locb.both.guildbattles.gui.GuiToolKit;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportWorker {

    private  Player ps;
    private final GuildBattles pl;
    private List<Player> tpPlayers = new ArrayList<>();

    int startScheduler;


    public TeleportWorker(Player ps) {
        this.pl = GuildBattles.getInstance();
        this.ps = ps;

        Guild g = pl.getDb().findGuildByMember(ps.getName());
        g.loadGuildMembers();
        List<Member> members = g.getMembers();
        for(Member m : members){
            Player p = Bukkit.getPlayer(m.getUuid());
            if(p != null) {
                tpPlayers.add(p);
            }
        }
    }


    public void start(int delay){
        Location startLoc = ps.getLocation();

        for (Player p : tpPlayers){
            p.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Глава созывает гильдию! Через 15 секунд вы будете телепортированы к месту его локации!");
        }
        ps.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Телепортация начнется через 15 секунд. Пожалуйста, не двигайтесь с места");

        startScheduler = pl.getServer().getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
            int stopwatch = 0;

            @Override
            public void run() {
                if(stopwatch > delay) {
                    teleport();
                    Bukkit.getServer().getScheduler().cancelTask(startScheduler);
                }
                if(!ps.getLocation().equals(startLoc)) {
                    ps.sendMessage(Messages.getPrefix() + "Телепортация отменяется, т.к. вы сдвинулись с места");
                    for (Player p : tpPlayers){
                        ps.sendMessage(Messages.getPrefix() + "Телепортация отменяется!");
                    }
                    Bukkit.getServer().getScheduler().cancelTask(startScheduler);
                }
                tpPlayers.removeIf(tpp -> tpp.getPlayer() == null);
                stopwatch++;
            }
        }, 0L, 20L);
    }


    public void teleport() {
        for(Player p : tpPlayers) {

//            int x = ps.getLocation().getBlockX();
//            int y = ps.getLocation().getBlockY();
            Location loc = ps.getLocation();
            loc.setY(loc.getBlockY()-1);
            ps.sendBlockChange(loc, Material.GLASS.createBlockData());
            p.teleport(ps);
        }
    }


}
