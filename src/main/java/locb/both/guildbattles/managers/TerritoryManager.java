package locb.both.guildbattles.managers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TerritoryManager {
    GuildBattles pl;
    private RegionContainer regionContainer;


    public TerritoryManager(){
        pl = GuildBattles.getInstance();
        regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
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

        if(!p.getLocation().getWorld().getName().equals("world")) {
            p.sendMessage(Messages.getPrefix() + ChatColor.RED + "Вы можете заприватить территорию только в обычном мире!");
            return false;
        }

        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(loc.getWorld()));

        RegionQuery query = regionContainer.createQuery();

        BlockVector3 v = BukkitAdapter.asBlockVector(loc);
        ApplicableRegionSet set = regionManager.getApplicableRegions(v);

        if (set.size() > 0 ) {
            p.sendMessage(Messages.getPrefix() + ChatColor.RED + "Эта территория уже занята!");
            return false;
        }

        if(guild.getTerritory().equals("+")) {
            if(!pl.getGuildManager().takeGuildMoney(guild, 50.0)) {
                p.sendMessage(Messages.getPrefix() + ChatColor.RED + "На счету гильдии недостаточно монет!");
                return false;
            }
        }

        String region_name = guild.getName() + "_" + this.locationToString(loc);
        BlockVector3 min = BlockVector3.at(loc.getBlockX()-15, -64, loc.getBlockZ()-15);
        BlockVector3 max = BlockVector3.at(loc.getBlockX()+15, 255, loc.getBlockZ()+15);
        ProtectedRegion region = new ProtectedCuboidRegion(region_name, min, max);

        region.getOwners().addPlayer(p.getUniqueId());
        regionManager.addRegion(region);

        guild.setTerritory(region_name);
        pl.getDb().updateGuild(guild);
        pl.updateAllPlayerMenuUsage();

        //p.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Территория теперь ваша!");


        return true;
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

    public boolean assignMember(Player ps, OfflinePlayer pt, boolean allow){
        Guild guild = pl.getDb().findGuildByMember(pt.getName());
        if(guild == null){
            return false;
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(Bukkit.getWorld("world")));
        ProtectedRegion region = regionManager.getRegion(guild.getTerritory());

        if(region == null) {
            ps.sendMessage(Messages.getPrefix() + ChatColor.RED + "Регион не обнаружен");
            return  false;
        }

        DefaultDomain members = region.getMembers();

        if(allow) {
            members.addPlayer(pt.getUniqueId());
        }
        else{
            members.removePlayer(pt.getUniqueId());
        }

        return true;
    }

    public boolean assignOwner(Player ps, OfflinePlayer pt, boolean allow){
        Guild guild = pl.getDb().findGuildByMember(pt.getName());
        if(guild == null){
            return false;
        }

        ProtectedRegion region = getGuildRegion(guild);
        if( region == null) {
            ps.sendMessage(Messages.getPrefix() + ChatColor.RED + "Регион не обнаружен");
            return  false;
        }

        DefaultDomain owners = region.getOwners();

        if(allow) {
            owners.addPlayer(pt.getUniqueId());
        }
        else{
            owners.removePlayer(pt.getUniqueId());
        }

        return true;
    }


    public ProtectedRegion getGuildRegion(Guild guild){

        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(Bukkit.getWorld("world")));
        ProtectedRegion region = regionManager.getRegion(guild.getTerritory());

        return region;
    }


    public static String locationToString(Location loc ){
        return loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    }


    public static Location stringToLocation(String strLoc) {
        String[] parts = strLoc.split("_");
        return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }


    public RegionContainer getRegionContainer() {
        return regionContainer;
    }
}
