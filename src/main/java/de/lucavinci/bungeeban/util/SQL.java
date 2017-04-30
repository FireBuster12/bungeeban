package de.lucavinci.bungeeban.util;

import java.sql.*;

/**
 * This class handles all SQL traffic of the plugin.
 * MYSQL and SQLITE sql drivers are supported.
 */
public class SQL {

    private DriverType driver;
    private Connection connection = null;

    private String filename;

    private String hostname;
    private int port;
    private String username, password, database;

    /**
     * Create a new SQLITE driver.
     * @param filename Path to sqlite file
     */
    public SQL(String filename) {
        this.filename = filename;
        this.driver = DriverType.SQLITE;
    }

    /**
     * Create a new MYSQL driver.
     * @param hostname IP of the server
     * @param port Port of the server
     * @param username Username to the database
     * @param password Password of the given user
     * @param database Database
     */
    public SQL(String hostname, int port, String username, String password, String database) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.driver = DriverType.MYSQL;
    }

    /**
     * Checks whether the SQL driver is connected or not.
     * @return true, if connected; false, if not.
     */
    public boolean isConnected() {
        return this.connection != null;
    }

    /**
     * Establishes the SQL driver connection.
     */
    public void openConnection() {
        if(!this.isConnected()) {
            try {
                if (this.driver == DriverType.MYSQL) {
                    Class.forName("com.mysql.jdbc.Driver");
                    this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/"
                                    + this.database + "?autoReconnect=true", this.username, this.password);
                } else if (this.driver == DriverType.SQLITE) {
                    Class.forName("org.sqlite.JDBC");
                    this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.filename);
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the SQL driver connection.
     */
    public void closeConnection() {
        if(this.isConnected()) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Executes an update on the SQL driver with the given query.
     * @param query SQL string
     */
    public void update(String query) {
        if(this.isConnected()) {
            try {
                this.connection.prepareStatement(query).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Executes an update on the SQL driver with the given query and returns its result
     * @param query SQL string
     * @return Result of the sql query
     */
    public ResultSet getResult(String query) {
        if(this.isConnected()) {
            try {
                return this.connection.prepareStatement(query).executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public DriverType getDriver() {
        return driver;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getFilename() {
        return filename;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setDriver(DriverType driver) {
        this.driver = driver;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static enum DriverType {
        MYSQL, SQLITE;
    }

}
