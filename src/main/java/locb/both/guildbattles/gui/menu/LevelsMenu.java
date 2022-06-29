package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.tools.CraftTools;
import locb.both.guildbattles.workers.TeleportWorker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LevelsMenu extends Menu {
    public LevelsMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);

        //временное решение для хранения прав
        itemPermitions.put(Material.LIGHT_BLUE_BANNER, Rank.LEADER);
        itemPermitions.put(Material.YELLOW_BANNER, Rank.LEADER);
        itemPermitions.put(Material.GREEN_BANNER, Rank.LEADER);
        itemPermitions.put(Material.RED_BANNER, Rank.LEADER);
        itemPermitions.put(Material.WHITE_BANNER, Rank.MEMBER);
    }

    @Override
    public String getMenuName() {
        return "Уровни битвы";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if( getOwnerRank().getLevel() <= itemPermitions.get( e.getCurrentItem().getType() ).getLevel() ) {
            switch (e.getCurrentItem().getType()) {
                case LIGHT_BLUE_BANNER:
                    e.getWhoClicked().closeInventory();
                    TeleportWorker tpw = new TeleportWorker((Player) e.getWhoClicked());
                    tpw.tpToBattle("level1", 15);

                    ItemStack item = CraftTools.craftAltarStager();
                    if ( CraftTools.InventoryIsFree(e.getWhoClicked().getInventory(), item) ) {
                        e.getWhoClicked().getInventory().addItem(item);
                    }
                    else {
                        e.getWhoClicked().getWorld().dropItem(e.getWhoClicked().getLocation().add(0,1,0), item);
                    }
                    break;

                case YELLOW_BANNER:
                    e.getWhoClicked().closeInventory();
                    break;

                case GREEN_BANNER:
                    e.getWhoClicked().closeInventory();
                    break;

                case RED_BANNER:
                    e.getWhoClicked().closeInventory();
                    break;

                case WHITE_BANNER:
                    new GuildMenu(playerMenuUsage).open();
                    break;
            }
        }
        else {
            e.getWhoClicked().sendMessage(Messages.getPrefix() + ChatColor.RED + "У вас недостаточно высокий ранг, чтобы использовать эту команду!");
        }
    }

    @Override
    public void setMenuItems() {
        ItemMeta meta;
        ArrayList<String> lore;

        // Голубой флаг
        ItemStack level1 = new ItemStack(Material.LIGHT_BLUE_BANNER, 1);
        meta = level1.getItemMeta();
        meta.setDisplayName("Уровень 1");
        lore = new ArrayList<>();
        lore.add("Стоимость 5");
        meta.setLore(lore);
        level1.setItemMeta(meta);


        // Желтый флаг
        ItemStack level2 = new ItemStack(Material.YELLOW_BANNER, 1);
        meta = level2.getItemMeta();
        meta.setDisplayName("Уровень  2");
        lore = new ArrayList<>();
        lore.add("Стоимость 10");
        meta.setLore(lore);
        level2.setItemMeta(meta);


        // Зеленый флаг
        ItemStack level3 = new ItemStack(Material.GREEN_BANNER, 1);
        meta = level3.getItemMeta();
        meta.setDisplayName("Уровень 3");
        lore = new ArrayList<>();
        lore.add("Стоимость 15");
        meta.setLore(lore);
        level3.setItemMeta(meta);


        // Красный флаг
        ItemStack level4 = new ItemStack(Material.RED_BANNER, 1);
        meta = level4.getItemMeta();
        meta.setDisplayName("Уровень 4");
        lore = new ArrayList<>();
        lore.add("Стоимость 20");
        meta.setLore(lore);
        level4.setItemMeta(meta);


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
        inventory.setItem(10, level1);
        inventory.setItem(12, level2);
        inventory.setItem(14, level3);
        inventory.setItem(16, level4);

    }
}
