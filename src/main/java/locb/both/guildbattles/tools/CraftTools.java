package locb.both.guildbattles.tools;

import locb.both.guildbattles.GuildBattles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;


public class CraftTools {
    public static ItemStack craftAltarStager(){
        ItemStack item = new ItemStack(Material.GOLD_BLOCK);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + "Алтарь");

        meta.addEnchant(Enchantment.OXYGEN, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey key = new NamespacedKey(GuildBattles.getInstance(), "gbId");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "AltarStager");

        item.setItemMeta(meta);

        return item;

    }


    public static boolean InventoryIsFree(PlayerInventory inventory, ItemStack item){
        if(inventory.firstEmpty() != -1) {
            return true;
        }
        else {
            int i = inventory.first(item.getType());
            ItemStack itemFirst = inventory.getItem(i);
            if ( itemFirst.getType().equals(item.getType()) && itemFirst.getItemMeta().equals(item.getItemMeta()) ) {
                if (itemFirst.getAmount() <= (64 - item.getAmount()) ) {
                    return true;
                }
            }
        }
        return false;
    }


}
