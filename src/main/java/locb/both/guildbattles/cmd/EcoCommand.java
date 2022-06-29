package locb.both.guildbattles.cmd;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.gui.menu.GuildMenu;
import locb.both.guildbattles.gui.menu.NoGuildMenu;
import locb.both.guildbattles.managers.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EcoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        System.out.println("hhhhh");

        if(args.length == 2 ) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);

            if(args[1].equals("b")) {
                double balance = EconomyManager.getBalance(p);
                commandSender.sendMessage("Баланс: " + balance);
            }

            else {
                try {
                    double sum = Double.parseDouble(args[1]);
                    EconomyManager.giveMoney(p, sum);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }
        return false;
    }
}
