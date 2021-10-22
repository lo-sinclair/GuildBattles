package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.managers.GuildManager;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberEditMenu extends Menu {

    Member targetMember;
    Rank role;
    private GuildManager gManager;

    public MemberEditMenu(PlayerMenuUsage playerMenuUsage, Rank role) {
        super(playerMenuUsage);

        targetMember = GuildBattles.getInstance().getDb().findMemberByName(playerMenuUsage.getTarget().getName());
        this.role = role;
        gManager = new GuildManager();

        //временное решение для хранения прав
        itemPermitions.put(Material.GOLDEN_HELMET, Rank.LEADER);
        itemPermitions.put(Material.IRON_HELMET, Rank.LEADER);
        itemPermitions.put(Material.DIAMOND_HELMET, Rank.LEADER);
        itemPermitions.put(Material.GREEN_WOOL, Rank.LEADER);
        itemPermitions.put(Material.RED_WOOL, Rank.LEADER);
        itemPermitions.put(Material.BARRIER, Rank.TRUSTED);
        itemPermitions.put(Material.WHITE_BANNER, Rank.MEMBER);
    }

    @Override
    public String getMenuName() {
        return "Редактирование участника";
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
                    gManager.makeTrustedAction(playerMenuUsage.getOwner(), playerMenuUsage.getTarget());
                    e.getWhoClicked().closeInventory();
                    break;

                case IRON_HELMET:
                    gManager.makeMemberAction(playerMenuUsage.getOwner(), playerMenuUsage.getTarget());
                    e.getWhoClicked().closeInventory();
                    break;

                case DIAMOND_HELMET:
                    gManager.giveLeaderAction(playerMenuUsage.getOwner(), playerMenuUsage.getTarget());
                    e.getWhoClicked().closeInventory();
                    break;

                case GREEN_WOOL:
                    gManager.assignPrivateAction(playerMenuUsage.getOwner(), playerMenuUsage.getTarget(), true);
                    e.getWhoClicked().closeInventory();
                    break;

                case RED_WOOL:
                    gManager.assignPrivateAction(playerMenuUsage.getOwner(), playerMenuUsage.getTarget(), false);
                    e.getWhoClicked().closeInventory();
                    break;

                case BARRIER:
                    gManager.kickMemberAction(playerMenuUsage.getOwner(), playerMenuUsage.getTarget());
                    e.getWhoClicked().closeInventory();
                    break;

                case WHITE_BANNER:
                    new MembersListMenu(playerMenuUsage, role).open();
                    break;

            }
        }
    }


    @Override
    public void setMenuItems() {
        ItemMeta meta;
        ArrayList<String> lore;

        ItemStack signRank = new ItemStack(Material.POPPY, 1);
        if(getTargetRank().equals(Rank.MEMBER)) {
            // Золотой шлем
            signRank = new ItemStack(Material.GOLDEN_HELMET, 1);
            meta = signRank.getItemMeta();
            meta.setDisplayName("Сделать заместителем");
            lore = new ArrayList<>();
            lore.add("Может пригласить игроков");
            lore.add("Может выгнать игрока");
            lore.add("Может установить алтарь и строить в битве гильдий");
            meta.setLore(lore);
            signRank.setItemMeta(meta);
        }
        if(getTargetRank().equals(Rank.TRUSTED)) {
            // Железный шлем
            signRank = new ItemStack(Material.IRON_HELMET, 1);
            meta = signRank.getItemMeta();
            meta.setDisplayName("Сделать рядовым");
            signRank.setItemMeta(meta);
        }

        ItemStack assignLeader = new ItemStack(Material.DIAMOND_HELMET, 1);
        meta = assignLeader.getItemMeta();
        meta.setDisplayName("Передать главу");
        assignLeader.setItemMeta(meta);

        ItemStack privat;
        if( !targetMember.isPrivat() ) {
            //Зеленая шерсть
            privat = new ItemStack(Material.GREEN_WOOL, 1);
            meta = privat.getItemMeta();
            meta.setDisplayName("Дать доступ к привату");
            privat.setItemMeta(meta);
        }
        else {
            //Красная шерсть
            privat = new ItemStack(Material.RED_WOOL, 1);
            meta = privat.getItemMeta();
            meta.setDisplayName("Снять доступ к привату");
            privat.setItemMeta(meta);
        }

        // Барьер
        ItemStack kick = new ItemStack(Material.BARRIER, 1);
        meta = kick.getItemMeta();
        meta.setDisplayName("Выгнать из гильдии");
        kick.setItemMeta(meta);


        //Left баннер
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
        inventory.setItem(10, signRank);
        inventory.setItem(12, assignLeader);
        inventory.setItem(14, privat);
        inventory.setItem(16, kick);
    }

}
