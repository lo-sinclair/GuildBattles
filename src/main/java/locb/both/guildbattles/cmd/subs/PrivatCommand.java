package locb.both.guildbattles.cmd.subs;

import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cmd.ISubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrivatCommand implements ISubCommand {
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

        if (args.length < 3) {
            commandSender.sendMessage(ChatColor.RED + "Не хватает аргументов!");
            return true;
        }

        Player ps = (Player) commandSender;

        try {
            OfflinePlayer pt = Bukkit.getOfflinePlayer(UUID.fromString(args[0]));

            if (args[2].equals("accept")) {

                if (args[1].equals("true")) {

                    if (pl.getPrivatManager().assignMember(ps, pt, true &&
                            pl.getGuildManager().assignPrivate(pt, true))) {

                        ps.sendMessage(Messages.getPrefix() + pt.getName() + " получает доступ к привату!");
                    }
                    else {
                        ps.sendMessage(ChatColor.RED + "Вы ошиблись, проверьте параметры");
                    }
                }
                if (args[1].equals("false")) {
                    if(pl.getPrivatManager().assignMember(ps, pt, false &&
                            pl.getGuildManager().assignPrivate(pt, false)) ){
                        ps.sendMessage(Messages.getPrefix() + pt.getName() + " больше не имеет доступа к привату.");
                    }
                    else {
                        ps.sendMessage(ChatColor.RED + "Вы ошиблись, проверьте параметры");
                    }
                }

                return true;
            }

            if (args[3].equals("deny")) {

                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }




        return false;
    }
}
