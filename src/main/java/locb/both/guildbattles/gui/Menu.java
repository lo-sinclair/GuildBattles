package locb.both.guildbattles.gui;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;

    protected PlayerMenuUsage playerMenuUsage;

    protected ItemStack FILLER_GLASS = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    public GuiToolKit guiToolKit = new GuiToolKit();


    protected Map<Material, Rank> itemPermitions = new HashMap<>();

    public Menu(PlayerMenuUsage playerMenuUsage) {
        this.playerMenuUsage = playerMenuUsage;
    }

    public Rank getOwnerRank(){
        return GuildBattles.getInstance().getRankManager().playerRank(playerMenuUsage.getOwner());
    }

    public Rank getTargetRank(){
        if (playerMenuUsage.getTarget() == null) {
            return null;
        }
        return GuildBattles.getInstance().getRankManager().playerRank(playerMenuUsage.getTarget());
    }


    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void setMenuItems();

    public void open(){
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();
        playerMenuUsage.getOwner().openInventory(inventory);
    }

    protected void drawMenuFrame(){
        drawMenuFrame(true);
    }

    protected void drawMenuFrame(boolean full){
        int cols = 9;
        int rows = getSlots()/9;
        int num = 0;


        if( full) {
            for (int i = 0; i<cols; i++){
                if(inventory.getItem(i) == null) {
                    inventory.setItem(i, FILLER_GLASS);
                }
            }

            for (int i = cols * (rows-1); i<getSlots(); i++){
                if(inventory.getItem(i) == null) {
                    inventory.setItem(i, FILLER_GLASS);
                }
            }
        }


        for(int i = 0; i <= cols * (rows-1); i+=cols) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER_GLASS);
            }
        }

        for(int i = cols-1; i <= cols * (rows-1)+cols; i+=cols) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }

    @Override
    public Inventory getInventory(){
        return inventory;
    }

    public ItemStack getFillerGlass() {
        return FILLER_GLASS;
    }

    public void setFillerGlass(Material type) {
        this.FILLER_GLASS = new ItemStack(type, 1);
    }


    protected class menuItem {

        private String name;
        private ItemStack item;
        private int slot;
        private Rank permission;


        public menuItem(String name, ItemStack item, int slot, Rank permission) {
            this.name = name;
            this.item = item;
            this.slot = slot;
            this.permission = permission;
        }

        public String getName() {
            return name;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getSlot(){
            return slot;
        }

        public Rank getPermission() {
            return permission;
        }

    }

}
