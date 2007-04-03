<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Cache-Control" content="no-cache" />
		<meta http-equiv="Window-target" content="_top" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><bean:message key="application.title" /></title>
		<meta name="Description" content="<bean:message key="application.title" />" />
		<meta name="Author" content="samaxes" />
	</head>
	
	<body>
		<p><bean:message key="view.report.subject" /></p>
		${reportHTML}
	</body>
</html>
