package locb.both.guildbattles.model;

import locb.both.guildbattles.GuildBattles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Guild {
    private int id;
    private String name;
    private long createDate;
    private double balance;
    private boolean allowFriendlyFire;

    private List<Member> members;

    public Guild(int id, String name, long createDate, double balance, boolean allowFriendlyFire) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.balance = balance;
        this.allowFriendlyFire = allowFriendlyFire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isAllowFriendlyFire() {
        return allowFriendlyFire;
    }

    public void setAllowFriendlyFire(boolean allowFriendlyFire) {
        this.allowFriendlyFire = allowFriendlyFire;
    }


    public Member getLeader(){
        List<Member> mamList = GuildBattles.getInstance().getDb().findMembersByGuild(id, "leader");
        if (mamList != null) return mamList.get(0);
        return null;
    }

    public void loadGuildMembers(){
       members = GuildBattles.getInstance().getDb().findMembersByGuild(id);
    }

    public int membersCount() {
        return members.size();
    }
    public int onlineMembersCount() {
        int count = 0;
        for(Member member : members ) {
            Player p = Bukkit.getPlayer( member.getName());
            if(p != null) {
                count++;
            }
        }

        return count;
    }
}
