<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header>
    <h1 class="title">Разыграев Кирилл Сергеевич</h1>
    <h3 class="subtitle">Группа: P3215</h3>
    <h3 class="subtitle">Вариант: 467213</h3>

    <div class="theme-switcher">
        <div class="theme-radio">
            <input type="radio" id="light-theme" name="theme" value="light"/>
            <label for="light-theme">
                <img src="<c:url value="/assets/sun.svg" />" class="theme-icon"  alt="light-theme"/>
            </label>
        </div>
        <div class="theme-radio">
            <input type="radio" id="dark-theme" name="theme" value="dark"/>
            <label for="dark-theme">
                <img src="<c:url value="/assets/moon.svg" />" class="theme-icon"  alt="dark-theme"/>
            </label>
        </div>
    </div>
</header>