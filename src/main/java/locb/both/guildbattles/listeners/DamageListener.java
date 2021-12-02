package locb.both.guildbattles.listeners;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.model.Guild;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    private final GuildBattles pl;

    public DamageListener(GuildBattles pl) {
        this.pl = pl;
    }

    @EventHandler
    public void oneDamage(EntityDamageByEntityEvent e) {
        if( e.getDamager().getType().equals(EntityType.PLAYER) && e.getEntityType().equals(EntityType.PLAYER) ) {
            Player p1 = (Player) e.getDamager();
            Player p2 = (Player) e.getEntity();

            Guild g1 = pl.getDb().findGuildByMember(p1.getName());
            if(g1 == null) return;
            Guild g2 = pl.getDb().findGuildByMember(p2.getName());
            if(g2 == null) return;

            if(g1.getId() == g2.getId()) {
                if(!g1.isAllowFriendlyFire()) {
                    e.setCancelled(true);
                }
            }
        }

    }
}
