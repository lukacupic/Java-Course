<%@ page import="hr.fer.zemris.java.hw15.model.BlogComment" %>
<%@ page import="hr.fer.zemris.java.hw15.model.BlogEntry" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<%
    BlogEntry entry = (BlogEntry) session.getAttribute("current.entry");
    String providedNick = (String) session.getAttribute("provided.user.nick");
    String currentNick = (String) session.getAttribute("current.user.nick");
    String authorURL = request.getContextPath() + "/servleti/author/" + providedNick;
%>
<table width="100%">
    <tr>
        <td>
            <a href=" <%=request.getContextPath()%>/servleti/main">Home</a><br>
            <h2><%=entry.getTitle()%>
            </h2>
            <p><%=entry.getText()%>
            </p>

            <table width="100%">
                <h3>Comments:</h3>

                <%if (entry.getComments().size() == 0) {%>
                <i>No comments...</i>
                <%}%>

                <%for (BlogComment comment : entry.getComments()) {%>
                <tr>
                    <td>
                        <%=comment.getMessage()%><br>
                        <i>posted by <%=comment.getUsersEMail()%> on <%=comment.getPostedOn().toString()%>
                        </i>
                        <hr>
                    </td>
                </tr>
                <%}%>
            </table>

            <br>
            <%if (session.getAttribute("current.user.id") != null) {%>
            <h3>Add Comment</h3>
            <form action="<%=request.getContextPath()%>/servleti/addComment" method="post">
                <textarea rows="4" cols="50" name="message"></textarea>
                <input type="hidden" name="entryID" value=<%=entry.getId()%>><br>
                <input type="submit" value="Submit">
            </form>
            <%}%>

            <%if (currentNick != null && session.getAttribute("current.user.nick").equals(providedNick)) {%>
            <p><a href="<%=authorURL%>/edit?entryID=<%=entry.getId()%>">Edit Entry</a><br></p>
            <%}%>
        </td>
        <td valign="top">
            <div align="right">
                <% if (session.getAttribute("current.user.id") != null) { %>
                Hello, <i><%=session.getAttribute("current.user.fn")%> <%=session.getAttribute("current.user.ln")%>
            </i><br>
                <a href=<%=request.getContextPath()%>/servleti/logout>Logout</a>
                <% } else { %>
                You are currently not logged in!
                <% } %>
            </div>
        </td>
    </tr>
</table>
</body>
</html>
