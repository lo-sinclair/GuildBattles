package locb.both.guildbattles.data;

import locb.both.guildbattles.GuildBattles;
import locb.both.guildbattles.model.Guild;
import locb.both.guildbattles.model.Member;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SQLDatabase {
    private String url;
    private String user;
    private String password;

    public SQLDatabase() throws Exception {
        FileConfiguration config = GuildBattles.getInstance().getConfig();

        Connection conn;

        if(config.getBoolean("mySQL.enable")) {
            String host = config.getString("mySQL.host");
            int port = config.getInt("mySQL.port");
            String dbname = config.getString("mySQL.dbname");
            user = config.getString("mySQL.user");
            password = config.getString("mySQL.password");
            url = "jdbc:mysql://" + host + ":" + port + "/" +dbname;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = getConnection();
            } catch (Exception e) {
                url = "jdbc:sqlite:" + GuildBattles.getInstance().getDataFolder() + File.separator + "database.db";
                Class.forName("org.sqlite.JDBC");
                user = null;
                conn = getConnection();
                GuildBattles.getInstance().getLogger().warning("Failed to connect to MySQL, using SQLite");
            }


        } else {
            url = "jdbc:sqlite:" + GuildBattles.getInstance().getDataFolder() + File.separator + "database.db";
            Class.forName("org.sqlite.JDBC");
            conn = getConnection();
            GuildBattles.getInstance().getLogger().info("Using SQLite");
        }

        Statement s = (Statement) conn.createStatement();

        String q;
        q = "PRAGMA foreign_keys=on;";
        s.execute(q);

        q = "CREATE TABLE IF NOT EXISTS gb_guilds ("
                + "`id` INTEGER PRIMARY KEY,"
                + "`name` VARCHAR(30) NOT NULL,"
                + "`create_date` TIMESTAMP NOT NULL,"
                + "`balance` DOUBLE(64, 2) DEFAULT 0.0,"
                + "`allow_friendly_fire` TINYINT(1) DEFAULT 0,"
                + "`territory` VARCHAR(100),"
                + "`home` VARCHAR(100),"
                + " UNIQUE (`name`));";
        s.executeUpdate(q);

        q = "CREATE TABLE IF NOT EXISTS gb_members ("
                + "`id` INTEGER PRIMARY KEY,"
                + "`name` VARCHAR(30),"
                + "`uuid` VARCHAR(36),"
                + "`guild_id` INTEGER NOT NULL,"
                + "`join_date` TIMESTAMP NOT NULL,"
                + "`role` VARCHAR,"
                + "`rival_kills` INTEGER(11) DEFAULT 0,"
                + "`friendly_fire` INTEGER(11) DEFAULT 0,"
                + "`deaths` INTEGER(11) DEFAULT 0,"
                + "`deposit` DOUBLE(64,2) DEFAULT 0.0, "
                + "`privat` TINYINT(1) DEFAULT 0, "
                + "FOREIGN KEY (guild_id) REFERENCES gb_guilds(id) ON DELETE CASCADE);";
        s.executeUpdate(q);

        s.close();
        conn.close();
    }

    public int createGuild(Guild guild) {
        int count = 0;
        int id = 0;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();


            String q = "INSERT INTO gb_guilds (`name`, `create_date`, `balance`, `allow_friendly_fire`) "
                    + "VALUES ('%s', %d, %.2f, %d);";

            count = stmt.executeUpdate(String.format(Locale.ROOT, q,
                    guild.getName(),
                    guild.getCreateDate(),
                    guild.getBalance(),
                    (guild.isAllowFriendlyFire() ? 1 : 0)
            ));

            ResultSet keys = stmt.getGeneratedKeys();

            if(keys.next()) {
                id = keys.getInt(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


    public int updateGuild(Guild guild) {
        int count = 0;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q = "UPDATE gb_guilds SET " +
                    "`name` = '%s'," +
                    "`create_date` = %d," +
                    "`balance` = %.2f," +
                    "`allow_friendly_fire` = %d " +
                    "WHERE `id` = %d;";


            count = stmt.executeUpdate(String.format(Locale.ROOT, q,
                    guild.getName(),
                    guild.getCreateDate(),
                    guild.getBalance(),
                    (guild.isAllowFriendlyFire() ? 1 : 0),
                    guild.getId()
            ));

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public boolean removeGuild(int id) {
        boolean result = false;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q;
            q = "PRAGMA foreign_keys=on;";
            stmt.execute(q);

            q = "DELETE FROM gb_guilds WHERE `id` = '%d';";
            result = stmt.execute(String.format(q, id));

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }


    public int createMember(Member member) {
        int count = 0;
        int id = 0;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q = "INSERT INTO gb_members (`name`, `uuid`, `guild_id`, `join_date`, `role`, `rival_kills`, `friendly_fire`, `deaths`, `deposit`, `privat`) "
                    + "VALUES ('%s', '%s', %d, %d, '%s', %d, %d, %d, %.2f, %d);";
            count = stmt.executeUpdate(String.format(Locale.ROOT, q,
                    member.getName(),
                    member.getUuid().toString(),
                    member.getGuildId(),
                    member.getJoinDate(),
                    member.getRole(),
                    member.getRivalKills(),
                    member.getFriendlyFire(),
                    member.getDeaths(),
                    member.getDeposit(),
                    (member.isPrivat() ? 1 : 0)
            ));
            ResultSet keys = stmt.getGeneratedKeys();
            if(keys.next()) {
                id = keys.getInt(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


    public int updateMember(Member member) {
        int count = 0;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q = "UPDATE gb_members SET " +
                    "`name` = '%s'," +
                    "`uuid` = '%s'," +
                    "`guild_id` = %d," +
                    "`join_date` = %d," +
                    "`role` = '%s'," +
                    "`rival_kills` = %d," +
                    "`friendly_fire` = %d," +
                    " `deaths` = %d," +
                    "`deposit` = %.2f," +
                    "privat = %d " +
                    "WHERE `id` = %d;";

            count = stmt.executeUpdate(String.format(Locale.ROOT, q,
                    member.getName(),
                    member.getUuid().toString(),
                    member.getGuildId(),
                    member.getJoinDate(),
                    member.getRole(),
                    member.getRivalKills(),
                    member.getFriendlyFire(),
                    member.getDeaths(),
                    member.getDeposit(),
                    (member.isPrivat() ? 1 : 0),
                    member.getId()
            ));
            ResultSet keys = stmt.getGeneratedKeys();

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public boolean removeMember(int id) {
        boolean result = false;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q = "DELETE FROM gb_members WHERE `id` = '%d'";
            result = stmt.execute(String.format(q, id));

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public int removeMember(String name) {
        int count = 0;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q = "DELETE FROM gb_members WHERE `name` = '%s'";
            count = stmt.executeUpdate(String.format(q, name));

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public Member findMemberByName(String name) {
        Member member = null;

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q = "SELECT * FROM gb_members WHERE `name` = '%s'";

            ResultSet result = stmt.executeQuery(String.format(q, name));

            if(result.next()) {

                member = new Member(
                        result.getInt("id"),
                        result.getString("name"),
                        UUID.fromString(result.getString("uuid")),
                        result.getInt("guild_id"),
                        result.getLong("join_date"),
                        result.getString("role"),
                        result.getInt("rival_kills"),
                        result.getInt("friendly_fire"),
                        result.getInt("deaths"),
                        result.getDouble("deposit"),
                        result.getBoolean("privat")
                );
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return member;
    }



    public  List<Member> findMembersByGuild(Integer guild_id, String role) {
        List<Member> members = new ArrayList<>();

        try {
            Connection c = getConnection();
            Statement s = c.createStatement();

            String q;
            ResultSet result;
            if (role == null) {
                q = "SELECT * FROM gb_members WHERE `guild_id` = %d;";
                result = s.executeQuery(String.format(q, guild_id));
            }
            else {
                q = "SELECT * FROM gb_members WHERE `guild_id` = %d AND `role` = '%s';";
                result = s.executeQuery(String.format(q, guild_id, role));
            }

            while(result.next()) {
                members.add (new Member(
                        result.getInt("id"),
                        result.getString("name"),
                        UUID.fromString(result.getString("uuid")),
                        result.getInt("guild_id"),
                        result.getLong("join_date"),
                        result.getString("role"),
                        result.getInt("rival_kills"),
                        result.getInt("friendly_fire"),
                        result.getInt("deaths"),
                        result.getDouble("deposit"),
                        result.getBoolean("privat")
                ));
            }

            s.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return members;
    }

    public  List<Member> findMembersByGuild(Integer guild_id) {
        return findMembersByGuild(guild_id, null);
    }


    public Guild findGuild(int id){
        Guild guild = null;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q = "SELECT * FROM gb_guilds WHERE `id` = %d";

            ResultSet result = stmt.executeQuery(String.format(q, id));

            if(result.next()) {
                guild = new Guild(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getLong("create_date"),
                    result.getDouble("balance"),
                    result.getBoolean("allow_friendly_fire"),
                    result.getString("territory"),
                    result.getString("home")
                );
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return guild;
    }


    public Guild findGuildByMember(String member_name){
        Guild guild = null;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q = "SELECT * FROM gb_guilds WHERE `id` = " +
                    "(SELECT `guild_id` FROM `gb_members` WHERE `name` = '%s')";

            ResultSet result = stmt.executeQuery(String.format(q, member_name));

            if(result.next()) {
                guild = new Guild(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getLong("create_date"),
                        result.getDouble("balance"),
                        result.getBoolean("allow_friendly_fire"),
                        result.getString("territory"),
                        result.getString("home")
                );
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return guild;
    }

    public List<String> findGuildsNames(){
        List<String>guildsNames = new ArrayList<>();

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            String q = "SELECT `name` FROM gb_guilds;";
            ResultSet result = stmt.executeQuery(q);

            while (result.next()) {
                guildsNames.add(result.getString("name"));
            }
            stmt.close();
            conn.close();

        } catch ( Exception e) {
            e.printStackTrace();
        }

        return guildsNames;
    }


    public Guild findGuildByName(String name){
        Guild guild = null;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String q = "SELECT * FROM gb_guilds WHERE `id` = '%s';";

            ResultSet result = stmt.executeQuery(String.format(q, name));

            if(result.next()) {
                guild = new Guild(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getLong("create_date"),
                        result.getDouble("balance"),
                        result.getBoolean("allow_friendly_fire"),
                        result.getString("territory"),
                        result.getString("home")
                );
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return guild;
    }



    public Connection getConnection() throws SQLException {
        if (user != null)
            return DriverManager.getConnection(url, user, password);
        else return DriverManager.getConnection(url);
    }

}
