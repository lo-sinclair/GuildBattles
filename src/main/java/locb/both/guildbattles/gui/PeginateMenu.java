package locb.both.guildbattles.gui;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class PeginateMenu extends Menu {
    protected int page = 0;

    //
    protected int maxItemPerPage = 28;

    protected int index = 0;


    public PeginateMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);
    }

    public void addMenuNavigation(){
        ItemMeta meta;
        List<Pattern> patterns;

        //ItemStack left = new ItemStack(LEFT_BTN, 1);
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

        ItemStack right = new ItemStack(Material.WHITE_BANNER, 1);
        meta = (BannerMeta)right.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Вперед");
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));
        assert meta != null;
        ((BannerMeta) meta).setPatterns(patterns);
        right.setItemMeta(meta);

        ItemStack close = new ItemStack(Material.BARRIER, 1);
        meta = close.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Закрыть");
        close.setItemMeta(meta);

        inventory.setItem(48, left);
        inventory.setItem(49, close);
        inventory.setItem(50, right);

    }
}
