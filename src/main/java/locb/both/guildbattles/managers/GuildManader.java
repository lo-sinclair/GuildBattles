package locb.both.guildbattles.managers;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.conversation.ConvPrefix;
//import locb.both.guildbattles.conversation.GuildNamePrompt;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;

public class GuildManader {

    private final GuildBattles pl;

    public GuildManader() {
        pl = GuildBattles.getInstance();
    }

    public void createGuildAction(Player p){

        if(!EconomyManager.takeMoney(p, 10)) {
            p.sendMessage(ChatColor.RED + "У вас недостаточно средств на счете!");
            return;
        }

        ConversationFactory cf = new ConversationFactory(pl);
        Conversation conv = cf.withFirstPrompt(new GuildNamePrompt())
                .withLocalEcho(false)
                .withTimeout(30)
                .withPrefix(new ConvPrefix())
                .buildConversation(p);
        conv.begin();
    }


    public void inviteToGuildAction(Player p) {


        pl.getCoollDownManager().getInviteCooldown().addPlayerToCoolDown(p);

        ConversationFactory cf = new ConversationFactory(pl);
        Conversation conv = cf.withFirstPrompt(new PlayerInvitePrompt(pl))
                .withLocalEcho(false)
                .withTimeout(30)
                .withPrefix(new ConvPrefix())
                .buildConversation(p);
        conv.begin();
    }


    private void createNewGuild(String guild_name, Player p) {
        long ts_naw = System.currentTimeMillis();
        Guild guild = new Guild(0, guild_name, ts_naw, p.getName(), 0.0, false);

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

            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
            createNewGuild(guild_name, p);
            context.getForWhom().sendRawMessage("Гильдия создана!");
            //context.setSessionData("guildName", guild_name);
            return END_OF_CONVERSATION;
        }
    }

    private class PlayerInvitePrompt extends PlayerNamePrompt {

        public PlayerInvitePrompt(Plugin plugin) {
            super(plugin);
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Player p) {

            p.sendMessage("Приглашение отправлено");

            return END_OF_CONVERSATION;
        }

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "Введите имя игрока, которого вы хотите пригласить в гильдию";
        }

        @Override
        protected String getFailedValidationText(ConversationContext context,
                                                 String invalidInput) {
            return ChatColor.RED + "Вы ошиблись, попробуйте снова";
        }
    }



}
