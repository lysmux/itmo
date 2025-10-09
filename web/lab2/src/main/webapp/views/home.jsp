<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="https://lysmux.dev/jsp/tags/table" prefix="table" %>

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
        <div id="toast-container" class="toast-container"></div>

        <div class="block">
            <jsp:include page="components/plot.jsp" />
        </div>
        <div class="block">
            <jsp:include page="components/form.jsp"/>
        </div>
        <div class="block wide" style="height: 100%">
            <c:url var="tableUrl" value="/views/table.jsp"/>
            <table:iframeWrap tableUrl="${tableUrl}" cssClass="table-frame" id="results-iframe"/>
        </div>
    </main>
</div>
</body>
</html>
