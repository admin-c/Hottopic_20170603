<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>选择比较方式</title>
    <script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>
    <div class="container" style="font-size:15px;margin-top:100px;">
            <div  class="btn btn-block btn-primary" onclick="router(0)">有时间段比较</div>
            <div style="margin-top:15px;"></div>
            <div class="btn btn-block btn-primary" onclick="router(1)">无时间段比较</div>
    </div>
<script>
    function router(i){
        if(i==0){
            window.location.href = "DatePickTime.jsp";
        }else if(i==1){
            window.location.href = "DatePick.jsp";
        }
    }
</script>
</body>
</html>
