package locb.both.guildbattles;

import locb.both.guildbattles.cmd.GuildCommand;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.listeners.MenuListener;
import locb.both.guildbattles.managers.CoolDownManager;
import locb.both.guildbattles.managers.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

public final class GuildBattles extends JavaPlugin implements Listener {
    private static GuildBattles instance;
    Logger log = Logger.getLogger("Minecraft");
    private SQLDatabase db;

    private static final HashMap<Player, PlayerMenuUsage> playerMenuUsageMap = new HashMap<>();
    private CoolDownManager coollDownManager;

    @Override
    public void onEnable() {
        instance = this;
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.AQUA + "[GuildBattles] " + ChatColor.GRAY + "Hi!");

        File config = new File(getDataFolder() + File.separator + "config.yml" );
        if(!config.exists()) {
            getLogger().info("Creating new config file...");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        try {
            db = new SQLDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        coollDownManager = new CoolDownManager(this);

        EconomyManager.init();

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        getCommand("guild").setExecutor(new GuildCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PlayerMenuUsage getPlayerMenuUsage(Player p) {
        PlayerMenuUsage playerMenuUsage;

        if(playerMenuUsageMap.containsKey(p)) {
            return playerMenuUsageMap.get(p);
        }
        else {
            playerMenuUsage = new PlayerMenuUsage((p));
            playerMenuUsageMap.put(p, playerMenuUsage);
            return playerMenuUsage;
        }
    }

    public CoolDownManager getCoollDownManager(){
        return coollDownManager;
    }

    public void updatePlayerMenuUsage(Player p) {
        playerMenuUsageMap.get(p).setMember();
    }

    public SQLDatabase getDb() {
        return db;
    }

    public static GuildBattles getInstance() {
        return instance;
    }
}
