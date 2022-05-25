<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="com.webapp.market.json.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users list</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>

<body class="w3-light-grey">
<table style="width:100%">
    <tr>
        <td><div class="w3-container w3-left w3-margin-bottom w3-padding">
            <%
                response.setContentType("text/html");
                response.setIntHeader("Refresh", 50);
            %>
            <div class="w3-container w3-red w3-tiny">
                <%
                    for (int i = 24; i >= 0; i--) {
                        out.println(String.format("<div style=\"float: left; width: 25%%;\">%.2f</div>", OrderBook.getAsk().get(i).get(0)));
                        out.println(String.format("<div style=\"float: left; width: 30%%;\" align=\"right\">%.8f</div>", OrderBook.getAsk().get(i).get(1)));
                        out.println(String.format("<div align=\"right\">%.8f</div>", OrderBook.getAsk().get(i).get(2)));

                        //out.println(String.format("%.2f    %.8f    %.8f<br/>", OrderBook.getAsk().get(i).get(0), OrderBook.getAsk().get(i).get(1), OrderBook.getAsk().get(i).get(2)));
                    }
                %>
            </div>
            <div class="w3-container w3-light-blue w3-tiny">
                <%
                    Date date=new Date(TargetPrice.getTime());
                    out.println(TargetPrice.getValue() + " USDT time:" + date  + "<br/>");
                %>
            </div>
            <div class="w3-container w3-green w3-tiny">
                <%
                    for (List<Double> bid : OrderBook.getBid()) {
                        out.println(String.format("<div style=\"float: left; width: 25%%;\">%.2f</div>", bid.get(0)));
                        out.println(String.format("<div style=\"float: left; width: 30%%;\" align=\"right\">%.8f</div>", bid.get(1)));
                        out.println(String.format("<div align=\"right\">%.8f</div>", bid.get(2)));

                        //out.println(String.format("%.2f    %.8f    %.8f<br/>", bid.get(0), bid.get(1), bid.get(2)));
                    }
                %>
            </div>
        </div></td>
        <td><div class="w3-container w3-light-blue w3-tiny w3-center">
            <h1 style="font-size:16vw">
            <%
                out.println(TargetPrice.getValue());
            %>
            </h1>
        </div></td>
    </tr>
</table>

</body>
</html>