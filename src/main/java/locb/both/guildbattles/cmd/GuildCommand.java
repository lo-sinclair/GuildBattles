package locb.both.guildbattles.cmd;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cmd.subs.InviteCommand;
import locb.both.guildbattles.cmd.subs.LeaveCommand;
import locb.both.guildbattles.gui.menu.GuildMenu;
import locb.both.guildbattles.gui.menu.NoGuildMenu;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;


public class GuildCommand implements CommandExecutor {
    GuildBattles pl;
    private Map<String, ISubCommand> subCommands = new HashMap<>();

    public GuildCommand(GuildBattles pl) {
        this.pl = pl;
        subCommands.put("invite", new InviteCommand());
        subCommands.put("leave", new LeaveCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only for players!");
            return true;
        }
        Player p = (Player) commandSender;

        //Без аргументов - Открыть меню
        if(args.length == 0 ) {
            if (GuildBattles.getPlayerMenuUsage(p).getMember() == null) {
                NoGuildMenu menu = new NoGuildMenu(GuildBattles.getPlayerMenuUsage(p));
                menu.open();
            }
            else {
                GuildMenu menu = new GuildMenu(GuildBattles.getPlayerMenuUsage(p));
                menu.open();
            }
            return true;
        }


        if(args.length > 0 ) {
            if( subCommands.containsKey(args[0]) ) {
                String cmd = args[0];
                args = Arrays.copyOfRange(args, 1, args.length);
                subCommands.get(cmd).execute(commandSender, args);
                return true;
            }
            commandSender.sendMessage("Неверный аргумент");
            return true;
        }


        return true;
    }
}
