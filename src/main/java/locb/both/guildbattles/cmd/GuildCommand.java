package locb.both.guildbattles.cmd;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.gui.menu.GuildMenu;
import locb.both.guildbattles.gui.menu.NoGuildMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GuildCommand implements CommandExecutor {
    GuildBattles pl;

    public GuildCommand(GuildBattles pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if (GuildBattles.getPlayerMenuUsage(p).getMember() == null) {
                NoGuildMenu menu = new NoGuildMenu(GuildBattles.getPlayerMenuUsage(p));
                menu.open();
            }
            else {
                GuildMenu menu = new GuildMenu(GuildBattles.getPlayerMenuUsage(p));
                menu.open();
            }
        }

        return true;
    }
}
