package de.lucavinci.bungeeban.util;

import com.google.common.io.ByteStreams;
import de.lucavinci.bungeeban.BungeeBan;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

/**
 * This class represents a configuration file of a BungeeCord plugin.
 * If you provide a default configuration, a default configuration file
 * will be created on startup, in case no file exists.
 */
public class Config {

    private String filename;
    private String defaultConfig;
    private Configuration configuration;

    /**
     * Create a new configuration file instance
     * @param filename Path to configuration file
     */
    public Config(String filename) {
        this.filename = filename;
    }

    /**
     * Create a new configuration file with a default config
     * @param filename Path to configuration file
     * @param defaultConfig Path to default configuration file resource
     */
    public Config(String filename, String defaultConfig) {
        this.filename = filename;
        this.defaultConfig = defaultConfig;
    }

    /**
     * Initializes the configuration files and instances.
     * Creates a new configuration file in case none exists
     * and writes the default configuration data in it, in case
     * a default configuration was given.
     */
    public void initialize() {
        if (!BungeeBan.getInstance().getDataFolder().exists()) {
            BungeeBan.getInstance().getDataFolder().mkdirs();
        }
        File file = this.getConfigurationFile();
        if (!file.exists()) {
            try {
                file.createNewFile();
                if (this.defaultConfig != null) {
                    try (InputStream is = BungeeBan.getInstance().getResourceAsStream(this.defaultConfig);
                         OutputStream os = new FileOutputStream(file)) {
                        ByteStreams.copy(is, os);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.reload();
    }

    /**
     * Reloads the configuration and instances.
     */
    public void reload() {
        try {
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.getConfigurationFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration to the file.
     */
    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.configuration, this.getConfigurationFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the configuration file from the datafolder.
     * @return Configuration file
     */
    private File getConfigurationFile() {
        return new File(BungeeBan.getInstance().getDataFolder(), this.filename);
    }

    public String getFilename() {
        return filename;
    }

    /**
     * Returns the configuration. You can get all set values from it.
     * @return YamlConfiguration
     */
    public Configuration c() {
        return configuration;
    }
}
