package locb.both.guildbattles.managers;

import com.sk89q.worldguard.bukkit.event.block.PlaceBlockEvent;
import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.model.Member;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class RankManager {
    private GuildBattles pl = GuildBattles.getInstance();

    public RankManager(){

    }

    public boolean playerHasPerms(Player p, Rank rank){
        Member member = pl.getDb().findMemberByName(p.getName());
        if( member != null ) {
            if(playerRank(p).getLevel() <= rank.getLevel()) {
                return true;
            }
        }
        return false;
    }

    public Rank playerRank(OfflinePlayer p){
        Member member = pl.getDb().findMemberByName(p.getName());
        if(member!=null) {
            if (member.getRole().equals("leader")) {
                return Rank.LEADER;
            }
            if (member.getRole().equals("trusted")) {
                return Rank.TRUSTED;
            }
            return Rank.MEMBER;
        }
        return null;
    }

    public Rank playerRank(Member member){
        if(member!=null) {
            if (member.getRole().equals("leader")) {
                return Rank.LEADER;
            }
            if (member.getRole().equals("trusted")) {
                return Rank.TRUSTED;
            }
            return Rank.MEMBER;
        }
        return null;
    }
}
