<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>图片水印DEMO</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
</head>
<body>
	<h4>图片信息</h4>
	<hr />
	<table width="100%">
		<s:iterator value="picInfos">
			<tr>
				<td width="50%"><img
					src='${pageContext.request.contextPath}<s:property value="imgURL"/>'
					height="350" /></td>
				<td width="50%"><img
					src='${pageContext.request.contextPath}<s:property value="markImgURL"/>'
					height="350" />
				</td>
			</tr>
		</s:iterator>
	</table>
	<hr />
	<a href="${pageContext.request.contextPath}">返回</a>
	<hr />
</body>
</html>