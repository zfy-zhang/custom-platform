<%@ page isELIgnored="false" %>
<html>
<head>
	<jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
	<title>Title</title>
</head>
<body>
<table>
	<thead>
	<tr>
		<td>用户名</td>
		<td>邮箱</td>
		<td>电话</td>
		<td>密码</td>
	</tr>
	</thead>
	<tbody>
	<c:forEach items ="${users}" var = "user">
		<tr>
			<td>${user.name}</td>
			<td>${user.email}</td>
			<td>${user.phoneNumber}</td>
			<td>${user.password}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>

</body>
</html>
