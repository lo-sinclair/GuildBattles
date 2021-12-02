package locb.both.guildbattles.cmd.subs;

import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cmd.ISubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendlyFireCommand implements ISubCommand {
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

        if (args[1].equals("accept")) {
            if(args[0].equals("on")) {
                if(pl.getGuildManager().setFriendlyFire(ps, true)) {
                    ps.sendMessage(Messages.getPrefix() + "Огонь по своим включен!");
                }
            }
            if(args[0].equals("off")) {
                if(pl.getGuildManager().setFriendlyFire(ps, false)) {
                    ps.sendMessage(Messages.getPrefix() + "Огонь по своим отключен!");
                }
            }
            return true;
        }

        if (args[1].equals("deny")) {
            return true;
        }


        return false;
    }
}
