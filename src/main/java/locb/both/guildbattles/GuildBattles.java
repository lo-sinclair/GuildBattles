package locb.both.guildbattles;

import locb.both.guildbattles.cmd.GuildCommand;
import locb.both.guildbattles.cmd.InviteCommand;
import locb.both.guildbattles.cooldowns.TimeCooldown;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.listeners.MenuListener;
import locb.both.guildbattles.managers.EconomyManager;
import locb.both.guildbattles.managers.GuildManader;
import locb.both.guildbattles.managers.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class GuildBattles<inviteCoolDown> extends JavaPlugin implements Listener {
    private static GuildBattles instance;
    Logger log = Logger.getLogger("Minecraft");
    private SQLDatabase db;

    //managers
    private RankManager rankManager;
    private GuildManader guildManager;

    private static final HashMap<Player, PlayerMenuUsage> playerMenuUsageMap = new HashMap<>();

    //CoolDowns
    private static TimeCooldown inviteCoolDown = new TimeCooldown();

    public static TimeCooldown getInviteCoolDown() {
        return inviteCoolDown;
    }

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

        EconomyManager.init();
        this.guildManager = new GuildManader();
        this.rankManager = new RankManager();

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        getCommand("guild").setExecutor(new GuildCommand(this));
        getCommand("guildInvite").setExecutor(new InviteCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    // playerMenuUsage
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

    public void updatePlayerMenuUsage(Player p) {
        playerMenuUsageMap.get(p).setMember();
        playerMenuUsageMap.get(p).setGuild();
    }

    public void resetPlayerMenuUsage(Player p) {
        PlayerMenuUsage playerMenuUsage = new PlayerMenuUsage((p));
        playerMenuUsageMap.put(p, playerMenuUsage);
    }

    public void updateAllPlayerMenuUsage(){
        for(Map.Entry<Player, PlayerMenuUsage> entry : playerMenuUsageMap.entrySet()) {
            updatePlayerMenuUsage(entry.getKey());
        }
    }


    public SQLDatabase getDb() {
        return db;
    }

    public GuildManader getGuildManager() {
        return guildManager;
    }
    public RankManager getRankManager() {
        return rankManager;
    }

    public static GuildBattles getInstance() {
        return instance;
    }

}
