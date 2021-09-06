package locb.both.guildbattles.managers;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.conversation.ConvPrefix;
//import locb.both.guildbattles.conversation.GuildNamePrompt;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class GuildManader {

    private final GuildBattles pl;

    public GuildManader() {
        pl = GuildBattles.getInstance();
    }

    public void createGuildAction(Player p){

        ConversationFactory cf = new ConversationFactory(pl);
        Conversation conv = cf.withFirstPrompt(new GuildNamePrompt())
                .withLocalEcho(false)
                .withTimeout(60)
                .withPrefix(new ConvPrefix())
                .buildConversation(p);
        conv.begin();
    }


    private void createNewGuild(String guild_name, Player p) {
        long ts_naw = System.currentTimeMillis();
        Guild guild = new Guild(0, guild_name, ts_naw, p.getName(), 0.0);

        int guild_id = pl.getDb().createGuild(guild);


        Member member = new Member(0, p.getName(), guild_id, ts_naw,"leader",0, 0, 0, 0.0);
        pl.getDb().createMember(member);
        pl.updatePlayerMenuUsage(p);

    }


    private class GuildNamePrompt extends StringPrompt {
        @Override
        public String getPromptText(ConversationContext context) {
            return "Введите имя гильдии";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String guild_name) {
            Player p = (Player)context.getForWhom();
            createNewGuild(guild_name, p);
            context.getForWhom().sendRawMessage("Гильдия создана!");
            //context.setSessionData("guildName", guild_name);
            return END_OF_CONVERSATION;
        }
    }

}
