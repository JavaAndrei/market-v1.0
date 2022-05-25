<%@ page import="java.util.List" %>
<%@ page import="com.webapp.market.dao.entities.User" %>
<%@ page import="com.webapp.market.dao.entities.Role" %>
<%@ page import="com.webapp.market.dao.DAOFactory" %>
<%@ page import="java.util.concurrent.atomic.AtomicReference" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users list</title>
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
<div class="w3-card-4">
    <div class="w3-container w3-center w3-light-blue">
        <h2>Editing by ID</h2>
    </div>
    <form method="post" class="w3-selection w3-light-grey w3-padding" action="./admin">
        <table style="width:100%"><tr>
            <%
                User user = (User) request.getAttribute("user");
                @SuppressWarnings("unchecked")
                AtomicReference<DAOFactory> dao = (AtomicReference<DAOFactory>) request.getServletContext().getAttribute("dao");
                if (user == null) {
                    out.println("<td style=\"width:10%\"><label>id:\n" +
                            "                <input type=\"text\" name=\"id\" class=\"w3-input w3-animate-input w3-border w3-round-large\"><br />\n" +
                            "            </label></td>\n" +
                            "            <td><label>Login:\n" +
                            "                <input type=\"text\" name=\"newLogin\" class=\"w3-input w3-animate-input w3-border w3-round-large\"><br />\n" +
                            "            </label></td>\n" +
                            "            <td> <label>Password:\n" +
                            "                <input type=\"text\" name=\"newPassword\" class=\"w3-input w3-animate-input w3-border w3-round-large\"><br />\n" +
                            "            </label></td>\n" +
                            "            <td><label>Role:\n" +
                            "                <select type=\"text\" name=\"newRole\" class=\"w3-input w3-animate-input w3-border w3-round-large\">");
                    for (Role role : dao.get().getRoleDAO().selectRoleTO(new Role())) {
                        out.println("<option value=\"" + role.getName() + "\">" + role.getName() + "</option>");
                    }
                    out.println("                </select><br />\n" +
                            "            </label></td>");
                } else {
                    out.println("<td style=\"width:10%\"><label>id:\n" +
                            "                <input type=\"text\" name=\"id\" value=\"" + user.getId() + "\" class=\"w3-input w3-animate-input w3-border w3-round-large\"><br />\n" +
                            "            </label></td>\n" +
                            "            <td><label>Login:\n" +
                            "                <input type=\"text\" name=\"newLogin\" value=\"" + user.getLogin() + "\" class=\"w3-input w3-animate-input w3-border w3-round-large\"><br />\n" +
                            "            </label></td>\n" +
                            "            <td> <label>Password:\n" +
                            "                <input type=\"text\" name=\"newPassword\" value=\"" + user.getPassword() + "\" class=\"w3-input w3-animate-input w3-border w3-round-large\"><br />\n" +
                            "            </label></td>\n" +
                            "            <td><label>Role:\n" +
                            "                <select type=\"text\" name=\"newRole\" class=\"w3-input w3-animate-input w3-border w3-round-large\">" +
                            "                    <option value=\"" + user.getRole().getName() + "\">" + user.getRole().getName() + "</option>");

                    for (Role role : dao.get().getRoleDAO().selectRoleTO(new Role())) {
                        if (!role.getName().equals(user.getRole().getName())) {
                            out.println("<option value=\"" + role.getName() + "\">" + role.getName() + "</option>");
                        }
                    }
                    out.println(        "                </select><br />\n" +
                            "            </label></td>");
                }
            %>
        </tr></table>
        <button type="submit" name="action" value="create" class="w3-btn w3-light-blue w3-round-large w3-margin-bottom">Create</button>
        <button type="submit" name="action" value="read" class="w3-btn w3-light-blue w3-round-large w3-margin-bottom">Read</button>
        <button type="submit" name="action" value="update" class="w3-btn w3-light-blue w3-round-large w3-margin-bottom">Update</button>
        <button type="submit" name="action" value="delete" class="w3-btn w3-light-blue w3-round-large w3-margin-bottom">Delete</button>
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
<div class="w3-container w3-center w3-margin-bottom w3-padding">
    <div class="w3-card-4">
        <div class="w3-container w3-light-blue">
            <h2>Users</h2>
        </div>
        <%
            List<User> users = (List<User>) request.getAttribute("userNames");
            
            if (users != null && !users.isEmpty()) {
                out.println("<table style=\"width:100%\">");
                out.println("<colgroup>\n" +
                                "<col style=\"background-color:#C3C3C3\">\n" +
                                "<col style=\"background-color:#D3D3D3\">\n" +
                                "<col style=\"background-color:#C3C3C3\">\n" +
                                "<col style=\"background-color:#D3D3D3\">\n" +
                            "</colgroup>");
                out.println("<tr>" +
                        "<th>id</th>" +
                        "<th>login</th>" +
                        "<th>password</th>" +
                        "<th>role</th></tr>");
                for (User u : users) {
                    out.println("<tr class=\"w3-hover-sand\">" +
                            "<td>" + u.getId() + "</td>" +
                            "<td>" + u.getLogin() + "</td>" +
                            "<td>" + u.getPassword() + "</td>" +
                            "<td>" + u.getRole().getName() + "</td></tr>");
                }
                out.println("</table>");

            } else out.println("<div class=\"w3-panel w3-red w3-display-container w3-card-4 w3-round\">\n"
                    +
                    "   <span onclick=\"this.parentElement.style.display='none'\"\n" +
                    "   class=\"w3-button w3-margin-right w3-display-right w3-round-large w3-hover-red w3-border w3-border-red w3-hover-border-grey\">×</span>\n" +
                    "   <h5>There are no users yet!</h5>\n" +
                    "</div>");
        %>
    </div>
</div>
</body>
</html>