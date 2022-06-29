package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.Messages;
import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.managers.GuildManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class NoGuildMenu extends Menu {

    private GuildManager manader;

    public NoGuildMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);
        manader = new GuildManager();
    }

    @Override
    public String getMenuName() {
        return "Меню гильдии";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        switch (e.getCurrentItem().getType()) {
            case WHITE_BANNER:
                e.getWhoClicked().closeInventory();
                manader.createGuildAction(playerMenuUsage.getOwner());
                break;

            case WRITTEN_BOOK:
                ItemStack item = e.getCurrentItem();
                Player p = playerMenuUsage.getOwner();
                int slot = p.getInventory().getHeldItemSlot();
                ItemStack old = p.getInventory().getItem(slot);
                p.getInventory().setItem(slot, item);
                p.openBook(item);
                p.getInventory().setItem(slot, old);

                break;
        }
    }

    @Override
    public void setMenuItems() {
        ItemMeta meta;
        ArrayList<String> lore;

        //Белый флаг
        ItemStack create_guild = new ItemStack(Material.WHITE_BANNER, 1);
        meta = create_guild.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + Messages.getNotice("messages.menu.noGuild.create_guild.title"));
        lore = new ArrayList<>();
        lore.add(Messages.getNotice("messages.menu.noGuild.create_guild.description"));
        meta.setLore(lore);
        create_guild.setItemMeta(meta);

        //Книга с пером
        ItemStack info = guiToolKit.bookButton(Messages.getNotice("messages.menu.noGuild.info.title"),
        Messages.getNotice("messages.menu.noGuild.info.content"));


        inventory.setItem(12, create_guild);
        inventory.setItem(14, info);

        drawMenuFrame();
    }
}
