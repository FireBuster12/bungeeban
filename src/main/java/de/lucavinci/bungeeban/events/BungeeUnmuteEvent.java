package de.lucavinci.bungeeban.events;

import de.lucavinci.bungeeban.util.Mute;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class BungeeUnmuteEvent extends Event {

    private boolean cancelled = false;

    private UUID uuid;
    private Mute mute;

    public BungeeUnmuteEvent(UUID uuid, Mute mute) {
        this.uuid = uuid;
        this.mute = mute;
    }

    public Mute getMute() {
        return mute;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setMute(Mute mute) {
        this.mute = mute;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
