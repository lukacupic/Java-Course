<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
<h1>Glasanje za omiljeni bend:</h1>
<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!</p>
<ol>
    <c:forEach var="band" items="${bands}">
        <li><a href="<%=request.getContextPath()%>/glasanje-glasaj?id=${band.getID()}">${band.getName()}</a></li>
    </c:forEach>
</ol>
</body>
</html>