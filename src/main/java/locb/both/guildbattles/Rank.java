package locb.both.guildbattles;

public enum Rank {
    LEADER(1),
    TRUSTED(2),
    MEMBER(3),
    OTHERS(4);

    private int level;
    Rank(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
