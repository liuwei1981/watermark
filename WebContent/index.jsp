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
	<h4>上传图片</h4>
	<hr />
	<form action="${pageContext.request.contextPath}/watermark.action"
		enctype="multipart/form-data" method="post">
		
		<input type="file" name="image"> <br/>
		<input type="file" name="image"> <br/>
		<input type="file" name="image"> <br/>
		<input type="file" name="image"> <br/>
		<input type="file" name="image"> <br/>
		
		<input type="submit"  value=" 上  传  " />
		
	</form>
	<hr />
</body>
</html>