package de.lucavinci.bungeeban.handlers;

import de.lucavinci.bungeeban.BungeeBan;
import de.lucavinci.bungeeban.util.BungeeBanPlayer;
import de.lucavinci.bungeeban.util.ConfigManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * This handler checks whether a player who connects to the server is banned
 * and if so, forbids him to connect.
 */
public class ServerConnectHandler implements Listener {

    @EventHandler
    public void onConnect(final ServerConnectEvent e) {
        if(ConfigManager.cfg().c().getBoolean("general.asyncbancheck")) {
            BungeeCord.getInstance().getScheduler().runAsync(BungeeBan.getInstance(), new Runnable() {
                @Override
                public void run() {
                    BungeeBanPlayer bbp = BungeeBan.getApi().getPlayer(e.getPlayer().getUniqueId());
                    if (bbp.getActiveBan() != null) {
                        e.setCancelled(true);
                        bbp.toProxiedPlayer().disconnect(bbp.getBanMessage());
                    }
                }
            });
        }
    }

}
