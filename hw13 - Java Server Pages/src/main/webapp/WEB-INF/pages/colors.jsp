<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
<a href="<%=request.getContextPath()%>/setcolor?color=white">WHITE</a>
<a href="<%=request.getContextPath()%>/setcolor?color=red">RED</a>
<a href="<%=request.getContextPath()%>/setcolor?color=green">GREEN</a>
<a href="<%=request.getContextPath()%>/setcolor?color=cyan">CYAN</a>
</body>
</html>