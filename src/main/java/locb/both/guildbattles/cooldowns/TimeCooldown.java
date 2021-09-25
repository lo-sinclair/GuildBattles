package locb.both.guildbattles.cooldowns;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TimeCooldown {

    public static HashMap<UUID, Long> cooldownMap;


    public TimeCooldown(){
        cooldownMap = new HashMap<UUID, Long>();
    }


    public void addPlayerToCoolDown(Player p, int seconds) {
        long delay = System.currentTimeMillis()/1000 + seconds;
        cooldownMap.put(p.getUniqueId(), delay);
    }


    public void removePlayerInCoolDown(Player p) {
        cooldownMap.remove(p.getUniqueId());
    }


    public int getCooldownLeft(Player p) {
        if(cooldownMap.containsKey(p.getUniqueId())) {
            System.out.println(Math.round(cooldownMap.get(p.getUniqueId()) - (System.currentTimeMillis()/1000)));
            return Math.round(cooldownMap.get(p.getUniqueId()) - (System.currentTimeMillis()/1000));
        }
        return 0;
    }


    public boolean isPlayerInCoolDown(Player p) {
        if (cooldownMap.containsKey(p.getUniqueId())) {
            if( getCooldownLeft(p) > 0 ) {
                return true;
            }
        }
        return false;
    }
}
