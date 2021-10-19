package locb.both.guildbattles.cmd;

import locb.both.guildbattles.GuildBattles;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ISubCommand {
    GuildBattles pl = GuildBattles.getInstance();

    String getName();

    String getDescription();

    String getSyntax();

    boolean execute(CommandSender commandSender, String[] args);

}
