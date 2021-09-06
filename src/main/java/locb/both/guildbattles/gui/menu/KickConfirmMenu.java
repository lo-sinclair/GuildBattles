package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class KickConfirmMenu extends Menu {
    public KickConfirmMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);
    }

    @Override
    public String getMenuName() {
        return null;
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        switch (e.getCurrentItem().getType()) {
            case EMERALD:

                Player target = playerMenuUsage.getTarget();
                e.getWhoClicked().sendMessage(target.getDisplayName() + "удален");
                break;
            case BARRIER:
                new MembersListMenu(playerMenuUsage).open();
                break;
        }
    }

    @Override
    public void setMenuItems() {
        ItemMeta meta;
        ArrayList<String> lore = new ArrayList<>();

        ItemStack yes = new ItemStack(Material.EMERALD, 1);
        meta = yes.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Да");
        lore.add(ChatColor.AQUA + "Удалить игрока из гильдии?");
        meta.setLore(lore);
        yes.setItemMeta(meta);

        ItemStack no = new ItemStack(Material.BARRIER, 1);
        meta = no.getItemMeta();
        meta.setDisplayName("Нет");
        no.setItemMeta(meta);

        inventory.setItem(3, yes);
        inventory.setItem(5, no);

    }
}
