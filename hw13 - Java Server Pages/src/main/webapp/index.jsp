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
<p>
    <a href="<%=request.getContextPath()%>/index">Background Color Chooser</a>
</p>
<p>
<form action="<%=request.getContextPath()%>/trigonometric" method="GET">
    Initial angle:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
    Final angle:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
    <input type="submit" value="Table it!"><input type="reset" value="Reset">
</form>
</p>
<p>
    <a href="<%=request.getContextPath()%>/stories/funny.jsp">Make me laugh!</a>
</p>
<p>
    <a href="<%=request.getContextPath()%>/powers?a=1&b=100&n=3">Create a new Excel table!</a>
</p>
<p>
    <a href="<%=request.getContextPath()%>/appinfo.jsp">Tell me the time, doc!</a>
</p>
</body>
</html>