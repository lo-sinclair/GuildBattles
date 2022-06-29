package locb.both.guildbattles.workers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Messages;
import locb.both.guildbattles.gui.GuiToolKit;
import locb.both.guildbattles.managers.TerritoryManager;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class TeleportWorker {

    private  Player ps;
    private final GuildBattles pl;
    private List<Player> tpPlayers = new ArrayList<>();

    BukkitTask startScheduler;


    public TeleportWorker(Player ps) {
        this.pl = GuildBattles.getInstance();
        this.ps = ps;
    }


    public void tpGuildtoLeader(int delay){
        Guild guild = pl.getDb().findGuildByMember(ps.getName());
        if(!pl.getGuildManager().takeGuildMoney(guild, 10.0)) {
            ps.sendMessage(Messages.getPrefix() + ChatColor.RED + "На счету гильдии недостаточно монет!");
            return;
        }

        Location startLoc = ps.getLocation();

        Guild g = pl.getDb().findGuildByMember(ps.getName());
        g.loadGuildMembers();
        List<Member> members = g.getMembers();
        for(Member m : members){
            Player p = Bukkit.getPlayer(m.getUuid());
            if(p != null) {
                tpPlayers.add(p);
            }
        }

        for (Player p : tpPlayers){
            p.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Глава созывает гильдию! Через " + delay + " сек. вы будете телепортированы к месту его локации!");
        }
        ps.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Телепортация начнется через " + delay  + " сек. Пожалуйста, не двигайтесь с места");

        startScheduler = pl.getServer().getScheduler().runTaskTimer(pl, new Runnable() {
            int stopwatch = 0;

            @Override
            public void run() {
                if(stopwatch > delay) {
                    teleport(startLoc);
                    startScheduler.cancel();
                }
                if(!ps.getLocation().equals(startLoc)) {
                    ps.sendMessage(Messages.getPrefix() + "Телепортация отменяется, т.к. вы сдвинулись с места");
                    for (Player p : tpPlayers){
                        p.sendMessage(Messages.getPrefix() + "Телепортация отменяется!");
                    }
                    startScheduler.cancel();
                }
                tpPlayers.removeIf(tpp -> tpp.getPlayer() == null);
                stopwatch++;
            }
        }, 0L, 20L);
    }


    public void tpHome(int delay) {
        Guild g = pl.getDb().findGuildByMember(ps.getName());
        if(g == null) return;

        if (g.getHome().isEmpty()) return;

        tpPlayers.add(ps);
        ps.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Телепортация в дом гильдии начнется через " + delay  + " сек.");
        startScheduler = pl.getServer().getScheduler().runTaskTimer(pl, new Runnable() {
            int stopwatch = 0;

            @Override
            public void run() {
                if(stopwatch > delay) {
                    teleport(TerritoryManager.stringToLocation(g.getHome()));
                    ps.sendMessage(Messages.getPrefix() + "Добро пожаловать в дом гильдии!");

                    startScheduler.cancel();
                }
                ps.spawnParticle(Particle.PORTAL, ps.getLocation(), 32, 0.6, 0.8, 0.6);
                stopwatch++;

            }
        }, 0L, 20L);

    }

    public void tpToBattle(String regionName, int delay){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(Bukkit.getWorld("world")));
        ProtectedRegion region = regionManager.getRegion(regionName);
        if(region == null) return;

        com.sk89q.worldedit.util.Location teleLoc = region.getFlag(Flags.TELE_LOC);
        if (teleLoc == null)  return;

        Guild guild = pl.getDb().findGuildByMember(ps.getName());
        if(!pl.getGuildManager().takeGuildMoney(guild, 20.0)) {
            ps.sendMessage(Messages.getPrefix() + ChatColor.RED + "На счету гильдии недостаточно монет!");
            return;
        }

        tpPlayers.add(ps);
        ps.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Телепортация на поле боя начнется через " + delay  + " сек.");
        startScheduler = pl.getServer().getScheduler().runTaskTimer(pl, new Runnable() {
            int stopwatch = 0;

            @Override
            public void run() {
                if(stopwatch > delay) {
                    teleport(BukkitAdapter.adapt(teleLoc));
                    ps.sendMessage(Messages.getPrefix() + "Вы телепортированы на поле боя!");

                    startScheduler.cancel();
                }
                ps.spawnParticle(Particle.PORTAL, ps.getLocation(), 32, 0.6, 0.8, 0.6);
                stopwatch++;

            }
        }, 0L, 20L);



    }


    public void teleport(Location loc) {
        for(Player p : tpPlayers) {
            p.teleport(loc);
        }
    }



}
