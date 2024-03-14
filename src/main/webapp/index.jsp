<%@ page import="com.tictactoe.Sign" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <link href="static/main.css" rel="stylesheet">
    <script src="<c:url value="/static/jquery-3.6.0.min.js"/>"></script>
    <title>Tic-Tac-Toe</title>
</head>
<body>
<h1>Tic-Tac-Toe</h1>
<table>
    <tr>
        <td onclick="window.location='/logic?click=0'">${sessionScope.data.get(0).getSign()}</td>
        <td onclick="window.location='/logic?click=1'">${sessionScope.data.get(1).getSign()}</td>
        <td onclick="window.location='/logic?click=2'">${sessionScope.data.get(2).getSign()}</td>
    </tr>
    <tr>
        <td onclick="window.location='/logic?click=3'">${sessionScope.data.get(3).getSign()}</td>
        <td onclick="window.location='/logic?click=4'">${sessionScope.data.get(4).getSign()}</td>
        <td onclick="window.location='/logic?click=5'">${sessionScope.data.get(5).getSign()}</td>
    </tr>
    <tr>
        <td onclick="window.location='/logic?click=6'">${sessionScope.data.get(6).getSign()}</td>
        <td onclick="window.location='/logic?click=7'">${sessionScope.data.get(7).getSign()}</td>
        <td onclick="window.location='/logic?click=8'">${sessionScope.data.get(8).getSign()}</td>
    </tr>
</table>
<hr/>
<c:if test="${sessionScope.draw}">
    <h1>IT'S A DRAW!</h1>
    <br>
    <button onclick="restart()">Start again</button>
</c:if>
<c:if test="${Sign.CROSS == sessionScope.winner}">
    <h1>CROSSES WIN!</h1>
    <button onclick="restart()">Start again</button>
</c:if>
<c:if test="${Sign.NOUGHT == sessionScope.winner}">
    <h1>NOUGHTS WIN!</h1>
    <button onclick="restart()">Start again</button>
</c:if>
<script>
    function restart() {
        $.ajax({
            url: '/restart',
            type: 'POST',
            contentType: 'application/json;charset=UTF-8',
            async: false,
            success: function () {
                location.reload();
            }
        });
    }
</script>

</body>
</html>