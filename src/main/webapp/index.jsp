<%@ page import="com.webapp.market.json.OrderBook" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.webapp.market.json.TargetPrice" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Trading bot</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>

<body class="w3-light-grey">
<%
    if (request.getSession().getAttribute("login") == null) {
        out.print("<div class=\"w3-container w3-grey w3-opacity w3-right-align w3-padding\">\n" +
                "        <button class=\"w3-btn w3-round-large\" onclick=\"location.href='./registration'\">Registration</button>\n" +
                "        <button class=\"w3-btn w3-round-large\" onclick=\"location.href='./login'\">Login</button>\n" +
                "    </div>");
    } else {
        out.print("<div class=\"w3-container w3-grey w3-opacity w3-right-align w3-padding\">\n" +
                "        <button class=\"w3-btn w3-round-large\" onclick=\"location.href='./bot_keys'\">bot</button>\n" +
                "        <button class=\"w3-btn w3-round-large\" onclick=\"location.href='./logout'\" >Logout</button>\n" +
                "    </div>");
    }
%>
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

<div class="w3-container">
    <iframe src="./service"></iframe>
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
