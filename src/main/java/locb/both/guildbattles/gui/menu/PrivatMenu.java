package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.Rank;
import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.managers.TerritoryManager;
import locb.both.guildbattles.model.Guild;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PrivatMenu extends Menu {

    TerritoryManager pManager;

    public PrivatMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);

        pManager = new TerritoryManager();

        //временное решение для хранения прав
        itemPermitions.put(Material.LANTERN, Rank.LEADER);
        itemPermitions.put(Material.KNOWLEDGE_BOOK, Rank.LEADER);
        itemPermitions.put(Material.SOUL_LANTERN, Rank.LEADER);
        itemPermitions.put(Material.WHITE_BANNER, Rank.LEADER);
    }

    @Override
    public String getMenuName() {
        return "Приват территории";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if( getOwnerRank().getLevel() <= itemPermitions.get( e.getCurrentItem().getType() ).getLevel() ) {
            switch (e.getCurrentItem().getType()) {
                case LANTERN:
                    pManager.setTerritoryAction((Player) e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    break;

                case SOUL_LANTERN:
                    pManager.removeTerritoryAction((Player) e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    break;

                case WHITE_BANNER:
                    new GuildMenu(playerMenuUsage).open();
                    break;
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemMeta meta;
        ArrayList<String> lore;

        // Заприватить территорию
        ItemStack privat = new ItemStack(Material.LANTERN, 1);
        Guild g = playerMenuUsage.getGuild();
        meta = privat.getItemMeta();
        meta.setDisplayName("Заприватить территорию");
        lore = new ArrayList<>();
        lore.add("Стоимость 10 монет");
        meta.setLore(lore);
        privat.setItemMeta(meta);


        // Зеленая книга
        ItemStack info = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        meta = info.getItemMeta();
        meta.setDisplayName("Информация");
        lore = new ArrayList<>();
        lore.add("Территория приватится на 100 блоков");
        lore.add("по всем 4 сторонам от игрока, вверх");
        lore.add("и вниз приватится полностью.");
        lore.add("На заприваченной территтории");
        lore.add("невозможно только ломать блоки. ");
        lore.add("Открывать двери, сундуки и т.д. можно.");
        lore.add("Если дать доступ к привату заместителю,");
        lore.add("то он может выгонять всех, кроме главы");
        lore.add("и других заместителей.");
        lore.add("Если дать доступ к привату обычному рядовому,");
        lore.add("то он может только строить или ломать.");
        lore.add("Выдавать доступ к привату можно ");
        lore.add("через список участников.");

        meta.setLore(lore);

        info.setItemMeta(meta);


        // Синий фонарь
        ItemStack removePrivate = new ItemStack(Material.SOUL_LANTERN, 1);
        meta = removePrivate.getItemMeta();
        meta.setDisplayName("Удалить приват");
        removePrivate.setItemMeta(meta);


        //Left банер
        List<Pattern> patterns;
        ItemStack left = new ItemStack(Material.WHITE_BANNER, 1);
        meta = (BannerMeta)left.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Назад");
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_MIRROR));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));
        assert meta != null;
        ((BannerMeta) meta).setPatterns(patterns);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        left.setItemMeta(meta);


        inventory.setItem(4, left);
        inventory.setItem(11, privat);
        inventory.setItem(13, info);
        inventory.setItem(15, removePrivate);

    }
}
