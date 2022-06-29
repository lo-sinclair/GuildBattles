package locb.both.guildbattles.listeners;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.tools.CraftTools;
import locb.both.guildbattles.tools.CreateTools;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlockListener implements Listener {
    private final GuildBattles pl;

    public BlockListener(GuildBattles pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onPlaceAltar(BlockPlaceEvent e) {
        Block block = e.getBlockPlaced();
        ItemStack item = e.getItemInHand();


        if(item.getItemMeta().equals(CraftTools.craftAltarStager().getItemMeta())) {
            e.setCancelled(true);
            e.getPlayer().getInventory().remove(item);

            LivingEntity altar = (LivingEntity) e.getPlayer().getWorld().spawnEntity(block.getLocation(), EntityType.SHEEP);
            altar.setCustomName(ChatColor.GOLD + "Священная овца");
            altar.setCustomNameVisible(true);
            altar.setMaxHealth(100);
            altar.setHealth(100);
            altar.setAI(false);



        }


    }


}
