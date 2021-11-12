package locb.both.guildbattles.cmd.subs;

import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cmd.ISubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TerritoryCommand implements ISubCommand {
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
        if( !pl.getRankManager().playerHasPerms((Player)commandSender, Rank.LEADER)  ) {
            commandSender.sendMessage(ChatColor.RED + "Вы не можете использовать эту команду.");
            return false;
        }

        if (args.length < 2) {
            commandSender.sendMessage(ChatColor.RED + "Не хватает аргументов!");
            return true;
        }

        Player ps = (Player) commandSender;

        if(args[0].equals("set")) {
            if (args[1].equals("accept")) {
                if(pl.getPrivatManager().setTerritory(ps)) {
                    ps.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Территория теперь ваша!");
                }
            }

            if (args[1].equals("deny")) {
                return true;
            }
        }

        if(args[0].equals("remove")) {
            if (args[1].equals("accept")) {
                if(pl.getPrivatManager().removeTerritory(ps)) {
                    ps.sendMessage(Messages.getPrefix() + ChatColor.GREEN + "Территория удалена!");
                }
            }

            if (args[1].equals("deny")) {
                return true;
            }
        }

        return false;
    }
}
