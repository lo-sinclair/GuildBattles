package locb.both.guildbattles.gui.menu;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Rank;
import locb.both.guildbattles.gui.PeginateMenu;
import locb.both.guildbattles.gui.PlayerMenuUsage;
import locb.both.guildbattles.managers.GuildManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MembersListMenu extends PeginateMenu {

    private GuildManager gManager;
    Rank rank;
    Rank role;

    public MembersListMenu(PlayerMenuUsage playerMenuUsage, Rank role) {
        super(playerMenuUsage);
        gManager = new GuildManager();
        rank = GuildBattles.getInstance().getRankManager().playerRank(playerMenuUsage.getOwner());
        this.role = role;

        //временное решение для хранения прав
        itemPermitions.put(Material.PLAYER_HEAD, Rank.TRUSTED);
        itemPermitions.put(Material.WHITE_BANNER, Rank.MEMBER);
        itemPermitions.put(Material.BARRIER, Rank.MEMBER);
    }

    @Override
    public String getMenuName() {
        String name = "";
        if(role.equals(Rank.TRUSTED))
            name = "Список заместителей";
        if(role.equals(Rank.MEMBER)) {
            name = "Список рядовых";
        }
        return name;
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        List<OfflinePlayer> players = gManager.guildPlayers(playerMenuUsage.getGuild(), role);

        if( rank.getLevel() > itemPermitions.get( e.getCurrentItem().getType() ).getLevel() ) {
            return;
        }

        if(e.getCurrentItem().getType().equals((Material.PLAYER_HEAD) )) {

            PlayerMenuUsage playerMenuUsage = GuildBattles.getPlayerMenuUsage((Player) e.getWhoClicked());

            String uuid_str = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(GuildBattles.getInstance(), "uuid"), PersistentDataType.STRING);
            OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(uuid_str));
            playerMenuUsage.setTarget(target);

            new MemberEditMenu(playerMenuUsage, role).open();

        }
        else if (e.getCurrentItem().getType().equals((Material.BARRIER) )) {
            new MembersMenu(playerMenuUsage).open();
        }
        else if (e.getCurrentItem().getType().equals((Material.WHITE_BANNER) ) || e.getCurrentItem().getType().equals((Material.WHITE_BANNER)) ) {
            if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Назад")) {
                if (page == 0){
                    e.getWhoClicked().sendMessage(ChatColor.GRAY + "Вы уже на первой странице");
                }
                else {
                    page = page - 1;
                    super.open();
                }
            }
            else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Вперед")) {
                if(!((index + 1 ) >= players.size())) {
                    page = page + 1;
                    super.open();
                }
                else{
                    e.getWhoClicked().sendMessage(ChatColor.GRAY + "Вы на последней странице");
                }
            }
        }
    }

    @Override
    public void setMenuItems() {

        addMenuNavigation();

        List<OfflinePlayer> players = gManager.guildPlayers(playerMenuUsage.getGuild(), role);
        ArrayList<String> lore;

        if(players != null && !players.isEmpty() ) {
            for(int i=0; i < maxItemPerPage; i++) {
                index = maxItemPerPage * page + i;
                if (index >= players.size()) break;
                if(players.get(index) != null) {

                    ItemStack playerItem = new ItemStack(Material.PLAYER_HEAD, 1);

                    SkullMeta meta = (SkullMeta) playerItem.getItemMeta();
                    OfflinePlayer p = players.get(index);

                    meta.setDisplayName(ChatColor.GREEN + p.getName());

                    meta.getPersistentDataContainer().set(new NamespacedKey(GuildBattles.getInstance(), "uuid"), PersistentDataType.STRING
                    , players.get(index).getUniqueId().toString());

                    meta.setOwningPlayer(p);

                    playerItem.setItemMeta(meta);

                    inventory.addItem(playerItem);

                }
            }
        }


    }
}
