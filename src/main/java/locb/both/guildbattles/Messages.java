package locb.both.guildbattles;

import locb.both.guildbattles.GuildBattles;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messages {
    private static GuildBattles pl = GuildBattles.getInstance();

    public static String getNotice(String path){
        String msg = pl.getConfig().getString(path);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String getNotice(String path, Player sender){
        String msg = pl.getConfig().getString(path);
        msg = msg.replaceAll("%sender", sender.getName());
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String getErrorMessage(String path) {
        //String msg = pl.getConfig().getString(path); //временно без конфига
        String msg = path;
        return ChatColor.RED + msg;
    }

    public static String getPrefix(){
        String msg = pl.getConfig().getString("messages.plugin_prefix") + " ";
        return ChatColor.translateAlternateColorCodes('&', msg) + ChatColor.RESET;
    }
}
