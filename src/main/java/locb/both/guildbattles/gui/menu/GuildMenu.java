package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.gui.Menu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.managers.GuildManager;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import locb.both.guildbattles.workers.TeleportWorker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GuildMenu extends Menu {
    private GuildManager manager;
    //Rank rank;

    public GuildMenu(PlayerMenuUsage playerMenuUsage) {
        super(playerMenuUsage);
        manager = new GuildManager();

        //rank = GuildBattles.getInstance().getRankManager().playerRank(playerMenuUsage.getOwner());

        //временное решение для хранения прав
        itemPermitions.put(Material.CYAN_BANNER, Rank.MEMBER);
        itemPermitions.put(Material.BOOK, Rank.MEMBER);
        itemPermitions.put(Material.BEACON, Rank.LEADER);
        itemPermitions.put(Material.CHEST, Rank.MEMBER);
        itemPermitions.put(Material.RED_BED, Rank.MEMBER);
        itemPermitions.put(Material.IRON_SWORD, Rank.LEADER);
        itemPermitions.put(Material.WOODEN_AXE, Rank.LEADER);
        itemPermitions.put(Material.GOLD_INGOT, Rank.MEMBER);
        itemPermitions.put(Material.LIME_DYE, Rank.TRUSTED);
        itemPermitions.put(Material.NETHER_STAR, Rank.LEADER);
        itemPermitions.put(Material.DIAMOND, Rank.LEADER);
        itemPermitions.put(Material.TIPPED_ARROW, Rank.LEADER);
        itemPermitions.put(Material.BARRIER, Rank.MEMBER);
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
        if( getOwnerRank().getLevel() <= itemPermitions.get( e.getCurrentItem().getType() ).getLevel() ) {
            switch (e.getCurrentItem().getType()) {
                case LIME_DYE:
                    manager.inviteToGuildAction(playerMenuUsage.getOwner());
                    e.getWhoClicked().closeInventory();
                    break;

                case BEACON:
                    GuildBattles.getInstance().getTeleportManager().callGuildAction((Player)e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    break;

                case BOOK:
                    new MembersMenu(playerMenuUsage).open();
                    break;

                case WOODEN_AXE:
                    new PrivatMenu(playerMenuUsage).open();
                    break;

                case IRON_SWORD:
                    new BattleMenu(playerMenuUsage).open();
                    break;

                case RED_BED:
                    if(e.isLeftClick()) {
                        TeleportWorker tpw = new TeleportWorker((Player) e.getWhoClicked());
                        tpw.tpHome(15);
                        e.getWhoClicked().closeInventory();
                    }
                    if(e.isRightClick()) {
                        if(getOwnerRank().equals(Rank.LEADER)) {
                            if (playerMenuUsage.getGuild().getHome().isEmpty()) {
                                manager.setHomeAction((Player) e.getWhoClicked());
                            } else {
                                manager.removeHomeAction((Player) e.getWhoClicked());
                            }
                            e.getWhoClicked().closeInventory();
                        }
                        else {
                            e.getWhoClicked().sendMessage(Messages.getPrefix() + ChatColor.RED + "Только глава гильдии может устанавливать точку дома!");
                        }
                    }
                    break;

                case GOLD_INGOT:
                    if(e.isLeftClick()) {
                        manager.sendMoneyToGuild((Player) e.getWhoClicked());
                        e.getWhoClicked().closeInventory();
                    }
                    if(e.isRightClick()) {
                        if(getOwnerRank().equals(Rank.LEADER)) {
                            manager.sendMoneyToOtherAction((Player)e.getWhoClicked());
                            e.getWhoClicked().closeInventory();
                        }
                        else {
                            e.getWhoClicked().sendMessage(Messages.getPrefix() + ChatColor.RED + "У вас нет прав на эту команду!");
                        }
                    }
                    break;

                case TIPPED_ARROW:
                    System.out.println(playerMenuUsage.getGuild().isAllowFriendlyFire());
                    if(playerMenuUsage.getGuild().isAllowFriendlyFire()) {
                        manager.friendlyFireOffAction((Player) e.getWhoClicked());
                    }
                    else {
                        manager.friendlyFireOnAction((Player) e.getWhoClicked());
                    }
                    e.getWhoClicked().closeInventory();
                    break;

                case BARRIER:
                    e.getWhoClicked().closeInventory();
                    manager.leaveGuildAction(playerMenuUsage.getOwner());
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
        lore.add("Глава: " + g.getLeader().getName());
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
        ItemStack house = new ItemStack(Material.RED_BED, 1);
        meta = house.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.house.title"));
        lore = new ArrayList<>();
        lore.add("ЛКМ: Телепортироваться в дом гильдии");
        if(playerMenuUsage.getGuild().getHome().isEmpty()) {
            lore.add("ПКМ: Установить точку дома гильдии");
            lore.add("     Стоимость: 20 монет ");
        }
        else {
            lore.add("ПКМ: Удалить точку дома гильдии");
        }
        meta.setLore(lore);
        house.setItemMeta(meta);


        // Железный меч
        ItemStack battle = new ItemStack(Material.IRON_SWORD, 1);
        meta = house.getItemMeta();
        lore = new ArrayList<>();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.battle.title"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        //meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        battle.setItemMeta(meta);


        // Деревянный топор
        ItemStack privat = new ItemStack(Material.WOODEN_AXE, 1);
        meta = privat.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.private.title"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        privat.setItemMeta(meta);


        // Золотой слиток
        ItemStack treasury = new ItemStack(Material.GOLD_INGOT, 1);
        meta = treasury.getItemMeta();
        meta.setDisplayName(Messages.getNotice("messages.menu.guild.treasury.title"));
        lore = new ArrayList<>();
        lore.add("Баланс: " + Double.toString(g.getBalance()) );
        lore.add("ЛКМ: Пополнить казну");

        if(getOwnerRank().equals(Rank.LEADER)) {
            lore.add("ПКМ: Перевод монет на другую гильдию");
        }

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
        potionMeta.setDisplayName(Messages.getNotice("messages.menu.guild.friendlyFire.title"));
        potionMeta.setBasePotionData(new PotionData(PotionType.STRENGTH));
        //potionMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

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
        inventory.setItem(21, battle);
        inventory.setItem(23, privat);
        inventory.setItem(25, treasury);

        inventory.setItem(37, invite);
        inventory.setItem(39, store);
        inventory.setItem(41, alliances);
        inventory.setItem(43, friendlyFire);

        inventory.setItem(49, leave);


        FILLER_GLASS = FILLER_GLASS = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        drawMenuFrame(false);
    }


}
