<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new user</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>

<body class="w3-light-grey">
<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">
    <button class="w3-btn w3-round-large" onclick="location.href='./logout'" >Logout</button>
</div>
<div class="w3-container w3-blue-grey w3-opacity w3-right-align">
    <div class="w3-left"><h1><%
        if (request.getSession().getAttribute("login") == null) {
            out.print("Register or Login to start");
        } else {
            out.print("Hello " + request.getSession().getAttribute("login"));
        }
    %></h1></div>
    <div class="w3-right"><h1>Trading bot</h1></div>
</div>

<div class="w3-container w3-padding">
    <%
        if (request.getAttribute("userName") != null) {
            out.println("<div class=\"w3-panel w3-green w3-display-container w3-card-4 w3-round\">\n" +
                    "   <span onclick=\"this.parentElement.style.display='none'\"\n" +
                    "   class=\"w3-button w3-margin-right w3-display-right w3-round-large w3-hover-green w3-border w3-border-green w3-hover-border-grey\">×</span>\n" +
                    "   <h5>Bot '" + request.getAttribute("userName") + "' started!</h5>\n" +
                    "</div>");
        }
    %>
    <div class="w3-card-4">
        <div class="w3-container w3-center w3-green">
            <h2>The bot is running.</h2>
        </div>
        <form method="post" class="w3-selection w3-light-grey w3-padding" >
            <button type="submit" name="action" value="finish" class="w3-btn w3-green w3-round-large w3-margin-bottom">Finish trading</button>
        </form>
    </div>
</div>
<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">
    <div class="w3-left">
        <form action="./bot">
            <input type="radio" id="html" name="strategy" value="agressive_trading" >
            <label for="html">agressive trading</label>
            <input type="radio" id="css" name="strategy" value="safe_trading" checked>
            <label for="css">safe trading</label>
            <input type="radio" id="javascript" name="strategy" value="close_all_position" >
            <label for="javascript">close all position</label>
        </form>
    </div>
    <div class="w3-right">
        <button class="w3-btn w3-round-large" onclick="location.href='./'">Back to main</button>
    </div>
</div>
<div class="w3-container">
    <iframe src="./trading"></iframe>
    <style>
        iframe {
            position: absolute;
            width: 100%;
            height: 100%;
        }
    </style>
</div>
</body>
</html>