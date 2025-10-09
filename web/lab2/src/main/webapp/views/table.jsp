<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="https://lysmux.dev/jsp/tags/table" prefix="table" %>

<%
    DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("HH:mm:ss")
            .withZone(ZoneId.of("Europe/Moscow"));
    request.setAttribute("dateTimeFormatter", formatter);
%>

<html>
<head>
    <title>Результаты</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="<c:url value='/dist/main.css'/>">
</head>
<body>
<table:dataTable
        items="${applicationScope.checks}"
        var="check"
        cssClass="table-wrapper"
        paginated="true"
        itemsPerPage="8"
>
    <table:column header="Время" sortable="true">
        ${check.getTime().format(dateTimeFormatter)}
    </table:column>
    <table:column header="X" property="x" sortable="true"/>
    <table:column header="Y" property="y" sortable="true"/>
    <table:column header="R" property="r" sortable="true"/>
    <table:column header="Попадание" sortable="true">
        ${check.isContains() ? "Да" : "Нет"}
    </table:column>
    <table:column header="Время выполнения" sortable="true">${check.getExecutionTime()} нс</table:column>
</table:dataTable>
</body>
</html>