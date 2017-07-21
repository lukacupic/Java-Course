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
<head>
    <style type="text/css">
        table.rez td {
            text-align: center;
        }
    </style>
</head>
<body bgcolor=<%=color%>>

<h1>Rezultati glasanja</h1>
<p>Ovo su rezultati glasanja.</p>
<table border="1" cellspacing="0" class="rez">
    <thead>
    <tr>
        <th>Bend</th>
        <th>Broj glasova</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="score" items="${scores}">
        <tr>
            <td>${sessionScope.get("bands").get(score.getID()-1).getName()}</td>
            <td>${score.getVotes()}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h2>Grafički prikaz rezultata</h2>
<img alt="Pie-chart" src="<%=request.getContextPath()%>/glasanje-grafika" width="400" height="400"/>

<h2>Rezultati u XLS formatu</h2>
<p>Rezultati u XLS formatu dostupni su <a href="<%=request.getContextPath()%>/glasanje-xls">ovdje</a></p>

<h2>Razno</h2>
<p>Primjeri pjesama pobjedničkih bendova:</p>

<ul>
    <c:forEach var="winner" items="${winners}">
        <li><a href=${winner.getSong()} target="_blank">${winner.getName()}</a></li>
    </c:forEach>
</ul>
</body>
</html>