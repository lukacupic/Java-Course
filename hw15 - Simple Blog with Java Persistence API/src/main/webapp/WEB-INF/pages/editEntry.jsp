<%@ page import="hr.fer.zemris.java.hw15.dao.DAOProvider" %>
<%@ page import="hr.fer.zemris.java.hw15.model.BlogEntry" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<%
    Long entryID = Long.valueOf(request.getParameter("entryID"));
    BlogEntry entry = DAOProvider.getDao().getEntry(entryID);
%>

<table width="100%">
    <tr>
        <td>
            <a href=" <%=request.getContextPath()%>/servleti/main">Home</a><br><br>
            <form action="<%=request.getContextPath()%>/servleti/editEntry?<%=entryID%>" method="post">
                Title: <input type="text" name="title" value="<%=entry.getTitle()%>"><br>
                <textarea rows="4" cols="50" name="text"><%=entry.getText()%></textarea><br>
                <input type="hidden" name="entryID" value=<%=entryID%>>
                <input type="submit" value="Submit">
            </form>
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