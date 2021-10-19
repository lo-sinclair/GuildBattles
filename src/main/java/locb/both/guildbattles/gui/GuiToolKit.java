package locb.both.guildbattles.gui;

import locb.both.guildbattles.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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


    public static TextComponent confirmMessage(Player sender, String text, String acceptCommand, String denyCommand) {

        TextComponent msg = new TextComponent(Messages.getPrefix() + text + "\n");
        TextComponent c1 = new TextComponent(ChatColor.BOLD + "" + ChatColor.GREEN + "[Да] ");
        c1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCommand));

        TextComponent c2 = new TextComponent(ChatColor.BOLD + "" + ChatColor.RED + "[Отмена] ");
        c2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, denyCommand));

        msg.addExtra(c1);
        msg.addExtra(c2);

        return msg;
    }





}
