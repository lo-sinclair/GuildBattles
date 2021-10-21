package locb.both.guildbattles.cmd.subs;

import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cmd.ISubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements ISubCommand {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {

        // Права на команду
        if( !pl.getRankManager().playerHasPerms((Player)commandSender, Rank.TRUSTED )  ) {
            commandSender.sendMessage(ChatColor.RED + "Вы не можете использовать эту команду.");
            return false;
        }

        if (args.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Не хватает аргументов!");
            return true;
        }

        Player ps = (Player) commandSender;
        Player pt = Bukkit.getPlayer(args[0]);

        if (args[1].equals("accept")) {
            if (pl.getGuildManager().kickMember(args[0])) {
                ps.sendMessage(args[0] + " больше не состоит в вашей гильдии.");
            }
            else {
                ps.sendMessage(ChatColor.RED + "Вы ошиблись, проверьте параметры");
            }
            return true;
        }

        if (args[1].equals("deny")) {

            return true;
        }


        return false;


    }
}
