package de.lucavinci.bungeeban;

import de.lucavinci.bungeeban.commands.*;
import de.lucavinci.bungeeban.handlers.ChatHandler;
import de.lucavinci.bungeeban.handlers.LoginHandler;
import de.lucavinci.bungeeban.handlers.ServerConnectHandler;
import de.lucavinci.bungeeban.util.ConfigManager;
import de.lucavinci.bungeeban.util.SQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.concurrent.TimeUnit;

/**
 * <h1>BungeeBan</h1>
 *
 * <p>BungeeBan is a BungeeCord plugin which manages bans and mutes.
 * It is fully configurable and lightweight.</p>
 *
 * <p>This is the main class of BungeeBan. It contains important instances and methods
 * which are executed on start and on stop of the plugin.</p>
 */
public class BungeeBan extends Plugin {

    public static String PREFIX = "";

    private static BungeeBan instance;
    private static SQL sql;

    private static BungeeBanApi api = new BungeeBanApi();

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.init();
        sql.openConnection();
        sql.update("CREATE TABLE IF NOT EXISTS bungeeban_players(uuid VARCHAR(100), playername VARCHAR(16), lastlogin LONG, PRIMARY KEY (uuid))");
        sql.update("CREATE TABLE IF NOT EXISTS bungeeban_bans(banid VARCHAR(100), uuid VARCHAR(100), bannedBy VARCHAR(100), banReason VARCHAR(100), banEnd LONG, bannedOn LONG, active VARCHAR(5), PRIMARY KEY (banid))");
        sql.update("CREATE TABLE IF NOT EXISTS bungeeban_mutes(muteid VARCHAR(100), uuid VARCHAR(100), mutedBy VARCHAR(100), muteReason VARCHAR(100), muteEnd LONG, mutedOn LONG, active VARCHAR(5), PRIMARY KEY (muteid))");
        this.register();
        BungeeCord.getInstance().getScheduler().schedule(instance, api, 1, 3, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable() {
        sql.closeConnection();
    }

    /**
     * Registers all commands and listeners
     */
    private void register() {
        PluginManager pm = BungeeCord.getInstance().getPluginManager();
        pm.registerListener(instance, new LoginHandler());
        pm.registerListener(instance, new ServerConnectHandler());
        pm.registerListener(instance, new ChatHandler());
        pm.registerCommand(instance, new CheckCommand("check"));
        pm.registerCommand(instance, new BanCommand("ban"));
        pm.registerCommand(instance, new UnbanCommand("unban"));
        pm.registerCommand(instance, new TempbanCommand("tempban"));
        pm.registerCommand(instance, new MuteCommand("mute"));
        pm.registerCommand(instance, new TempmuteCommand("tempmute"));
        pm.registerCommand(instance, new UnmuteCommand("unmute"));
        pm.registerCommand(instance, new KickCommand("kick"));
    }

    /**
     * Returns the instance of the Plugin
     * @return plugin instance
     */
    public static BungeeBan getInstance() {
        return instance;
    }

    /**
     * Returns the sql driver instance of the Plugin
     * @return sql instance
     */
    public static SQL getSql() {
        return sql;
    }

    /**
     * Returns the {@link BungeeBan} API
     * @return de.lucavinci.bungeeban.BungeeBanApi
     */
    public static BungeeBanApi getApi() {
        return api;
    }

    public static void setSql(SQL sql) {
        BungeeBan.sql = sql;
    }
}
