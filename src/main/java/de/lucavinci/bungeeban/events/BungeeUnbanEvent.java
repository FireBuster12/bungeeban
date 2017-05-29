package de.lucavinci.bungeeban.events;

import de.lucavinci.bungeeban.util.Ban;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class BungeeUnbanEvent extends Event {

    private boolean cancelled = false;

    private UUID uuid;
    private Ban ban;

    public BungeeUnbanEvent(UUID uuid, Ban ban) {
        this.uuid = uuid;
        this.ban = ban;
    }

    public Ban getBan() {
        return ban;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
