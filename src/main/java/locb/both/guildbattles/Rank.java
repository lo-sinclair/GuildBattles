package locb.both.guildbattles;

public enum Rank {
    LEADER(1, "leader"),
    TRUSTED(2, "trusted"),
    MEMBER(3, "member");

    private int level;
    private String name;

    Rank(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}
