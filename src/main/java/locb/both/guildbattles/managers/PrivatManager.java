package locb.both.guildbattles.managers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Messages;
import locb.both.guildbattles.gui.GuiToolKit;
import locb.both.guildbattles.model.Guild;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PrivatManager {
    GuildBattles pl;

    public PrivatManager(){
        pl = GuildBattles.getInstance();
    }


    public void setTerritoryAction(Player p){
        TextComponent msg = GuiToolKit.confirmMessage(p, "Установить приват на эту территорию?",
                "/guild territory set accept", "/guild territory set deny");
        p.spigot().sendMessage(msg);
    }

    public void removeTerritoryAction(Player p){
        TextComponent msg = GuiToolKit.confirmMessage(p, "Вы действительноо хотите удалить территорию?",
                "/guild territory remove accept", "/guild territory remove deny");
        p.spigot().sendMessage(msg);
    }

    public boolean removeTerritory(Player p) {
        Guild guild = pl.getDb().findGuildByMember(p.getName());

        if(guild == null){
            return false;
        }

        if(guild.getTerritory().isEmpty() || guild.getTerritory().equals("+")) {
            p.sendMessage(Messages.getPrefix() + ChatColor.RED + "У вас еще нет территории.");
            return false;
        }

        Location loc = p.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(loc.getWorld()));

        String region_name = guild.getTerritory();
        System.out.println(regionManager.removeRegion(region_name));

        guild.setTerritory("+");
        pl.getDb().updateGuild(guild);
        pl.updateAllPlayerMenuUsage();

        return true;
    }

    public boolean setTerritory(Player p){
        Guild guild = pl.getDb().findGuildByMember(p.getName());

        if(guild == null){
            return false;
        }

        if( ! (guild.getTerritory().isEmpty()  || guild.getTerritory().equals("+")) ) {
            p.sendMessage(Messages.getPrefix() + ChatColor.RED + "У вас уже есть территория.");
            return false;
        }

        Location loc = p.getLocation();

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(loc.getWorld()));

        RegionQuery query = container.createQuery();

        BlockVector3 v = BukkitAdapter.asBlockVector(loc);
        ApplicableRegionSet set = regionManager.getApplicableRegions(v);

        if (set.size() > 0 ) {
            p.sendMessage(Messages.getPrefix() + ChatColor.RED + "Эта территория уже занята!");
            return false;
        }

        if(guild.getTerritory() == "+") {
            if(!EconomyManager.takeMoney(p, 50)) {
                p.sendMessage(Messages.getPrefix() + ChatColor.RED + "У вас недостаточно средств на счете!");
                return false;
            }
        }

        String region_name = guild.getName() + "_" + this.locationToString(loc);
        BlockVector3 min = BlockVector3.at(loc.getBlockX()-15, 0, loc.getBlockZ()-15);
        BlockVector3 max = BlockVector3.at(loc.getBlockX()+15, 255, loc.getBlockZ()+15);
        ProtectedRegion region = new ProtectedCuboidRegion(region_name, min, max);

        region.getOwners().addPlayer(p.getUniqueId());
        regionManager.addRegion(region);

        guild.setTerritory(region_name);
        pl.getDb().updateGuild(guild);
        pl.updateAllPlayerMenuUsage();

        //p.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Территория теперь ваша!");

        //ProtectedRegion region = regionManager.getRegion(region_name);
        return true;
        }





    public void removeRegion(Guild guild, Player p){
        if(guild.getTerritory() == null || guild.getTerritory() == "+") {
            p.sendMessage(Messages.getPrefix() + ChatColor.RED + "У вас недостаточно средств на счете!");
        }

    }


    private String locationToString(Location loc ){
        return loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    }

}
