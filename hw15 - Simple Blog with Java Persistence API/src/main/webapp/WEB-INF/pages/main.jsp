<%@ page import="hr.fer.zemris.java.hw15.dao.DAOProvider" %>
<%@ page import="hr.fer.zemris.java.hw15.model.BlogUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ahoy!</title>
    <style type="text/css">
        .container {
            width: 200px;
            clear: both;
        }

        .container input {
            width: 100%;
            clear: both;
        }

    </style>
</head>
<body>

<table width="100%">
    <tr>
        <td valign="top">
            <h2>Here's a list of registered authors:</h2>

            <%if (DAOProvider.getDao().getUsers().size() == 0) {%>
            <i>No registered users...</i>
            <%}%>
            <p>
            <ul>
                <%for (BlogUser user : DAOProvider.getDao().getUsers()) {%>
                <%String fullName = user.getFirstName() + " " + user.getLastName();%>
                <li><a href=" <%=request.getContextPath()%>/servleti/author/<%=user.getNick()%>"><%=fullName%>
                </a></li>
                <%}%>
            </ul>
            </p>
        </td>
        <td valign="top">
            <div align="right">
                <p>
                    <% if (session.getAttribute("current.user.id") != null) { %>
                    Hello, <i><%=session.getAttribute("current.user.fn")%> <%=session.getAttribute("current.user.ln")%>
                </i><br>
                    <a href=<%=request.getContextPath()%>/servleti/logout>Logout</a>
                    <% } else { %>
                    You are currently not logged in!
                    <% } %>
                </p>

                <% if (session.getAttribute("current.user.id") == null) { %>
                <div class="container" align="left">
                    <form action="<%=request.getContextPath()%>/servleti/do_login" method="post">

                        <% if (session.getAttribute("current.user.nick") == null) { %>
                        Nick: <input type="text" name="nick"><br>
                        <% } else { %>
                        Nick: <input type="text" name="nick" value=<%=session.getAttribute("current.user.nick")%>><br>
                        <% } %>

                        Password: <input type="password" name="password"><br>
                        <input type="submit" value="Login">
                    </form>


                    <% if (session.getAttribute("matchError") != null) { %>
                    <i><%=session.getAttribute("matchError")%>
                    </i>
                    <% }%>
                    <% if (session.getAttribute("nickError") != null) { %>
                    <i><%=session.getAttribute("nickError")%>
                    </i>
                    <% }%>
                    <p>
                        New user? Register <a href=" <%=request.getContextPath()%>/servleti/register">here</a>
                    </p>
                </div>
                <% } %>
            </div>
        </td>
    </tr>
</table>
</body>
</html>