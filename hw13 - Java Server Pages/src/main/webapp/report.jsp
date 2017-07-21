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
<h2>OS usage</h2>
<p>Here are the results of the OS usage in a survey that we completed:</p>
<img src="<%=request.getContextPath()%>/reportImage"/>
</form>
</body>
</html>