package locb.both.guildbattles.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;

    protected PlayerMenuUsage playerMenuUsage;

    protected ItemStack FILLER_GLASS = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    public GuiToolKit guiToolKit = new GuiToolKit();

    public Menu(PlayerMenuUsage playerMenuUsage) {
        this.playerMenuUsage = playerMenuUsage;
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


        for(int i = cols; i < cols * (rows-1); i+=cols) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER_GLASS);
            }
        }

        for(int i = cols*2-1; i < cols * (rows-1)+cols; i+=cols) {
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

}
