package locb.both.guildbattles.conversation;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class ConvPrefix implements ConversationPrefix {
    @Override
    public String getPrefix(ConversationContext conversationContext) {
        return ChatColor.AQUA + "[GuildBattles] ";
    }
}
