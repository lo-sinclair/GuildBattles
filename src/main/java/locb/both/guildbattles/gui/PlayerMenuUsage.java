package locb.both.guildbattles.gui;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import org.bukkit.entity.Player;

public class PlayerMenuUsage {

    private Player owner;
    private Player target;
    private Member member;
    private Guild guild;

    public PlayerMenuUsage(Player user) {
        this.owner = user;
        this.setMember();
        this.setGuild();
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Member getMember() {
        return member;
    }

    public void setMember() {
        this.member = GuildBattles.getInstance().getDb().findMemberByName(owner.getName());
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild() {
        if (member == null ) {
            guild = null;
        }
        else {
            guild = GuildBattles.getInstance().getDb().findGuild(member.getGuildId());
            guild.loadGuildMembers();
        }
    };

}
