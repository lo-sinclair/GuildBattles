package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.Messages;
import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.managers.GuildManader;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GuildMenu extends Menu {
    private GuildManader manader;

    public GuildMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);
        manader = new GuildManader();
    }

    @Override
    public String getMenuName() {
        return "Меню гильдии";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }

    @Override
    public void setMenuItems() {
        ItemMeta meta;
        ArrayList<String> lore;

        // Бирюзовый флаг
        ItemStack guild = new ItemStack(Material.CYAN_BANNER, 1);

        Guild g = playerMenuUsage.getGuild();
        Member m = playerMenuUsage.getMember();

        Date dateObj = new Date(g.getCreateDate());
        SimpleDateFormat dateformat = new SimpleDateFormat("d.MM.Y");
        StringBuilder dataStr = new StringBuilder( dateformat.format( dateObj ));

        meta = guild.getItemMeta();
        meta.setDisplayName(g.getName());
        lore = new ArrayList<>();
        lore.add("Дата создания: " + dataStr);
        lore.add("Участников онлайн: " + g.onlineMembersCount() + "/" + g.membersCount());
        lore.add("Глава: " + m.getName());
        lore.add("Статус битвы гильдий: не участвует");

        meta.setLore(lore);
        guild.setItemMeta(meta);

        // Книга
        ItemStack members_list = new ItemStack(Material.BOOK, 1);
        meta = members_list.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.members_list.title"));
        members_list.setItemMeta(meta);


        // Маяк
        ItemStack call = new ItemStack(Material.BEACON, 1);
        meta = call.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.call.title"));
        lore = new ArrayList<>();
        lore.add(Messages.getNotice("messages.menu.guild.call.description"));
        meta.setLore(lore);
        call.setItemMeta(meta);


        // Сундук
        ItemStack storage = new ItemStack(Material.CHEST, 1);
        meta = storage.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.storage.title"));
        lore = new ArrayList<>();
        lore.add(Messages.getNotice("messages.menu.guild.storage.description1"));
        meta.setLore(lore);
        storage.setItemMeta(meta);


        // Кровать
        ItemStack house = new ItemStack(Material.CHEST, 1);
        meta = house.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.house.title"));
        lore = new ArrayList<>();
        lore.add(Messages.getNotice("messages.menu.guild.house.description1"));
        meta.setLore(lore);
        house.setItemMeta(meta);


        // Железный меч
        ItemStack battle = new ItemStack(Material.IRON_SWORD, 1);
        meta = house.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.battle.title"));
        battle.setItemMeta(meta);


        // Деревянный топор
        ItemStack privat = new ItemStack(Material.IRON_SWORD, 1);
        meta = privat.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.private.title"));
        battle.setItemMeta(meta);


        // Золотой слиток
        ItemStack treasury = new ItemStack(Material.CHEST, 1);
        meta = treasury.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.treasury.title"));
        lore = new ArrayList<>();
        lore.add(Messages.getNotice("messages.menu.guild.treasury.description"));
        meta.setLore(lore);
        treasury.setItemMeta(meta);


        // Лаймовый краситель
        ItemStack invite = new ItemStack(Material.LIME_DYE, 1);
        meta = invite.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.invite.title"));
        invite.setItemMeta(meta);


        // Звезда незера
        ItemStack store = new ItemStack(Material.NETHER_STAR, 1);
        meta = store.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.store.title"));
        store.setItemMeta(meta);


        // Алмаз
        ItemStack alliances = new ItemStack(Material.DIAMOND, 1);
        meta = alliances.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.alliances.title"));
        lore = new ArrayList<>();
        lore.add(Messages.getNotice("messages.menu.guild.alliances.description"));
        meta.setLore(lore);
        alliances.setItemMeta(meta);


        //Стрела силы
        ItemStack friendlyFire = new ItemStack(Material.TIPPED_ARROW, 1);
        PotionMeta potionMeta = (PotionMeta) friendlyFire.getItemMeta();
        potionMeta.setDisplayName(Messages.getNotice("messages.menu.friendlyFire.store.title"));
        potionMeta.setBasePotionData(new PotionData(PotionType.STRENGTH));
        friendlyFire.setItemMeta(potionMeta);


        // Барьер
        ItemStack leave = new ItemStack(Material.BARRIER, 1);
        meta = leave.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.leave.title"));
        leave.setItemMeta(meta);



        inventory.setItem(1, guild);
        inventory.setItem(3, members_list);
        inventory.setItem(5, call);
        
        inventory.setItem(7, storage);
        inventory.setItem(19, house);

        FILLER_GLASS = FILLER_GLASS = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        drawMenuFrame(false);
    }
}
