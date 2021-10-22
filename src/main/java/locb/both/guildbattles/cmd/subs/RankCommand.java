package locb.both.guildbattles.cmd.subs;

import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cmd.ISubCommand;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
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

        if (args.length < 3) {
            commandSender.sendMessage(ChatColor.RED + "Не хватает аргументов!");
            return true;
        }

        Player ps = (Player) commandSender;
        Player pt = Bukkit.getPlayer(args[0]);
        Guild guild = pl.getDb().findGuildByMember(ps.getName());

        if (args[2].equals("accept")) {
            if (args[1].equals("trusted")) {
                if (pl.getGuildManager().makeRank(args[0], Rank.TRUSTED)) {
                    ps.sendMessage(args[0] + " cтановится вашим заместителем!");
                    if (pt != null) {
                        pt.sendMessage("Вы назначены заместителем главы гильдии " + ChatColor.BLUE + "\"" + guild.getName() + "\"");
                    }
                }
                else {
                    ps.sendMessage(ChatColor.RED + "Вы ошиблись, проверьте параметры");
                }
            }
            if (args[1].equals("member")) {
                if(pl.getGuildManager().makeRank(args[0], Rank.MEMBER)){
                    ps.sendMessage(args[0] + " больше не является заместителем главы гильдии.");
                }
                else {
                    ps.sendMessage(ChatColor.RED + "Вы ошиблись, проверьте параметры");
                    if (pt != null) {
                        pt.sendMessage(ChatColor.BLUE + ps.getName() + ChatColor.RESET + " снял вас с должно заместителя главы гильдии " + ChatColor.BLUE + "\"" + guild.getName() + "\"");
                    }
                }
            }
            if (args[1].equals("leader")) {
                if(pl.getGuildManager().makeRank(args[0], Rank.LEADER) &&
                        pl.getGuildManager().makeRank(ps.getName(), Rank.MEMBER)
                ){
                    ps.sendMessage(args[0] + " становится новым лидером гильдии!");
                    if (pt != null) {
                        pt.sendMessage("Вы назначены главой гильдии " + ChatColor.BLUE + "\"" + guild.getName() + "\"");
                    }
                }
                else {
                    ps.sendMessage(ChatColor.RED + "Вы ошиблись, проверьте параметры");
                }
            }

            return true;
        }

        if (args[2].equals("deny")) {

            return true;
        }
        return false;
    }
}
