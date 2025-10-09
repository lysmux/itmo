<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form class="form" id="check-form">
  <div class="loader hidden">
    <div class="spin"></div>
  </div>
  <div class="block-container">
    <div class="block">
      <h1>X</h1>
      <div class="input-container">
        <c:forEach var="i" begin="0" end="8">
          <c:set var="x" value="${i - 3}" />
          <div class="checkbox">
            <input name="x" id="checkbox-x-${x}" type="checkbox" value="${x}">
            <label for="checkbox-x-${x}">${x}</label>
          </div>
        </c:forEach>
      </div>
    </div>
    <div class="block">
      <h1>R</h1>
      <div class="input-container">
        <c:forEach var="r" begin="1" end="5">
          <div class="checkbox">
            <input name="r" id="checkbox-r-${r}" type="checkbox" value="${r}">
            <label for="checkbox-r-${r}">${r}</label>
          </div>
        </c:forEach>
      </div>
    </div>
    <div class="block wide">
      <h1>Y</h1>
      <input name="y" class="input" type="text" maxlength="5" placeholder="Введите Y">
    </div>
  </div>

  <div class="button-container">
    <div class="check-container">
      <button class="button action" id="check-btn">Проверить</button>
      <div class="errors-block" id="errors-block"></div>
    </div>
    <button class="button danger" id="clear-btn">Очистить результаты</button>
  </div>
</form>