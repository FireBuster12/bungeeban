package de.lucavinci.bungeeban.util;

import de.lucavinci.bungeeban.BungeeBan;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;

/**
 * This class manages both configuration files of the plugin.
 */
public class ConfigManager {

    private static Config config, messages;

    public static void init() {
        // Initialize configuration files
        config = new Config("config.yml", "config.yml");
        messages = new Config("messages.yml", "messages.yml");
        config.initialize();
        messages.initialize();
        // Load SQL from config and setup drivers
        SQL.DriverType driverType = SQL.DriverType.valueOf(config.c().getString("sql.driver"));
        switch (driverType) {
            case SQLITE:
                BungeeBan.setSql(new SQL(config.c().getString("sql.sqlite.filename")));
                break;
            case MYSQL:
                BungeeBan.setSql(new SQL(
                        config.c().getString("sql.mysql.hostname"),
                        Integer.parseInt(config.c().getString("sql.mysql.port")),
                        config.c().getString("sql.mysql.username"),
                        config.c().getString("sql.mysql.password"),
                        config.c().getString("sql.mysql.database")
                        ));
                break;
            default:
                System.err.println("Unknown SQL Driver in config.yml.");
                break;
        }
        BungeeBan.PREFIX = txt("prefix");
        __performVersionUpgrades();
    }

    public static Config cfg() {
        return config;
    }

    public static Config msg() {
        return messages;
    }

    public static String cv(String path) {
        return config.c().getString(path);
    }

    public static String txt(String path) {
        return ChatColor.translateAlternateColorCodes('&', messages.c().getString(path));
    }

    public static String txt2(String path) {
        String str = "";
        for(String line : messages.c().getStringList(path)) {
            str += ChatColor.translateAlternateColorCodes('&', line) + "\n";
        }
        return str;
    }

    public static ArrayList<String> txt3(String path) {
        ArrayList<String> lines = new ArrayList<>();
        for(String line : messages.c().getStringList(path)) {
            lines.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return lines;
    }

    private static void __performVersionUpgrades() {
        if(config.c().get("general.asyncbancheck") == null) {
            config.c().set("general.asyncbancheck", true);
            config.save();
        }
        if(config.c().get("general.permissions.exempt") == null) {
            config.c().set("general.permissions.exempt.ban", "lucavinci.bungeeban.ban.exempt");
            config.c().set("general.permissions.exempt.mute", "lucavinci.bungeeban.mute.exempt");
            config.c().set("general.permissions.exempt.kick", "lucavinci.bungeeban.kick.exempt");
            config.save();
        }
    }

}
