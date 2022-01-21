package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.model.Guild;
import net.minecraft.network.protocol.game.PacketPlayOutOpenBook;
import net.minecraft.world.EnumHand;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BattleMenu extends Menu {
    public BattleMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);
        //временное решение для хранения прав
        itemPermitions.put(Material.BELL, Rank.LEADER);
        itemPermitions.put(Material.ENDER_PEARL, Rank.LEADER);
        itemPermitions.put(Material.WRITTEN_BOOK, Rank.LEADER);
        itemPermitions.put(Material.WHITE_BANNER, Rank.LEADER);
    }

    @Override
    public String getMenuName() {
        return "Битва гильдий";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if( getOwnerRank().getLevel() <= itemPermitions.get( e.getCurrentItem().getType() ).getLevel() ) {
            switch (e.getCurrentItem().getType()) {
                case BELL:
                    new LevelsMenu(playerMenuUsage).open();
                    break;

                case ENDER_PEARL:
                    e.getWhoClicked().closeInventory();
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

        // Колокол
        ItemStack battle = new ItemStack(Material.BELL, 1);
        Guild g = playerMenuUsage.getGuild();
        meta = battle.getItemMeta();
        meta.setDisplayName("Участвовать в битве");
        lore = new ArrayList<>();
        lore.add("После нажатия открывается меню с уровнями поле боя");
        meta.setLore(lore);
        battle.setItemMeta(meta);


        // Жемчуг эндера
        ItemStack altarTp = new ItemStack(Material.ENDER_PEARL, 1);
        meta = altarTp.getItemMeta();
        meta.setDisplayName("Телепортироваться на алтарь");
        altarTp.setItemMeta(meta);


        //Книга с пером
        ItemStack info = guiToolKit.bookButton("Информация",
                "После того, как вы нажмёте на кнопку \"участие\" и выберете уровень поле боя, вас автоматически перекидывает на поля боя, Вам нужно будет поставить алтарь, после чего вы уже будете считаться участником битвы. \n" +
                        "Алтарь может установить только глава, а заместитель может принимать участие в застройке до начала битвы. После установки алтаря гильдия может строить для защиты алтаря во время битвы. Установить алтарь можно только с 00:00 до 19:00 по МСК каждый день.\n" +
                        "После установки алтаря, его невозможно переместить или отменить.\n" +
                        "В 20:00 по МСК начинается битва. Во время битвы телепортироваться могут все. Телепорт работает 10 минут после начала битвы.\n" +
                        "Подробнее можно узнать в нашем дискорд канале, в чате #guild-info.");



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
        inventory.setItem(11, battle);
        inventory.setItem(13, altarTp);
        inventory.setItem(15, info);

    }
}
