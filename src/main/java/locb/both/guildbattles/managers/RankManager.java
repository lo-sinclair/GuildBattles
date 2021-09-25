package locb.both.guildbattles.managers;

import com.sk89q.worldguard.bukkit.event.block.PlaceBlockEvent;
import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.model.Member;
import org.bukkit.entity.Player;

public class RankManager {
    private GuildBattles pl = GuildBattles.getInstance();

    public RankManager(){

    }

    public boolean playerHasRank(Player p, Rank rank){
        Member member = pl.getDb().findMemberByName(p.getName());
        switch (rank) {
            case MEMBER:
                if(member != null){ return true; }
                break;
            case LEADER:
                if(member.getRole() == "leader") { return true; }
                break;
            case TRUSTED:
                if(member.getRole() == "trusted") { return true; }
                break;
        }
        return false;
    }



}
