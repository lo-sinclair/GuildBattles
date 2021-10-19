package locb.both.guildbattles.cmd.subs;

import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cmd.ISubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements ISubCommand {
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

        if (args.length < 4) {
            commandSender.sendMessage(ChatColor.RED + "Не хватает аргументов!");
            return true;
        }

        Player ps = (Player) commandSender;
        Player pt = Bukkit.getPlayer(args[1]);

        if (args[3].equals("accept")) {
            if (args[2] == "trusted") {
                if (pl.getGuildManager().makeRank(args[1], Rank.TRUSTED)) {
                    ps.sendMessage(args[1] + " cтановится вашим заместителем!");
                }
                else {
                    ps.sendMessage(ChatColor.RED + "Вы ошиблись, проверьте параметры");
                }
            }
            if (args[2] == "member") {
                if(pl.getGuildManager().makeRank(args[1], Rank.MEMBER)){
                    ps.sendMessage(args[1] + " больше не является заместителем главы гильдии.");
                }
                else {
                    ps.sendMessage(ChatColor.RED + "Вы ошиблись, проверьте параметры");
                }
            }
            if (args[2] == "leader") {
                if(pl.getGuildManager().makeRank(args[1], Rank.LEADER) &&
                        pl.getGuildManager().makeRank(ps.getName(), Rank.MEMBER)
                ){
                    ps.sendMessage(args[1] + " становится новым лидером гильдии!");

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


        return false;
    }
}
