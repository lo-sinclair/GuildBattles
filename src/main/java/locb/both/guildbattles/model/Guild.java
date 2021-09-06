package locb.both.guildbattles.model;

import locb.both.guildbattles.GuildBattles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Guild {
    private int id;
    private String name;
    private long createDate;
    private String leader;
    private double balance;

    private List<Member> members;

    public Guild(int id, String name, long createDate, String leader, double balance) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.leader = leader;
        this.balance = balance;
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

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
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
