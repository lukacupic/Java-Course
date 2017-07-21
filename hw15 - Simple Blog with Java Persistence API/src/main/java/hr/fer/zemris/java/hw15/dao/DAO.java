package hr.fer.zemris.java.hw15.dao;

import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

import java.util.List;

/**
 * This class represents a service provider towards the data persistence
 * subsystem. The implementation of this interface must be able to provide
 * access to some type of subsystem or other persistence mechanism which is
 * in charge of data manipulation.
 *
 * @author Luka Čupić
 */
public interface DAO {

    /**
     * Returns a blog entry with the specified {@code id}. If no such entry
     * exists, {@code null} is returned.
     *
     * @param id the ID of the entry
     * @return an entry or {@code null} if no such entry exists
     * @throws DAOException if an error occurs while accessing the data
     */
    BlogEntry getEntry(Long id) throws DAOException;

    /**
     * Returns a list of all entries published by a user with the specified ID,
     * or {@code null} if the user does not exist.
     *
     * @param id the ID of the user, or {@code null} if the user does not exist.
     * @return a list of all entries published by a user with the specified ID.
     * @throws DAOException if an error occurs while accessing the data
     */
    List<BlogEntry> getEntries(Long id) throws DAOException;

    /**
     * Adds a new blog entry to the persistence context.
     *
     * @param entry the entry to add
     * @throws DAOException if an error occurs while accessing the data
     */
    void addNewEntry(BlogEntry entry) throws DAOException;

    /**
     * Updates an existing entry in the database.
     *
     * @param entry the modified entry
     * @throws DAOException if an error occurs while accessing the data
     */
    void updateEntry(BlogEntry entry) throws DAOException;

    /**
     * Adds a new blog comment to the persistence context.
     *
     * @param comment the comment to add
     * @throws DAOException if an error occurs while accessing the data
     */
    void addNewComment(BlogComment comment) throws DAOException;

    /**
     * Returns a blog user with the specified nickname. If no such user
     * exists, {@code null} is returned.
     *
     * @param nick the nickname of the user
     * @return a user or {@code null} if no such user exists
     * @throws DAOException if an error occurs while accessing the data
     */
    BlogUser getUser(String nick) throws DAOException;

    /**
     * Returns a list of all blog users.
     *
     * @return a list of users
     * @throws DAOException if an error occurs while accessing the data
     */
    List<BlogUser> getUsers() throws DAOException;

    /**
     * Adds a new blog user into the persistence context.
     *
     * @param user the new user
     * @throws DAOException if an error occurs while accessing the data
     */
    void addUser(BlogUser user) throws DAOException;
}