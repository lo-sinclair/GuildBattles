package locb.both.guildbattles.cmd.subs;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cmd.ISubCommand;
import locb.both.guildbattles.model.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements ISubCommand {
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
            commandSender.sendMessage(ChatColor.RED + "Не хватает аргументов!\n" +
                    ChatColor.RESET + "/guild money send [Название_гильдии] + [сумма]");
            return true;
        }

        Player ps = (Player) commandSender;

        if (args[0].equals("send")) {
            Guild g = pl.getDb().findGuildByName(args[1]);
            System.out.println(args[1]);
            if(g == null ) {
                ps.sendMessage(Messages.getPrefix() + ChatColor.RED + "Такой гильдии не существует");
                return true;
            }

            try {
                double sum = Double.parseDouble(args[2]);
                if(GuildBattles.getInstance().getGuildManager().sendMoneyToOther(ps, g, sum)) {
                    ps.sendMessage(Messages.getPrefix() + "Гильдия " + ChatColor.BLUE + g.getName() + ChatColor.RESET + " благодарит вас за перевод монет!");
                    Player pt = Bukkit.getPlayer(g.getLeader().getName());
                    if(pt != null) {
                        pt.sendMessage(Messages.getPrefix() + "Ваша гильдия получает " + sum + " монет от гильдии " +
                                ChatColor.BLUE + g.getName() + ChatColor.RESET + "!");

                    }
                }

                return true;
            } catch(NumberFormatException e){
                ps.sendMessage(Messages.getPrefix() + ChatColor.RED + "Сумма монет должна быть числом");
                return true;
            }

        }


        return false;
    }
}
