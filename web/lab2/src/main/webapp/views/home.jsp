<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html class="theme-dark">
<head>
    <title>2 лаба по вебу</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" type="text/css" href="<c:url value='/dist/main.css'/>">
    <script defer src="<c:url value='/dist/main.js'/>"></script>
</head>
<body>
<div class="container">
    <%@ include file="components/header.jsp" %>

    <main>
        <div class="block">
            <jsp:include page="components/plot.jsp" />
        </div>
        <div class="block">
            <jsp:include page="components/form.jsp"/>
        </div>
        <div class="block wide" style="height: 100%">
            <jsp:include page="components/table.jsp"/>
        </div>
    </main>
</div>
</body>
</html>
