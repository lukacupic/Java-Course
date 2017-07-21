<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>:(</title>
</head>
<body>
<table width="100%">
    <tr>
        <td>
            <a href=" <%=request.getContextPath()%>/servleti/main">Home</a><br>
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
<br><br><br>
<center><i>Whoops, looks like something went wrong... are you sure you're supposed to be here?</i></center>
</body>
</html>