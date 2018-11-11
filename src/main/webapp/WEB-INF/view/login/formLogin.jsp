<%--
  Created by IntelliJ IDEA.
  User: lizhihui
  Date: 2018/11/10
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>表单登录</title>
</head>
<body>
<div>
    <form action="/login/form" method="post" id="authorizeForm">
        <input type="hidden" name="isLogin" value="1">
        <input type="text" name="username" value="" />
        <input type="text" name="password" value=""/>
        <input type="checkbox" name="rememberMe" value="1">
        <input type="submit" value="登录"/>
    </form>
</div>
</body>
</html>
