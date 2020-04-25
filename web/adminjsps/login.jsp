<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>管理员登录页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	  <link href="admin/css/bootstrap/bootstrap.min.css" type="text/css" rel="stylesheet">
	  <link href="admin/css/bootstrap/bootstrap-theme.min.css" type="text/css" rel="stylesheet">
	  <script src="admin/js/bootstrap/bootstrap.min.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript">
		function checkForm() {
			if(!$("#adminname").val()) {
				alert("管理员名称不能为空！");
				return false;
			}
			if(!$("#adminpwd").val()) {
				alert("管理员密码不能为空！");
				return false;
			}
			if (false){
			    alert(${msg});
			}
			return true;
		}
	</script>

  </head>
  
  <body>

	<h1>管理员登录页面</h1>
	<hr/>

  <p style="font-weight: 900; color: red">${msg }</p>
	<form  action="<c:url value='/adminServlet'/>" method="post" onsubmit="return checkForm()" target="_top">
	<input type="hidden" name="method" value="login"/>
	<div class="form-group">
		<label for="adminname">管理员用户名</label>
		<input  style="width: auto" type="text" name="adminname" class="form-control" value="${adminname}" id="adminname" placeholder="请输入用户名">
	</div>
	<div class="form-group">
		<label for="adminpwd">密码</label>
		<input style="width: auto" type="password" name="adminpwd" value="${adminpwd}" class="form-control" id="adminpwd" placeholder="请输入密码">
	</div>
	<button type="submit" class="btn btn-default">登录</button>

	</form>

  </body>
</html>
