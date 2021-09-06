package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.gui.PeginateMenu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class MembersListMenu extends PeginateMenu {
    public MembersListMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);
    }

    @Override
    public String getMenuName() {
        return "Список участников";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ArrayList<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());

        if(e.getCurrentItem().getType().equals((Material.PLAYER_HEAD) )) {
            PlayerMenuUsage playerMenuUsage = GuildBattles.getPlayerMenuUsage((Player) e.getWhoClicked());
            playerMenuUsage.setTarget(Bukkit.getPlayer(UUID.fromString(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(GuildBattles.getInstance(), "uuid"), PersistentDataType.STRING))));
            new KickConfirmMenu(playerMenuUsage).open();
        }
        else if (e.getCurrentItem().getType().equals((CLOSE_BTN) )) {
            e.getWhoClicked().closeInventory();
        }
        else if (e.getCurrentItem().getType().equals((LEFT_BTN)) || e.getCurrentItem().getType().equals((RIGHT_BTN)) ) {
            if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("left")) {
                if (page == 0){
                    e.getWhoClicked().sendMessage(ChatColor.GRAY + "Вы уже на первой странице");
                }
                else {
                    page = page - 1;
                    super.open();
                }
            }
            else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("right")) {
                if(!((index + 1 ) >= players.size())) {
                    page = page + 1;
                    super.open();
                }
                else{
                    e.getWhoClicked().sendMessage(ChatColor.GRAY + "Вы на последней странице");
                }
            }
        }
    }

    @Override
    public void setMenuItems() {

        addMenuNavigation();

        ArrayList<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());

        if(players != null && !players.isEmpty() ) {
            for(int i=0; i < maxItemPerPage; i++) {
                index = maxItemPerPage * page + i;
                if (index >= players.size()) break;
                if(players.get(index) != null) {

                    ItemStack playerItem = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemMeta meta = playerItem.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + players.get(index).getDisplayName());

                    meta.getPersistentDataContainer().set(new NamespacedKey(GuildBattles.getInstance(), "uuid"), PersistentDataType.STRING
                    , players.get(index).getUniqueId().toString());

                    playerItem.setItemMeta(meta);

                    inventory.addItem(playerItem);



                }
            }
        }


    }
}
