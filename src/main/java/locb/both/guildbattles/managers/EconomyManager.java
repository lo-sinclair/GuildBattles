package locb.both.guildbattles.managers;

import locb.both.guildbattles.GuildBattles;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class EconomyManager {

    private static Economy eco;

    public static void init() {
        //Bukkit.getServicesManager().register(Economy.class, eco, GuildBattles.getInstance(), ServicePriority.Highest);
        RegisteredServiceProvider<Economy>reg =  Bukkit.getServicesManager().getRegistration(Economy.class);
        if(reg != null) eco = reg.getProvider();
    }


    public static boolean takeMoney(Player p, double amount) {
        if(eco == null) return false;

        System.out.println(eco.getBalance(p));

        if(eco.getBalance(p) < amount) {
            return false;
        }
        return eco.withdrawPlayer(p, amount).transactionSuccess();
    }

    public static void giveMoney(OfflinePlayer p, double amount) {
        if(eco == null) return;
        eco.depositPlayer(p, amount);
    }

    public static double getBalance(OfflinePlayer p) {
        System.out.println(eco);
        if(eco == null) return -1;
        return eco.getBalance(p);
    }


}
