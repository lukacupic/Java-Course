<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

<%
    String color = "#FFFFFF";

    String attr = (String) session.getAttribute("pickedBgCol");
    if (attr != null) {
        if ("red".equals(attr)) color = "#B20000";
        else if ("green".equals(attr)) color = "#168730";
        else if ("cyan".equals(attr)) color = "#1FC6C6";
    }
%>

<html>
<body bgcolor=<%=color%>>
<table border="1">
    <tr>
        <th>x</th>
        <th>sin(x)</th>
        <th>cos(x)</th>
    </tr>
    <c:forEach var="row" items="${trigResults}">
        <tr>
            <td>${row[0]}</td>
            <td>${row[1]}</td>
            <td>${row[2]}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>