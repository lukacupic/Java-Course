package hr.fer.zemris.java.hw15.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents a blog entry. A blog entry can
 * be published only by a user who has been registered
 * on the web application.
 *
 * @author Luka Čupić
 */
@Entity
@Table(name = "blog_entries")
public class BlogEntry {

	/**
	 * The ID of the entry.
	 */
	private Long id;

	/**
	 * The list of comments.
	 */
	private List<BlogComment> comments = new ArrayList<>();

	/**
	 * The time the entry was published at.
	 */
	private Date createdAt;

	/**
	 * The time the entry was modified at.
	 */
	private Date lastModifiedAt;

	/**
	 * The title of the entry.
	 */
	private String title;

	/**
	 * The text of the entry.
	 */
	private String text;

	/**
	 * The user who created the entry.
	 */
	private BlogUser creator;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(
		mappedBy = "blogEntry",
		fetch = FetchType.LAZY,
		cascade = CascadeType.PERSIST,
		orphanRemoval = true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}

	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	@Column(length = 200, nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(length = 4096, nullable = false)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogUser getCreator() {
		return creator;
	}

	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BlogEntry blogEntry = (BlogEntry) o;
		return id.equals(blogEntry.id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}