package locb.both.guildbattles.managers;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {
    private static Economy eco;

    public static void init() {
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
}
