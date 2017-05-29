package de.lucavinci.bungeeban.events;

import de.lucavinci.bungeeban.util.Ban;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class BungeeBanEvent extends Event {

    private boolean cancelled = false;

    private UUID banned;
    private Ban ban;

    public BungeeBanEvent(UUID banned, Ban ban) {
        this.banned = banned;
        this.ban = ban;
    }

    public Ban getBan() {
        return ban;
    }

    public UUID getBanned() {
        return banned;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setBanned(UUID banned) {
        this.banned = banned;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }
}
