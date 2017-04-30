package de.lucavinci.bungeeban.util;

import de.lucavinci.bungeeban.BungeeBan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * This class represents a ban of a {@link BungeeBanPlayer}.
 * A player can be banned multiple times, but the newest ban
 * makes the old bans invalid.
 *
 * A ban has an own {@link UUID} to keep track of them.
 */
public class Ban implements DBObject {

    private UUID uuid, banId;
    private String bannedBy, banReason;
    private long banEnd, bannedOn;

    private boolean active;

    /**
     * This constructor is used to create an object of an existing ban.
     * The information will be fetched automatically.
     * @param banId The id of the existing ban.
     */
    public Ban(UUID banId) {
        this.banId = banId;
        this.fetch();
    }

    /**
     * This constructor creates a new ban.
     * A random ban id will be created.
     * @param uuid {@link UUID} of the Player
     * @param bannedBy Name of the person who banned the Player
     * @param banReason Reason of the ban
     * @param milliseconds Length of the ban (-1 for permanent)
     */
    public Ban(UUID uuid, String bannedBy, String banReason, long milliseconds) {
        this.uuid = uuid;
        this.banId = UUID.randomUUID();
        this.bannedBy = bannedBy;
        this.banReason = banReason;
        this.bannedOn = System.currentTimeMillis();
        if(milliseconds != -1) {
            this.banEnd = System.currentTimeMillis() + milliseconds;
        } else {
            this.banEnd = milliseconds;
        }
        this.active = true;
    }

    public String getBanReason() {
        return banReason;
    }

    public String getBannedBy() {
        return bannedBy;
    }

    public long getBanEnd() {
        return banEnd;
    }

    public long getBannedOn() {
        return bannedOn;
    }

    public UUID getBanId() {
        return banId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Check whether the ban has already exceeded or not.
     * @return true, if ban is still valid, false, if not.
     */
    public boolean isValid() {
        return this.isActive() && (this.banEnd > System.currentTimeMillis() || this.banEnd == - 1);
    }

    /**
     * Calculates the remaining time and formats it to a string.
     * @return The remaining time.
     */
    public String remainingTime() {
        if(this.isValid()) {
            if(this.banEnd == -1) {
                return ConfigManager.txt("permanenttime");
            }
            long seconds = (this.banEnd - System.currentTimeMillis()) / 1000L;
            long minutes = 0;
            while(seconds >= 60) {
                minutes += 1;
                seconds -= 60;
            }
            long hours = 0;
            while(minutes >= 60) {
                hours += 1;
                minutes -= 60;
            }
            long days = 0;
            while(hours >= 24) {
                days += 1;
                hours -= 24;
            }
            return ConfigManager.txt("remainingtime").replace("%DAYS%", days + "").replace("%HOURS%", hours + "").replace("%MINUTES%", minutes + "").replace("%SECS%", seconds + "");
        }
        return null;
    }

    @Override
    public void fetch() {
        ResultSet rs = BungeeBan.getSql().getResult("SELECT * FROM bungeeban_bans WHERE banid='" + this.banId.toString() + "'");
        try {
            if(rs.next()) {
                this.uuid = UUID.fromString(rs.getString("uuid"));
                this.bannedBy = rs.getString("bannedBy");
                this.banReason = rs.getString("banReason");
                this.banEnd = rs.getLong("banEnd");
                this.bannedOn = rs.getLong("bannedOn");
                this.active = Boolean.parseBoolean(rs.getString("active"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        this.remove();
        BungeeBan.getSql().update("INSERT INTO bungeeban_bans(banid, uuid, bannedBy, banReason, banEnd, bannedOn, active) " +
                "VALUES('" + this.banId.toString() + "','" + this.uuid.toString() + "','" + this.bannedBy + "','" + this.banReason + "'," +
                "'" + this.banEnd + "','" + this.bannedOn + "','" + Boolean.toString(this.active) + "')");
    }

    @Override
    public void remove() {
        BungeeBan.getSql().update("DELETE FROM bungeeban_bans WHERE banid='" + this.banId.toString() + "'");
    }

}
