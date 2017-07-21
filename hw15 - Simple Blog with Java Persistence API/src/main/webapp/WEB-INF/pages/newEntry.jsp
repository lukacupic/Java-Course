<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Entry</title>
</head>
<body>
<table width="100%">
    <tr>
        <td>
            <a href=" <%=request.getContextPath()%>/servleti/main">Home</a><br><br>
            <form action="<%=request.getContextPath()%>/servleti/submitEntry" id="entryForm">
                Title: <input type="text" name="title"><br>

                <textarea rows="4" cols="50" name="text"></textarea><br>
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
