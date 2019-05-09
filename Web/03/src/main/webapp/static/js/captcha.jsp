<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Codeforces</title>
</head>

<body>
<main>
    <div class="captcha-image">
        <%
            String s = (String) request.getSession().getAttribute("captcha_img");
            request.getSession().removeAttribute("captcha_img");
        %>
        <img src="data:image/png;base64,<%=s%>">
    </div>
    <div class="answer-form">
        <form action="" method="get">
            <label for="answer-form__text">Enter your answer:</label>
            <input name="answer" id="answer-form__text">
        </form>
    </div>
</main>
</body>
</html>
