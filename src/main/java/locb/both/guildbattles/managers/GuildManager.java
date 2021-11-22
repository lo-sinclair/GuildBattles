package locb.both.guildbattles.managers;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.Messages;
import locb.both.guildbattles.Rank;
//import locb.both.guildbattles.conversation.GuildNamePrompt;
import locb.both.guildbattles.cooldowns.TimeCooldown;
import locb.both.guildbattles.gui.GuiToolKit;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class GuildManager {

    private final GuildBattles pl;
    private TimeCooldown coolDown = GuildBattles.getInviteCoolDown();

    public GuildManager() {
        pl = GuildBattles.getInstance();
    }

    public void createGuildAction(Player p){


        if(!EconomyManager.takeMoney(p, 10)) {
            p.sendMessage(ChatColor.RED + "У вас недостаточно средств на счете!");
            return;
        }

        ConversationFactory cf = new ConversationFactory(pl);
        Conversation conv = cf.withFirstPrompt(new GuildNamePrompt("[a-zA-Z0-9_-]+"))
                .withLocalEcho(false)
                .withTimeout(30)
                .buildConversation(p);
        conv.begin();
    }


    public void inviteToGuildAction(Player p) {

        ConversationFactory cf = new ConversationFactory(pl);
        Conversation conv = cf.withFirstPrompt(new PlayerInvitePrompt(pl))
                .withLocalEcho(false)
                .withTimeout(30)
                .buildConversation(p);
        conv.begin();
    }

    public void leaveGuildAction(Player p) {
        TextComponent msg = GuiToolKit.confirmMessage(p, "Вы уверены, что хотите покинуть гильдию?",
                "/guild leave accept", "/guild leave deny");
        p.spigot().sendMessage(msg);
    }

    public void makeTrustedAction(Player sender, OfflinePlayer target) {
        TextComponent msg = GuiToolKit.confirmMessage(sender, "Вы уверены?",
                "/guild rank " + target.getUniqueId().toString() + " trusted accept", "/guild rank " + target.getUniqueId().toString() + " trusted deny");
        sender.spigot().sendMessage(msg);
    }

    public void makeMemberAction(Player sender, OfflinePlayer target) {
        TextComponent msg = GuiToolKit.confirmMessage(sender, "Вы уверены?",
                "/guild rank " + target.getUniqueId().toString() + " member accept", "/guild rank " + target.getUniqueId().toString() + " member deny");
        sender.spigot().sendMessage(msg);
    }

    public void giveLeaderAction(Player sender, OfflinePlayer target) {
        TextComponent msg = GuiToolKit.confirmMessage(sender, "Вы уверены, что хотите передать права на управление гильдией игроку " +
                target.getName() + "?",
                "/guild rank " + target.getUniqueId().toString() + " leader accept", "/guild rank " + target.getUniqueId().toString() + " leader deny");
        sender.spigot().sendMessage(msg);
    }

    public void assignPrivateAction(Player sender, OfflinePlayer target, boolean allow) {
        TextComponent msg = GuiToolKit.confirmMessage(sender, "Вы уверены?",
                "/guild privat " + target.getUniqueId().toString() + " " + (allow ? "true" : "false") + " accept", "/guild privat " + target.getName() + " " + (allow ? "true" : "false") + " deny");
        sender.spigot().sendMessage(msg);
    }

    public void kickMemberAction(Player sender, OfflinePlayer target){
        TextComponent msg = GuiToolKit.confirmMessage(sender, "Вы уверены, что хотите выгнать " + target.getName() + "из гильдии?",
                "/guild kick " + target.getName() + " accept", "/guild kick " + target.getName() + " deny");
        sender.spigot().sendMessage(msg);
    }

    public void sendMoneyToGuild(Player ps){

        ConversationFactory cf = new ConversationFactory(pl);
        Conversation conv = cf.withFirstPrompt(new takeMoneyPrompt())
                .withLocalEcho(false)
                .withTimeout(30)
                .buildConversation(ps);
        conv.begin();

    }



    public boolean kickMember(String name) {
        Member member = pl.getDb().findMemberByName(name);
        if(member != null) {
            if(member.getRole() == "leader") {
                return false;
            }
            pl.getDb().removeMember(name);
            pl.updateAllPlayerMenuUsage();
            return true;
        }
        return false;
    }


    public boolean assignPrivate(OfflinePlayer pt, boolean allow) {
        Member member = pl.getDb().findMemberByUUID(pt.getUniqueId().toString());
        if(member != null) {
            member.setPrivat(allow);
            pl.getDb().updateMember(member);
            pl.updateAllPlayerMenuUsage();
            return true;
        }
        return false;
    }


    public boolean makeRank(OfflinePlayer pt, Rank rank) {
        Member member = pl.getDb().findMemberByUUID(pt.getUniqueId().toString());
        if(member != null) {
            if( pl.getRankManager().playerRank(member) == rank) {
                return false;
            }
            member.setRole(rank.getName());
            pl.getDb().updateMember(member);
            pl.updateAllPlayerMenuUsage();
            return true;
        }
        return false;
    }



    public void sendInviteToGuild(Player sender, Player target) {

        coolDown.addPlayerToCoolDown(target, 120);
        Guild guild = GuildBattles.getPlayerMenuUsage(sender).getGuild();

        // Сообщение для target
        TextComponent msg = new TextComponent(Messages.getPrefix() + ChatColor.BLUE + sender.getName() + ChatColor.RESET +
                " приглашает вас вступить в гильдию  " +  ChatColor.BLUE +"\""  + guild.getName() + "\"" + ChatColor.RESET  + ". Хотите принять приглашение?\n");
        TextComponent c1 = new TextComponent(ChatColor.BOLD + "" + ChatColor.GREEN + "[Принять] ");
        c1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild invite accept " + sender.getName() + " " + target.getName()));

        TextComponent c2 = new TextComponent(ChatColor.BOLD + "" + ChatColor.RED + "[Отказаться] ");
        c2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild invite deny " + sender.getName() + " " + target.getName()));

        msg.addExtra(c1);
        msg.addExtra(c2);
        target.spigot().sendMessage(msg);
    }


    public void addMemberToGuild(Player sender, Player target ){

        int guildId = pl.getDb().findMemberByName(sender.getName()).getGuildId();
        long ts_naw = System.currentTimeMillis();
        Member member = new Member(0, target.getName(), target.getUniqueId(), guildId, ts_naw, "member", 0, 0, 0, 0.0, false);

        if (pl.getDb().createMember(member) != 0) {
            pl.updateAllPlayerMenuUsage();
            coolDown.removePlayerInCoolDown(target);
        }
    }


    private void createNewGuild(String guild_name, Player p) {
        long ts_naw = System.currentTimeMillis();
        Guild guild = new Guild(0, guild_name, ts_naw, 0.0, false, "", "");

        int guild_id = pl.getDb().createGuild(guild);

        Member member = new Member(0, p.getName(), p.getUniqueId(), guild_id, ts_naw,"leader",0, 0, 0, 0.0, false);

        pl.getDb().createMember(member);
        pl.updatePlayerMenuUsage(p);
    }


    public void leaveGuild(Player p) {
        if(pl.getRankManager().playerRank(p).equals(Rank.MEMBER)) {
            pl.getDb().removeMember(p.getName());
            p.sendMessage(Messages.getPrefix() + "Вы покинули гильдию!");
        }
        if(pl.getRankManager().playerRank(p).equals(Rank.LEADER)) {
            Member member = pl.getDb().findMemberByName(p.getName());
            pl.getDb().removeGuild(member.getGuildId());
            p.sendMessage(Messages.getPrefix() + "Ваша гильдия прекратила существование :(");
        }
        pl.updateAllPlayerMenuUsage();
    }



    private class takeMoneyPrompt extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            return Messages.getPrefix() + "Введите сумму перевода";
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number number) {

            Player p = (Player)context.getForWhom();
            if(!EconomyManager.takeMoney(p, number.doubleValue())) {
                p.sendMessage(Messages.getPrefix() + ChatColor.RED + "У вас недостаточно средств на счете!");
                return END_OF_CONVERSATION;
            }

            Guild g = pl.getDb().findGuildByMember(p.getName());
            g.setBalance(g.getBalance() + number.doubleValue());
            pl.getDb().updateGuild(g);
            pl.updateAllPlayerMenuUsage();
            System.out.println(g.getBalance());
            context.getForWhom().sendRawMessage(Messages.getPrefix() + "Гильдия " + g.getName() + " благодарит вас за перевод!");
            return END_OF_CONVERSATION;
        }
    }



    private class GuildNamePrompt extends RegexPrompt {

        public GuildNamePrompt(String regex) {
            super(regex);
        }

        @Override
        public String getPromptText(ConversationContext context) {
            return "Введите имя гильдии. (Используйте только латинские буквы, цифры и символы почеркивания). ";
        }


        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String guild_name) {
            if(guild_name.length() > 30 ) {
                context.getForWhom().sendRawMessage(Messages.getPrefix() +  ChatColor.RED + "Длина названия должна быть не более 30-ти символов!");
                return END_OF_CONVERSATION;
            }

            if( pl.getDb().findGuildByName(guild_name) != null) {
                context.getForWhom().sendRawMessage(Messages.getPrefix() +  ChatColor.RED + "Такая гильдия уже существует. Придумайте другое название!");
                return END_OF_CONVERSATION;
            }
            Player p = (Player)context.getForWhom();

            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
            createNewGuild(guild_name, p);
            context.getForWhom().sendRawMessage(Messages.getPrefix() + "Гильдия создана!");
            //context.setSessionData("guildName", guild_name);
            return END_OF_CONVERSATION;

        }

        @Override
        protected String getFailedValidationText(ConversationContext context,
                                                 String invalidInput) {
            return Messages.getPrefix() + ChatColor.RED + "Название может содержать только латинские буквы, цифры, и символы тире и подчеркивания!";
        }
    }


    private class PlayerInvitePrompt extends PlayerNamePrompt {

        public PlayerInvitePrompt(Plugin plugin) {
            super(plugin);
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Player target) {
            Player sender = (Player) context.getForWhom();
            if( pl.getRankManager().playerHasPerms(target, Rank.MEMBER ) ) {
                context.getForWhom().sendRawMessage(Messages.getPrefix() +  ChatColor.RED + "Этот игрок уже состоит в гильдии!");
                return END_OF_CONVERSATION;
            }

            if( coolDown.isPlayerInCoolDown(target) ) {
                context.getForWhom().sendRawMessage(Messages.getPrefix() + ChatColor.RED + "Вы только что отправляли приглашение этому пользователю. " +
                        "Ждите его ответа еще " + coolDown.getCooldownLeft(target) + " сек.");
                return END_OF_CONVERSATION;
            }

            context.getForWhom().sendRawMessage("Приглашение отправлено!");
            sendInviteToGuild((Player)context.getForWhom(), target);

            return END_OF_CONVERSATION;
        }

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return Messages.getPrefix() + "Введите имя игрока, которого вы хотите пригласить в гильдию";
        }

        @Override
        protected String getFailedValidationText(ConversationContext context,
                                                 String invalidInput) {
            return Messages.getPrefix() + ChatColor.RED + "Вы ошиблись, попробуйте снова";
        }
    }

    public List<OfflinePlayer> guildPlayers(Guild g, Rank role) {
        List<OfflinePlayer> players = new ArrayList<>();
        List<Member> members = pl.getDb().findMembersByGuild(g.getId(), role.getName());
        for(Member member : members) {
            //Player gp = Bukkit.getPlayer(member.getUuid());
            OfflinePlayer gp = Bukkit.getOfflinePlayer(member.getUuid());
//            if( gp == null ) {
//                 gp = (Player) Bukkit.getOfflinePlayer(member.getUuid());
//            }
            if(gp != null) {
                players.add(gp);
            }
        }
        return players;
    }

}
