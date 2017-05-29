package de.lucavinci.bungeeban.events;

import net.md_5.bungee.api.plugin.Event;

public class BungeeKickEvent extends Event {

    private boolean cancelled = false;

    private String playername, reason, kickedBy;

    public BungeeKickEvent(String playername, String reason, String kickedBy) {
        this.playername = playername;
        this.reason = reason;
        this.kickedBy = kickedBy;
    }

    public String getPlayername() {
        return playername;
    }

    public String getReason() {
        return reason;
    }

    public String getKickedBy() {
        return kickedBy;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setKickedBy(String kickedBy) {
        this.kickedBy = kickedBy;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
