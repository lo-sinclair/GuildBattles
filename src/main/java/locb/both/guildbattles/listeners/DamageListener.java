package locb.both.guildbattles.listeners;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.model.Guild;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

    @EventHandler
    public void potionDamage(PotionSplashEvent e) {

        if ( !(e.getEntity().getShooter() instanceof Player) ) return;
        Player p1 = (Player)e.getEntity().getShooter();

        Guild g1 = pl.getDb().findGuildByMember(p1.getName());
        if(g1 == null) return;

        if(!g1.isAllowFriendlyFire()) {
            List<PotionEffectType> potionEffectTypes = new ArrayList<>();
            potionEffectTypes.add(PotionEffectType.POISON);
            potionEffectTypes.add(PotionEffectType.WEAKNESS);
            potionEffectTypes.add(PotionEffectType.SLOW);

            for (PotionEffect effect : e.getPotion().getEffects()) {
                System.out.println(effect.getType());

                if (potionEffectTypes.contains(effect.getType())) {
                    for (LivingEntity le : e.getAffectedEntities()) {
                        if (le.getType().equals(EntityType.PLAYER)) {
                            Guild g2 = pl.getDb().findGuildByMember(le.getName());
                            if (g1.getId() == g2.getId()) {
                                e.setIntensity(le, 0);
                            }
                        }
                    }
                }

            }
        }
    }


}
