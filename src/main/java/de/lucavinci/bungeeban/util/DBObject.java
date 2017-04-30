package de.lucavinci.bungeeban.util;

/**
 * This interface is used for classes whose objects are stored in a database.
 * It has universal methods for saving, fetching and removing the object
 * to / from the database.
 */
public interface DBObject {

    /**
     * This method fetches the object from the database and sets the values
     * in the current object. Unsaved data will be lost.
     */
    void fetch();

    /**
     * This method saves the object to the database.
     */
    void save();

    /**
     * This method fully removes the object from the database.
     * Warning: All data will be lost forever.
     */
    void remove();

}
