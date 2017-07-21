<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%!
    private String getTimeDifference(long beginning) {
        long millis = System.currentTimeMillis() - beginning;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return String.format("%02d days %02d hours %02d minutes %02d seconds", days, hours % 24, minutes % 60, seconds % 60);
    }
%>

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
<h2>Great Scott, look at the time!</h2>
<%=getTimeDifference((long) session.getServletContext().getAttribute("principium"))%>
</body>
</html>