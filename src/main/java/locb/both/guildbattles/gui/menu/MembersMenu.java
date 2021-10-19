package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.Rank;
import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.managers.GuildManager;
import locb.both.guildbattles.model.Guild;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MembersMenu extends Menu {
    private GuildManager manager;

    public MembersMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);

        manager = new GuildManager();

        //временное решение для хранения прав
        itemPermitions.put(Material.DIAMOND_HELMET, Rank.MEMBER);
        itemPermitions.put(Material.GOLDEN_HELMET, Rank.MEMBER);
        itemPermitions.put(Material.IRON_HELMET, Rank.MEMBER);
        itemPermitions.put(Material.WHITE_BANNER, Rank.MEMBER);
    }

    @Override
    public String getMenuName() {
        return "Список участников";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if( getOwnerRank().getLevel() <= itemPermitions.get( e.getCurrentItem().getType() ).getLevel() ) {
            switch (e.getCurrentItem().getType()) {
                case GOLDEN_HELMET:
                    new MembersListMenu(playerMenuUsage, Rank.TRUSTED).open();
                    break;

                case IRON_HELMET:
                    new MembersListMenu(playerMenuUsage, Rank.MEMBER).open();
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

        // Алмазный шлем
        ItemStack leader = new ItemStack(Material.DIAMOND_HELMET, 1);
        Guild g = playerMenuUsage.getGuild();
        meta = leader.getItemMeta();
        meta.setDisplayName("Глава");
        lore = new ArrayList<>();
        lore.add(g.getLeader().getName());
        meta.setLore(lore);
        leader.setItemMeta(meta);


        // Золотой шлем
        ItemStack trusted = new ItemStack(Material.GOLDEN_HELMET, 1);
        meta = trusted.getItemMeta();
        meta.setDisplayName("Список заместителей");
        trusted.setItemMeta(meta);


        // Железный шлем
        ItemStack soldier = new ItemStack(Material.IRON_HELMET, 1);
        meta = soldier.getItemMeta();
        meta.setDisplayName("Список рядовых");
        soldier.setItemMeta(meta);


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
        left.setItemMeta(meta);


        inventory.setItem(4, left);
        inventory.setItem(11, leader);
        inventory.setItem(13, trusted);
        inventory.setItem(15, soldier);

    }
}
