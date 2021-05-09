<%@ page import="com.itranswarp.learnjava.bean.*"%>
<%
    User user = (User) request.getAttribute("user");
%>
<html>
<head>
    <title>Profile</title>
</head>
<body>
    <p><a href="/">Index</a> - Profile</p>
    <form action="/user/profile" method="post">
        <p>Name: <input name="name"></p>
        <p><button type="submit">Update</button></p>
    </form>
</body>
</html>
