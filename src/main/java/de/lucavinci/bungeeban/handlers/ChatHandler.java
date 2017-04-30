package de.lucavinci.bungeeban.handlers;

import de.lucavinci.bungeeban.BungeeBan;
import de.lucavinci.bungeeban.util.BungeeBanPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * This handler checks whether a player is muted and then forbids him to chat.
 */
public class ChatHandler implements Listener {

    @EventHandler
    public void onChat(ChatEvent e) {
        ProxiedPlayer p = (ProxiedPlayer)e.getSender();
        if(!e.getMessage().toLowerCase().startsWith("/")) {
            BungeeBanPlayer bbp = BungeeBan.getApi().getPlayer(p.getUniqueId());
            if(bbp.isMuted()) {
                e.setCancelled(true);
                for(String msg : bbp.getMuteMessage().split("\n")) {
                    p.sendMessage(BungeeBan.PREFIX + msg);
                }
            }
        }
    }

}
