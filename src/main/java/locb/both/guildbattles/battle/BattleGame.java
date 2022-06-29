package locb.both.guildbattles.battle;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Messages;
import locb.both.guildbattles.model.Guild;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BattleGame {


    public boolean altarSet(Player p){

        Guild guild = GuildBattles.getInstance().getDb().findGuildByMember(p.getName());

        if(guild == null){
            return false;
        }

        Location loc = p.getLocation();

        RegionContainer container = GuildBattles.getInstance().getTerritoryManager().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(loc.getWorld()));

        RegionQuery query = container.createQuery();

        BlockVector3 v = BukkitAdapter.asBlockVector(loc);
        ApplicableRegionSet set = regionManager.getApplicableRegions(v);

        if (set.size() > 0 ) {
            p.sendMessage(Messages.getPrefix() + ChatColor.RED + "Эта территория уже занята!");
            return false;
        }

        return true;

    }
}
