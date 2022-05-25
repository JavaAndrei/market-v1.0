<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Trading bot</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>

<body class="w3-light-grey">
<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">
    <button class="w3-btn w3-round-large" onclick="location.href='./registration'">Registration</button>
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

<div class="w3-card-4">
    <div class="w3-container w3-center w3-green">
        <h2>Authentication</h2>
    </div>
    <form method="post" class="w3-selection w3-light-grey w3-padding">
        <label>Login:
            <input type="text" name="login" class="w3-input w3-animate-input w3-border w3-round-large" style="width: 30%"><br />
        </label>
        <label>Password:
            <input type="password" name="password" class="w3-input w3-animate-input w3-border w3-round-large" style="width: 30%"><br />
        </label>
        <button type="submit" class="w3-btn w3-green w3-round-large w3-margin-bottom">Login</button>
        <%
            String errorMessage = (String) request.getAttribute("error");
            if (errorMessage != null) {
                out.print("<q style=\"color:red;\">" + errorMessage + "</q>");
            }
        %>
    </form>
</div>

<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">
    <button class="w3-btn w3-round-large" onclick="location.href='./'">Back to main</button>
</div>
</body>
</html>
