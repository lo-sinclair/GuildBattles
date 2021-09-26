package locb.both.guildbattles.cmd;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.cooldowns.TimeCooldown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCommand implements CommandExecutor {
    GuildBattles pl;

    public InviteCommand(GuildBattles pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only for players!");
            return true;
        }

        // Права на команду
        if( ! pl.getRankManager().playerHasRank((Player)commandSender, Rank.OTHERS)  ) {
            commandSender.sendMessage(ChatColor.RED + "Вы не можете использовать эту команду.");
            return false;
        }

        if (args.length < 3) {
            commandSender.sendMessage(ChatColor.RED + "Не хватает аргументов!");
            return true;
        }

        Player inviteSender = Bukkit.getPlayer(args[1]);
        Player inviteTarget = Bukkit.getPlayer(args[2]);

        if (args[0].equals("accept")) {
            if(inviteSender != null ) {
                TimeCooldown coolDown = GuildBattles.getInviteCoolDown();
                if(coolDown.isPlayerInCoolDown(inviteTarget)) {
                    pl.getGuildManager().addMemberToGuild(inviteSender, inviteTarget);
                    inviteSender.sendMessage(Messages.getPrefix() + inviteTarget.getName() + ChatColor.GREEN + " принял приглашение в гильдию!");
                    inviteTarget.sendMessage(Messages.getPrefix()  +  "Вы вступили в гильдию!");
                }
                else {
                    inviteTarget.sendMessage(Messages.getPrefix()  +  ChatColor.RED + "К сожалению, время вышло.");
                }
            }
            return true;
        }

        if (args[0].equals("deny")) {
            if(inviteSender != null ) {
                inviteTarget.sendMessage(Messages.getPrefix()  +  "Вы отказлись от приглашения!");
                inviteSender.sendMessage(Messages.getPrefix() + inviteTarget.getName() + ChatColor.RED + " отказался вступить в гильдию.");
            }

            return true;
        }

        return true;
    }
}
