package locb.both.guildbattles.managers;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.cooldowns.InviteCooldown;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoolDownManager {

    private Map<UUID, Integer> playerCoolDownMap = new HashMap<>();

    private InviteCooldown inviteCooldown;



    public CoolDownManager(GuildBattles pl) {
        inviteCooldown = new InviteCooldown();
        /*new BukkitRunnable() {

            @Override
            public void run() {
                for(UUID uuid : playerCoolDownMap.keySet()){
                    if(playerCoolDownMap.get(uuid) == 1){
                        playerCoolDownMap.remove(uuid);
                        continue;
                    }
                    playerCoolDownMap.put(uuid, playerCoolDownMap.get(uuid)-1);
                }

            }
        }.runTaskTimer(pl, 0, 20);*/
    }
    public InviteCooldown getInviteCooldown() {
        return inviteCooldown;
    }


    public  void  addPlayerToMap(Player p, Integer time) {
        playerCoolDownMap.put(p.getUniqueId(), time);
    }

    public boolean isPlayerInCoolDown(Player p) {
        return playerCoolDownMap.containsKey(p.getUniqueId());
    }

    public Integer getTimeRemaining(Player p) {
        if(!isPlayerInCoolDown(p)) {
            return 0;
        }
        else {
            return playerCoolDownMap.get(p);
        }
    }



}
