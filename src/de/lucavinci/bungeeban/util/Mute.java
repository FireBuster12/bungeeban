package de.lucavinci.bungeeban.util;

import de.lucavinci.bungeeban.BungeeBan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * This class represents a mute of a {@link BungeeBanPlayer}.
 * A player can be muted multiple times, but the newest mute
 * makes the old mutes invalid.
 *
 * A mute has an own {@link UUID} to keep track of them.
 */
public class Mute implements DBObject {

    private UUID uuid, muteId;
    private String mutedBy, muteReason;
    private long muteEnd, mutedOn;

    private boolean active;

    /**
     * This constructor is used to create an object of an existing mute.
     * The information will be fetched automatically.
     * @param muteId The id of the existing mute.
     */
    public Mute(UUID muteId) {
        this.muteId = muteId;
        this.fetch();
    }

    /**
     * This constructor creates a new mute.
     * A random mute id will be created.
     * @param uuid {@link UUID} of the Player
     * @param mutedBy Name of the person who muted the Player
     * @param muteReason Reason of the mute
     * @param milliseconds Length of the mute (-1 for permanent)
     */
    public Mute(UUID uuid, String mutedBy, String muteReason, long milliseconds) {
        this.uuid = uuid;
        this.muteId = UUID.randomUUID();
        this.mutedBy = mutedBy;
        this.muteReason = muteReason;
        this.mutedOn = System.currentTimeMillis();
        if(milliseconds != -1) {
            this.muteEnd = System.currentTimeMillis() + milliseconds;
        } else {
            this.muteEnd = milliseconds;
        }
        this.active = true;
    }

    public String getMuteReason() {
        return muteReason;
    }

    public String getMutedBy() {
        return mutedBy;
    }

    public long getMuteEnd() {
        return muteEnd;
    }

    public long getMutedOn() {
        return mutedOn;
    }

    public UUID getMuteId() {
        return muteId;
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
     * Check whether the mute has already exceeded or not.
     * @return true, if mute is still valid, false, if not.
     */
    public boolean isValid() {
        return this.isActive() && (this.muteEnd > System.currentTimeMillis() || this.muteEnd == - 1);
    }

    /**
     * Calculates the remaining time and formats it to a string.
     * @return The remaining time.
     */
    public String remainingTime() {
        if(this.isValid()) {
            if(this.muteEnd == -1) {
                return ConfigManager.txt("permanenttime");
            }
            long seconds = (this.muteEnd - System.currentTimeMillis()) / 1000L;
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
        ResultSet rs = BungeeBan.getSql().getResult("SELECT * FROM bungeeban_mutes WHERE muteid='" + this.muteId.toString() + "'");
        try {
            if(rs.next()) {
                this.uuid = UUID.fromString(rs.getString("uuid"));
                this.mutedBy = rs.getString("mutedBy");
                this.muteReason = rs.getString("muteReason");
                this.muteEnd = rs.getLong("muteEnd");
                this.mutedOn = rs.getLong("mutedOn");
                this.active = Boolean.parseBoolean(rs.getString("active"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        this.remove();
        BungeeBan.getSql().update("INSERT INTO bungeeban_mutes(muteid, uuid, mutedBy, muteReason, muteEnd, mutedOn, active) " +
                "VALUES('" + this.muteId.toString() + "','" + this.uuid.toString() + "','" + this.mutedBy + "','" + this.muteReason + "'," +
                "'" + this.muteEnd + "','" + this.mutedOn + "','" + Boolean.toString(this.active) + "')");
    }

    @Override
    public void remove() {
        BungeeBan.getSql().update("DELETE FROM bungeeban_mutes WHERE muteid='" + this.muteId.toString() + "'");
    }

}
