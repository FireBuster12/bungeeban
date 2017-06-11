package de.lucavinci.bungeeban;

import de.lucavinci.bungeeban.util.BungeeBanPlayer;
import net.md_5.bungee.BungeeCord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is the general API of the {@link BungeeBan} plugin.
 */
public class BungeeBanApi implements Runnable {

    private static final CopyOnWriteArrayList<BungeeBanPlayer> PLAYERS_CACHE = new CopyOnWriteArrayList<>();

    /**
     * Only package private access to the constructor so only the BungeeBan class can create an instance.
     */
    BungeeBanApi() {

    }

    /**
     * Fetches the {@link UUID} of a given player by his playername from the database.
     * @param playername Playername of the given player
     * @return UUID of the given player
     */
    public UUID getUuid(String playername) {
        ResultSet rs = BungeeBan.getSql().getResult("SELECT * FROM bungeeban_players WHERE playername='" + playername + "'");
        try {
            if(rs.next()) {
                return UUID.fromString(rs.getString("uuid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks whether a player with a given UUID exists.
     * @param uuid uuid of the given player
     * @return true, if he exists; false, if not
     */
    public boolean playerExists(UUID uuid) {
        ResultSet rs = BungeeBan.getSql().getResult("SELECT * FROM bungeeban_players WHERE uuid='" + uuid.toString() + "'");
        try {
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks whether a player with a given playername exists.
     * @param playername Playername of the given player
     * @return true, if he exists; false, if not
     */
    public boolean playerExists(String playername) {
        return this.getUuid(playername) != null;
    }

    /**
     * Returns the {@link BungeeBanPlayer} with the given {@link UUID}.
     * @param uuid UUID of the given player
     * @return BungeeBanPlayer instance with the given UUID
     */
    public BungeeBanPlayer getPlayer(UUID uuid) {
        for(BungeeBanPlayer bbp : PLAYERS_CACHE) {
            if(bbp.getUuid().toString().equalsIgnoreCase(uuid.toString())) {
                return bbp;
            }
        }
        BungeeBanPlayer bbp = new BungeeBanPlayer(uuid);
        PLAYERS_CACHE.add(bbp);
        return bbp;
    }

    /**
     * Returns the {@link BungeeBanPlayer} with the given {@link UUID} and creates a new database entry in case he doesn't exist.
     * @param uuid UUID of the given player
     * @return BungeeBanPlayer instance with the given UUID
     */
    public BungeeBanPlayer getPlayer(UUID uuid, String playername) {
        if(this.playerExists(uuid)) {
            return this.getPlayer(uuid);
        }
        BungeeBanPlayer bbp = new BungeeBanPlayer(uuid, playername);
        PLAYERS_CACHE.add(bbp);
        return bbp;
    }

    /**
     * Checks whether a player has the given permission.
     * This method is not properly working yet as it can not check the permissions of offline players.
     * @param uuid UUID of the player
     * @param permission Permission string to check
     * @return true, if the player has the given permission, false, if not
     */
    public boolean hasPermissions(UUID uuid, String permission) {
        if(BungeeCord.getInstance().getPlayer(uuid) != null) {
            return BungeeCord.getInstance().getPlayer(uuid).hasPermission(permission);
        }
        return false;
    }

    @Override
    public void run() {
        for(BungeeBanPlayer bbp : PLAYERS_CACHE) {
            if(bbp.toProxiedPlayer() == null) {
                PLAYERS_CACHE.remove(bbp);
            }
        }
    }
}
