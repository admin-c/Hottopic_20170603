<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ page import ="java.util.*" %>
     <%@ page import ="com.javy.entity.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%  HashMap<Integer, Model> models = (HashMap<Integer, Model>)session.getAttribute("models");%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>
	<div class="container" style="font-size:15px">
		<div class="row">
			<form action="mergeevent.do" method="post" class="form form-horizontal">
				<div class="col-lg-12">	请选择事件类型：</div>
				<div class="col-lg-2">
					<input type="radio" id="eventpick2" name="eventpick2" value="0" checked >事件库已有事件
				</div>
				<div class="col-lg-6">
					<select name = "modelselect" class="form form-control">
					<option value="0">请选择</option>
					<%
					for(int id : models.keySet()){
						%>
					<option value="<%=id%>"><%=models.get(id).getName()%></option>
					<%} %>
					</select>
				</div>
				<div class="col-lg-12">	&nbsp;</div>
				<div class="col-lg-2">
					<input type="radio" id="eventpick2" name="eventpick2" value="1">新事件
				</div>
				<div class="col-lg-6">
					<input type="text" id="eventname" name="eventname" class="form form-control" placeholder = "请输入新事件名称">
				</div>
				<div class="col-lg-12">	&nbsp;</div>	
				<div class="col-lg-4">
      				<div class="form-group"> 
						<input type="submit" value="确定" class="btn btn-block btn-primary">
					</div>	
   				</div>
			</form>
		</div>
	</div>
		
</body>
</html>