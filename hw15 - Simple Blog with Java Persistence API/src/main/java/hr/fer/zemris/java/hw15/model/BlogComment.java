package hr.fer.zemris.java.hw15.model;

import javax.persistence.*;
import java.util.Date;

/**
 * This class represents a blog comment. Each blog comment must
 * have it's parent, i.e. the blog entry, otherwise instances of
 * this class would make absolutely no sense whatsoever.
 *
 * @author Luka Čupić
 */
@Entity
@Table(name = "blog_comments")
public class BlogComment {

	/**
	 * The comment's ID.
	 */
	private Long id;

	/**
	 * The entry, which is the parent of the comment.
	 */
	private BlogEntry blogEntry;

	/**
	 * The e-mail address of the user who posted the comment.
	 */
	private String usersEMail;

	/**
	 * The text of the comment itself.
	 */
	private String message;

	/**
	 * The time the comment was posted at.
	 */
	private Date postedOn;

	/**
	 * Gets the comment's ID.
	 *
	 * @return the comment's ID.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets the comment's ID.
	 *
	 * @param id the comment's ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the blog entry which this comment belongs to.
	 *
	 * @return the blog entry
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Sets the blog entry which this comment belongs to.
	 *
	 * @param blogEntry the blog entry
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Gets the e-mail of the user who posted the comment.
	 *
	 * @return the e-mail of the user who posted the comment.
	 */
	@Column(length = 100, nullable = false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Sets the e-mail of the user who posted the comment.
	 *
	 * @param usersEMail the e-mail of the user who posted the comment.
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Gets the text message of this comment.
	 *
	 * @return the text message of this comment.
	 */
	@Column(length = 4096, nullable = false)
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the text message for this comment.
	 *
	 * @param message the text message for this comment.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the date on which this comment was posted on.
	 *
	 * @return the date on which this comment was posted on.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets the date on which this comment was posted on.
	 *
	 * @param postedOn the date on which this comment was posted on.
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BlogComment that = (BlogComment) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}