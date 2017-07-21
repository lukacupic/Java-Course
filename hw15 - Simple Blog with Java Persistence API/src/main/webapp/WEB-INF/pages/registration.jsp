<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
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
<div class="container">
    <form action="<%=request.getContextPath()%>/servleti/do_register" method="post">
        First Name: <input type="text" name="firstName"><br>
        Last Name: <input type="text" name="lastName"><br>
        E-mail: <input type="email" name="email"><br>
        Nick: <input type="text" name="nick"><br>
        Password: <input type="password" name="password"><br>
        <input type="submit" value="Register">
    </form>
</div>
<% if (session.getAttribute("regError") != null) { %>
<i><%=session.getAttribute("regError")%>
</i>
<% }%>
</body>
</html>
