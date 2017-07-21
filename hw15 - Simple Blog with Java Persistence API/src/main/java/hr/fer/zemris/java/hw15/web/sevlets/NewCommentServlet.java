package hr.fer.zemris.java.hw15.web.sevlets;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * This class implements a web servlet which adds a new comment
 * on to the blog. The servlet expects three parameters from the
 * request context: "entryID" (the ID of the entry on which this
 * comment was submitted), "message" (the message of the comment)
 * and "current.user.nick" (the nickname of the user who posted
 * the comment).
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/addComment")
public class NewCommentServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long entryID = Long.valueOf(req.getParameter("entryID"));
		BlogEntry entry = DAOProvider.getDao().getEntry(entryID);

		String commetCreatorNick = (String) req.getSession().getAttribute("current.user.nick");
		BlogUser commentCreator = DAOProvider.getDao().getUser(commetCreatorNick);

		BlogComment comment = new BlogComment();
		comment.setBlogEntry(entry);
		comment.setUsersEMail(commentCreator.getEmail());
		comment.setMessage(req.getParameter("message"));
		comment.setPostedOn(new Date());

		DAOProvider.getDao().addNewComment(comment);

		String creatorNick = entry.getCreator().getNick();
		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + creatorNick + "/" + entry.getId());
	}
}