package hr.fer.zemris.java.hw15.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single blog user.
 * <p>
 * Each blog user is uniquely identified by it's ID, given by
 * the database initialization procedure during the registration
 * process.
 * <p>
 * The registration/login process uses the attribute {@link #nick}
 * as the username, and the attribute {@link #passwordHash} as the
 * hashed value of the user's password. These two attributes are
 * checked against the values from the database on any registration
 * or login attempt - this means that the user's nickname is unique.
 *
 * @author Luka Čupić
 */
@Entity
@Table(name = "blog_users")
public class BlogUser {

	/**
	 * The ID of the user.
	 */
	private Long id;

	/**
	 * User's first name.
	 */
	private String firstName;

	/**
	 * User's last name.
	 */
	private String lastName;

	/**
	 * User's nickname. Each nickname is unique, meaning
	 * only one user can have the same nickname.
	 */
	private String nick;

	/**
	 * User's e-mail address.
	 */
	private String email;

	/**
	 * The hash value of the user's password (because plain-text
	 * passwords are for newbies).
	 */
	private String passwordHash;

	/**
	 * A list of blog entries posted by the user.
	 */
	private List<BlogEntry> entries = new ArrayList<>();

	/**
	 * Gets the user's ID.
	 *
	 * @return the user's ID.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets the user's ID.
	 *
	 * @param id the ID to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the user's first name.
	 *
	 * @return user's first name
	 */
	@Column
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the user's first name.
	 *
	 * @param firstName user's first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the user's last name.
	 *
	 * @return user's last name
	 */
	@Column
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the user's last name.
	 *
	 * @param lastName user's first name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the user's nickname.
	 *
	 * @return the user's nickname
	 */
	@Column
	public String getNick() {
		return nick;
	}

	/**
	 * Sets the user's nickname.
	 *
	 * @param nick the user's nickname
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Gets the user's e-mail address.
	 *
	 * @return the user's e-mail
	 */
	@Column
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the user's e-mail address.
	 *
	 * @param email the user's e-mail address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the hash value of the user's password.
	 *
	 * @return the hash value of the user's password.
	 */
	@Column
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Sets the hash value of the user's password.
	 *
	 * @param passwordHash the hash value of the user's password
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * Gets a list of blog entries posted by the user.
	 *
	 * @return a list of blog entries posted by the user
	 */
	@OneToMany(
		mappedBy = "creator",
		fetch = FetchType.LAZY,
		cascade = CascadeType.PERSIST,
		orphanRemoval = true)
	@OrderBy("createdAt")
	public List<BlogEntry> getEntries() {
		return entries;
	}

	/**
	 * Sets a list of blog entries posted by the user.
	 *
	 * @param entries a list of blog entries posted by the user
	 */
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BlogUser blogUser = (BlogUser) o;
		return id.equals(blogUser.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}