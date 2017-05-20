package de.lucavinci.bungeeban.handlers;

import de.lucavinci.bungeeban.BungeeBan;
import de.lucavinci.bungeeban.util.BungeeBanPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * This handler checks whether a player who is logging in already exists in the
 * database, and if not, creates his profile in the database.
 */
public class LoginHandler implements Listener {

    @EventHandler
    public void onLogin(final LoginEvent e) {
        BungeeCord.getInstance().getScheduler().runAsync(BungeeBan.getInstance(), new Runnable() {
            @Override
            public void run() {
                BungeeBanPlayer bbp = BungeeBan.getApi().getPlayer(e.getConnection().getUniqueId(), e.getConnection().getName());
                bbp.setPlayername(e.getConnection().getName());
                bbp.setLastLogin(System.currentTimeMillis());
                bbp.save();
            }
        });
    }

}
