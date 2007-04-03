<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

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
<h1><bean:message key="application.title" /></h1>

<h2><bean:message key="home.choice" /></h2>

<table cellpadding="2" cellspacing="0">
    <tbody>
        <tr>
            <td colspan="2"><a href="run?ReportName=testWebReport.rptdesign"><bean:message
                key="home.choice.servlet" /></a></td>
        </tr>
        <tr>
            <td><a href="htmlReport.do?ReportName=testWebReport.rptdesign"><bean:message
                key="home.choice.action.html" /></a></td>
            <td><bean:message key="home.choice.action.struts" /></td>
        </tr>
        <tr>
            <td><a href="pdfReport.do?ReportName=testWebReport.rptdesign"><bean:message
                key="home.choice.action.pdf" /></a></td>
            <td><bean:message key="home.choice.action.struts" /></td>
        </tr>
        <tr>
            <td><a href="xlsReport.do?ReportName=testWebReport.rptdesign"><bean:message
                key="home.choice.action.xls" /></a></td>
            <td><bean:message key="home.choice.action.struts" /></td>
        </tr>
    </tbody>
</table>
</body>
</html>
