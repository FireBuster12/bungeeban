package de.lucavinci.bungeeban.util;

import de.lucavinci.bungeeban.BungeeBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * This class represents each player in the database.
 * Every player has n amount of bans and mutes, while
 * only having a maximum of one active ban or mute at
 * a time.
 */
public class BungeeBanPlayer implements DBObject {

    private UUID uuid;
    private String playername;
    private long lastLogin;

    private ArrayList<Ban> bans = new ArrayList<>();
    private ArrayList<Mute> mutes = new ArrayList<>();

    /**
     * This constructor is used to fetch the information
     * of an existing Player.
     * @param uuid The {@link UUID} of the existing player
     */
    public BungeeBanPlayer(UUID uuid) {
        this.uuid = uuid;
        this.fetch();
    }

    /**
     * This constructor is used to create a new Player.
     * @param uuid The {@link UUID} of the new player
     * @param playername The current playername of the new player
     */
    public BungeeBanPlayer(UUID uuid, String playername) {
        this.uuid = uuid;
        this.playername = playername;
        this.lastLogin = System.currentTimeMillis();
        this.save();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayername() {
        return playername;
    }

    public ArrayList<Ban> getBans() {
        return bans;
    }

    public ArrayList<Mute> getMutes() {
        return mutes;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Ban getActiveBan() {
        for(Ban ban : this.bans) {
            if(ban.isValid()) {
                return ban;
            }
        }
        return null;
    }

    public Mute getActiveMute() {
        for(Mute mute : this.mutes) {
            if(mute.isValid()) {
                return mute;
            }
        }
        return null;
    }

    public boolean isBanned() {
        return this.getActiveBan() != null;
    }

    public boolean isMuted() {
        return this.getActiveMute() != null;
    }

    public void ban(Ban ban) {
        this.unban();
        this.bans.add(ban);
        this.save();
        ban.save();
        if(this.toProxiedPlayer() != null) {
            this.toProxiedPlayer().disconnect(this.getBanMessage());
        }
    }

    public void mute(Mute mute) {
        this.unmute();
        this.mutes.add(mute);
        this.save();
        mute.save();
    }

    public void unban() {
        for(Ban b : this.bans) {
            b.setActive(false);
            b.save();
        }
    }

    public void unmute() {
        for(Mute m : this.mutes) {
            m.setActive(false);
            m.save();
        }
    }

    public ProxiedPlayer toProxiedPlayer() {
        return BungeeCord.getInstance().getPlayer(this.uuid);
    }

    public String getBanMessage() {
        if(this.getActiveBan() != null) {
            Ban ban = this.getActiveBan();
            return ConfigManager.txt2("messages.ban").replace("%BANNEDBY%", ban.getBannedBy()).replace("%REASON%", ban.getBanReason()).replace("%REMAININGTIME%", ban.remainingTime());
        }
        return null;
    }

    public String getMuteMessage() {
        if(this.getActiveMute() != null) {
            Mute mute = this.getActiveMute();
            return ConfigManager.txt2("messages.mute").replace("%MUTEDBY%", mute.getMutedBy()).replace("%REASON%", mute.getMuteReason()).replace("%REMAININGTIME%", mute.remainingTime());
        }
        return null;
    }

    @Override
    public void fetch() {
        try {
            this.bans = new ArrayList<>();
            this.mutes = new ArrayList<>();
            ResultSet rs = BungeeBan.getSql().getResult("SELECT * FROM bungeeban_players WHERE uuid='" + this.uuid.toString() + "'");
            if(rs.next()) {
                this.playername = rs.getString("playername");
                this.lastLogin = rs.getLong("lastlogin");
            }
            rs = BungeeBan.getSql().getResult("SELECT * FROM bungeeban_bans WHERE uuid='" + this.uuid + "'");
            while(rs.next()) {
                this.bans.add(new Ban(UUID.fromString(rs.getString("banid"))));
            }
            rs = BungeeBan.getSql().getResult("SELECT * FROM bungeeban_mutes WHERE uuid='" + this.uuid + "'");
            while(rs.next()) {
                this.mutes.add(new Mute(UUID.fromString(rs.getString("muteid"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void save() {
        this.remove();
        BungeeBan.getSql().update("INSERT INTO bungeeban_players(uuid, playername, lastlogin) VALUES('" + this.uuid.toString() + "', '" + this.playername + "', '" + this.lastLogin + "')");
    }

    @Override
    public void remove() {
        BungeeBan.getSql().update("DELETE FROM bungeeban_players WHERE uuid='" + this.uuid.toString() + "'");
    }
}
