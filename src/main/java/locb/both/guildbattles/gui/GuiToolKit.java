package locb.both.guildbattles.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiToolKit {
    public ItemStack bookButton(String title, String content){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        List<String> pages = new ArrayList<String>();

        content = ChatColor.BOLD + title + ChatColor.RESET + "\n" + content;

        int maxLenght = 220;
        Pattern p = Pattern.compile("\\G\\s*(.{1,"+maxLenght+"})(?=\\s|$)", Pattern.DOTALL);
        Matcher m = p.matcher(content);
        while (m.find()) {
            pages.add(m.group(1));
        }

        bookMeta.setPages(pages);
        bookMeta.setTitle(title);
        bookMeta.setAuthor("Admin");

        book.setItemMeta(bookMeta);
        return book;
    }
}
