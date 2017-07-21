<%@ page import="hr.fer.zemris.java.hw15.dao.DAOProvider" %>
<%@ page import="hr.fer.zemris.java.hw15.model.BlogEntry" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
</head>
<body>
<%
    String providedNick = (String) session.getAttribute("provided.user.nick");
    String currentNick = (String) session.getAttribute("current.user.nick");
    List<BlogEntry> entries = DAOProvider.getDao().getEntries((Long) session.getAttribute("provided.user.id"));
%>

<table width="100%">
    <tr>
        <td>
            <a href=" <%=request.getContextPath()%>/servleti/main">Home</a><br>
            <h2>Entries by <%=providedNick%>:</h2>
            <%if (entries.size() == 0) {%>
            <i>No entries...</i>
            <%}%>
            <p>
            <ul>
                <%for (BlogEntry entry : entries) {%>
                <li>
                    <a href=" <%=request.getContextPath()%>/servleti/author/<%=providedNick+"/"+entry.getId()%>"><%=entry.getTitle()%>
                    </a>
                </li>
                <%}%>
            </ul>
            </p>
            <%if (currentNick != null && currentNick.equals(providedNick)) {%>
            <br><p><a href=" <%=request.getContextPath()%>/servleti/author/<%=providedNick%>/new">New Entry</a><br></p>
            <%}%>
            </p>
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