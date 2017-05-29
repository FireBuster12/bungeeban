package de.lucavinci.bungeeban.events;

import de.lucavinci.bungeeban.util.Mute;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class BungeeMuteEvent extends Event {

    private boolean cancelled = false;

    private UUID banned;
    private Mute mute;

    public BungeeMuteEvent(UUID banned, Mute mute) {
        this.banned = banned;
        this.mute = mute;
    }

    public Mute getMute() {
        return mute;
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

    public void setMute(Mute mute) {
        this.mute = mute;
    }
}
