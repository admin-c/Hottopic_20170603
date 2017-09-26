<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% String message = (String)session.getAttribute("message"); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/bootstrap.min.css">
<title>Insert title here</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h2><%=message %></h2>
			</div>
			<form action="backindex.do" method="post">
				<div class="col-lg-4">
      				<div class="form-group"> 
						<input type="submit" value="返回主页" class="btn btn-block btn-primary">
					</div>	
   				</div>
			</form>	
			
		</div>
	</div>
</body>
</html>