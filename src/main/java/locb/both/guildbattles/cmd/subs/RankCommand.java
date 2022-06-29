package locb.both.guildbattles.cmd.subs;

import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cmd.ISubCommand;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
            commandSender.sendMessage(Messages.getPrefix() + ChatColor.RED + "Вы не можете использовать эту команду.");
            return false;
        }

        if (args.length < 3) {
            commandSender.sendMessage(Messages.getPrefix()+ ChatColor.RED + "Не хватает аргументов!");
            return true;
        }

        Player ps = (Player) commandSender;
        OfflinePlayer pt = Bukkit.getOfflinePlayer(UUID.fromString(args[0]));
        Guild guild = pl.getDb().findGuildByMember(ps.getName());

        if (args[2].equals("accept")) {
            if (args[1].equals("trusted")) {
                if (pl.getGuildManager().makeRank(pt, Rank.TRUSTED)) {
                    ps.sendMessage(Messages.getPrefix()+ pt.getName() + " cтановится вашим заместителем!");
                    if (pt.getPlayer() != null) {
                        ((Player)pt).sendMessage(Messages.getPrefix()+ "Вы назначены заместителем главы гильдии " + ChatColor.BLUE + "\"" + guild.getName() + "\"");
                    }
                }
                else {
                    ps.sendMessage(Messages.getPrefix() + ChatColor.RED + "Вы ошиблись, проверьте параметры");
                }
            }
            if (args[1].equals("member")) {
                if(pl.getGuildManager().makeRank(pt, Rank.MEMBER)){
                    ps.sendMessage(Messages.getPrefix() + pt.getName() + " больше не является заместителем главы гильдии.");
                }
                else {
                    ps.sendMessage(Messages.getPrefix() + ChatColor.RED + "Вы ошиблись, проверьте параметры");
                    if (pt.getPlayer() != null) {
                        pt.getPlayer().sendMessage(Messages.getPrefix() + ChatColor.BLUE + ps.getName() + ChatColor.RESET + " снял вас с должность заместителя главы гильдии " + ChatColor.BLUE + "\"" + guild.getName() + "\"");
                    }
                }
            }
            if (args[1].equals("leader")) {
                if(pl.getGuildManager().makeRank(pt, Rank.LEADER) &&
                        pl.getGuildManager().makeRank(ps.getPlayer(), Rank.MEMBER)
                ){
                    if (pl.getPrivatManager().getGuildRegion(guild) != null) {
                        pl.getPrivatManager().assignOwner(ps, pt, true);
                        pl.getPrivatManager().assignOwner(ps, (OfflinePlayer)ps, false);
                    }
                    ps.sendMessage(Messages.getPrefix() + pt.getName() + " становится новым лидером гильдии!");
                    if (pt.getPlayer() != null) {
                        pt.getPlayer().sendMessage(Messages.getPrefix() + "Вы назначены главой гильдии " + ChatColor.BLUE + "\"" + guild.getName() + "\"");
                    }
                }
                else {
                    ps.sendMessage(Messages.getPrefix() + ChatColor.RED + "Вы ошиблись, проверьте параметры");
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
