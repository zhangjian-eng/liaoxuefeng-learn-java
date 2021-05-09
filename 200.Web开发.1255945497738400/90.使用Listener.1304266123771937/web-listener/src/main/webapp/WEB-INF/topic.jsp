<%@ page import="com.itranswarp.learnjava.bean.*"%>
<%
    User user = (User) request.getAttribute("user");
%>
<html>
<head>
    <title>Profile</title>
</head>
<body>
    <p><a href="/">Index</a> - New Topic</p>
    <form action="/user/topic" method="post">
        <p>Title: <input name="title"></p>
        <p>Content: <textarea name="content"></textarea></p>
        <p><button type="submit">Post</button></p>
    </form>
</body>
</html>
