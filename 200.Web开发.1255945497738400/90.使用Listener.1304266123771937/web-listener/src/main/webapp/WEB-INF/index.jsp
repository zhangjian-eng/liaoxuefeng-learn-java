<%@ page import="java.util.*"%>
<%@ page import="com.itranswarp.learnjava.bean.*"%>
<%
    List<Topic> topics = (List<Topic>) request.getAttribute("topics");
%>
<html>
<head>
    <title>Index</title>
</head>
<body>
    <p>Index - <a href="/user/topic">New Topic</a></p>
    <% for (topic : topics) { %>
        <p>Name: <input name="name"></p>
        <p><button type="submit">Update</button></p>
    </form>
    <% } %>
</body>
</html>
