package locb.both.guildbattles.cooldowns;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Colldown {

    protected Map<UUID, Long> coldownMap = new HashMap<>();

    int cooldownTime = 30;

    public abstract  void addPlayerToCoolDown(Player p);

    public abstract  void removePlayerToCoolDown(Player p);

    public abstract  boolean isPlayerInCoolDown(Player p);

}
