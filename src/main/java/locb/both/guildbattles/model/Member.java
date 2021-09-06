package locb.both.guildbattles.model;

public class Member {
    private int id;
    private String name;
    private int guildId;
    private long joinDate;
    private String role;
    private int rivalKills;
    private int friendlyFire;
    private int deaths;
    private double deposit;

    public Member(int id, String name, int guildId, long joinDate, String role, int rivalKills, int friendlyFire, int deaths, double deposit) {
        setId(id);
        setName(name);
        setGuild_id(guildId);
        setRole(role);
        setJoinDate(joinDate);
        setRivalKills(rivalKills);
        setFriendlyFire(friendlyFire);
        setDeaths(deaths);
        setDeposit(deposit);
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long join_date) {
        this.joinDate = join_date;
    }

    public int getRivalKills() {
        return rivalKills;
    }

    public void setRivalKills(int rivalKills) {
        this.rivalKills = rivalKills;
    }

    public int getFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(int friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getGuildId() {
        return guildId;
    }

    public void setGuild_id(int guild_id) {
        this.guildId = guild_id;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
}
