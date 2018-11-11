<%--
  Created by IntelliJ IDEA.
  User: lizhihui
  Date: 2018/11/6
  Time: 18:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录跳转中</title>
</head>
<body>
    <div style="display:none;">
        <form action="/login/wechat" method="post" id="authorizeForm">
        <input type="text" value="${code}" name="oauthCode"/>
        <input type="text" name="isLogin" value="1"/>
        <input type="submit" value="提交"/>
        </form>
    </div>
</body>
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
    $(function (){
        $("#authorizeForm").submit();
    });
</script>
</html>
