<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
			<form action="eventsearch.do" method="post" class="form form-horizontal">
				<div class="col-lg-12">	请选择时间范围：</div>
				<div class="col-lg-12">
					<input type="radio" id="hotpick" name="hotpick" value="4" onclick="changechose()" checked>指定任意时间段(不通过两个时间段比较)得到热点事件
				</div>	
				<div class="col-lg-12">&nbsp;</div>
				<div class="form-group"> 
					<label class="col-lg-3 control-label">请输入你想显示的热点事件条数</label>
   					<div class="col-lg-6">
      					<input type="number" name="number" class="form-control">
   					</div>	
   				</div>	
   				<div class="col-lg-12">&nbsp;</div>
				<div class="form-group"> 
					<label class="col-lg-3 control-label">请选择事件查重算法</label>
   					<div class="col-lg-6">
      					<select name="similar" class="form-control">
      						<option value="1">cos相似度计算</option>
      					</select>
   					</div>	
   				</div>	
   				<div class="col-lg-12">&nbsp;</div>
				<div class="form-group"> 
					<label class="col-lg-3 control-label">请设置相似度阈值</label>
   					<div class="col-lg-6">
      					<input type="text" name="sim" class="form-control">
   					</div>	
   				</div>
   				<div class="col-lg-12">&nbsp;</div>	
				
				<div class="form-group" id="time5"> 
					 					
   					<div class="col-lg-12">&nbsp;</div>	
   					<label class="col-lg-3 control-label">请选择你要查询的热点事件时间段</label>
   					<div class="col-lg-4">
      					<input type="date" name="t51" class="form-control">
   					</div>
   					<div class="col-lg-1" style="text-align:center">
      					-------
   					</div>
   					<div class="col-lg-4">
      					<input type="date" name="t52" class="form-control">
   					</div>
				</div>
				<div class="col-lg-4">&nbsp;</div>	
				<div class="col-lg-4">
      				<div class="form-group"> 
						<input type="submit" value="查询" class="btn btn-block btn-primary">
					</div>	
   				</div>
			</form>
		</div>
	</div>
</body>
</html>