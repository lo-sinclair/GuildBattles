package locb.both.guildbattles.cooldowns;

import org.bukkit.entity.Player;

public class InviteCooldown extends Colldown {
    int cooldownTime = 120;

    @Override
    public void addPlayerToCoolDown(Player p) {
        coldownMap.put(p.getUniqueId(), System.currentTimeMillis());
    }

    @Override
    public void removePlayerToCoolDown(Player p) {
        coldownMap.remove(p.getUniqueId());
    }

    @Override
    public boolean isPlayerInCoolDown(Player p) {
        if (coldownMap.containsKey(p.getUniqueId())) {
            long secondsLeft = ((coldownMap.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);

            if (secondsLeft > 0) {
                return true;
            }
            else return false;
        }
        return false;
    }
}
