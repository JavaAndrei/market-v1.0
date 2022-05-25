<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="com.webapp.market.json.*" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.webapp.market.markets.Market" %>
<%@ page import="com.webapp.market.markets.ConnectionPool" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users list</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>

<body class="w3-light-grey">
    <div class="w3-container w3-left w3-margin-bottom w3-padding">
        <%
            response.setContentType("text/html");
            response.setIntHeader("Refresh", 5);

            Market market = ConnectionPool.getMarket((String) request.getSession().getAttribute("login"));
        %>

        <div class="w3-container w3-light-blue w3-tiny"><h1 style="font-size:4vw">
            <%
                Date date=new Date(TargetPrice.getTime());
                out.println(date  + "<br/>");
            %></h1>
        </div>
        <div class="w3-container w3-light-blue w3-tiny"><h1 style="font-size:3vw">
            <%
                out.println("price BTC: " + TargetPrice.getValue() + " USDT<br/>");
            %></h1>
        </div>
        <div class="w3-container w3-light-blue w3-tiny"><h1 style="font-size:2vw">
            <%
                Map<String, Object> walletUSDT = market.getWalletUSDT();
                out.println(String.format("balance: %s USDT; used: %s USDT; free: %s USDT",
                        (walletUSDT.containsKey("balance") ? (String) walletUSDT.get("balance") : "0.0"),
                        (walletUSDT.containsKey("used") ? (String) walletUSDT.get("used") : "0.0"),
                        (walletUSDT.containsKey("free") ? (String) walletUSDT.get("free") : "0.0")));
            %></h1>
        </div>
        <div class="w3-container w3-light-blue w3-tiny"><h1 style="font-size:2vw">
            <%
                Map<String, Object> position = market.getPosition();
                out.println(String.format("type: %s; quantity: %s BTC; price: %s USDT; r_pnl: %s; unr_pnl: %s",
                        (position.containsKey("type") ? (String) position.get("type") : "0.0"),
                        (position.containsKey("quantity") ? (String) position.get("quantity") : "0.0"),
                        (position.containsKey("base_price") ? (String) position.get("base_price") : "0.0"),
                        (position.containsKey("realized_pnl") ? (String) position.get("realized_pnl") : "0.0"),
                        (position.containsKey("unrealized_pnl") ? (String) position.get("unrealized_pnl") : "0.0")));
            %></h1>
        </div>
        <div class="w3-container w3-red w3-tiny"><h1 style="font-size:20px">
            <%
                Map<String, Object> limitOrderSell = market.getLimitOrderSell();
                out.println(String.format("Short limit order id: %s; price: %s USDT; quantity: %s BTC; leverage: %s; status: %s",
                        (limitOrderSell.containsKey("order_id") ? (String) limitOrderSell.get("order_id") : "0.0"),
                        (limitOrderSell.containsKey("price") ? (String) limitOrderSell.get("price") : "0.0"),
                        (limitOrderSell.containsKey("quantity") ? (String) limitOrderSell.get("quantity") : "0.0"),
                        (limitOrderSell.containsKey("leverage") ? (String) limitOrderSell.get("leverage") : "0.0"),
                        (limitOrderSell.containsKey("status") ? (String) limitOrderSell.get("status") : "0.0")));
            %></h1>
        </div>

        <div class="w3-container w3-green w3-tiny"><h1 style="font-size:20px">
            <%
                Map<String, Object> limitOrderBuy = market.getLimitOrderBuy();
                out.println(String.format("Long limit order id: %s; price: %s USDT; quantity: %s BTC; leverage: %s; status: %s",
                        (limitOrderBuy.containsKey("order_id") ? (String) limitOrderBuy.get("order_id") : "0.0"),
                        (limitOrderBuy.containsKey("price") ? (String) limitOrderBuy.get("price") : "0.0"),
                        (limitOrderBuy.containsKey("quantity") ? (String) limitOrderBuy.get("quantity") : "0.0"),
                        (limitOrderBuy.containsKey("leverage") ? (String) limitOrderBuy.get("leverage") : "0.0"),
                        (limitOrderBuy.containsKey("status") ? (String) limitOrderBuy.get("status") : "0.0")));
            %></h1>
        </div>
    </div>

</body>
</html>