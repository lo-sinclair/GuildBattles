package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.Messages;
import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.managers.GuildManader;
import net.minecraft.network.protocol.game.PacketPlayOutOpenBook;
import net.minecraft.world.EnumHand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class NoGuildMenu extends Menu {

    private GuildManader manader;

    public NoGuildMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);
        manader = new GuildManader();
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
                e.getWhoClicked().sendMessage("Создается гильдия");
                manader.createGuildAction(playerMenuUsage.getOwner());
                break;

            case WRITTEN_BOOK:
                ItemStack item = e.getCurrentItem();

                CraftPlayer p = (CraftPlayer)playerMenuUsage.getOwner();

                int slot = p.getInventory().getHeldItemSlot();
                ItemStack old = p.getInventory().getItem(slot);
                p.getInventory().setItem(slot, item);

                e.getWhoClicked().closeInventory();
                p.getHandle().b.sendPacket(new PacketPlayOutOpenBook(EnumHand.a));
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
