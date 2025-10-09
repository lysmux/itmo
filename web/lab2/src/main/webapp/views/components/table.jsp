<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="table-container">
    <table id="results">
        <thead>
        <tr>
            <th>Время</th>
            <th>X</th>
            <th>Y</th>
            <th>R</th>
            <th>Попадание</th>
            <th>Время обработки</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${applicationScope.checks}" var="check">
            <c:forEach items="${check.checks}" var="contain">
                <tr>
                    <td>${check.time}</td>
                    <td>${contain.x}</td>
                    <td>${contain.y}</td>
                    <td>${contain.r}</td>
                    <td>${contain.contains}</td>
                    <td>${check.executionTime} нс</td>
                </tr>
            </c:forEach>
        </c:forEach>
        </tbody>
    </table>
</div>