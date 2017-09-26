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
<script type="text/javascript">
function changechose(){
	var radioflag = $("input[name='hotpick']:checked").val();
	var t1 = document.getElementById("time1");
	var t2 = document.getElementById("time2");
	var t3 = document.getElementById("time3");
	var t4 = document.getElementById("time4");
	var t5 = document.getElementById("time5");
	var t6 = document.getElementById("time6");
	if(radioflag == 0){
		t1.removeAttribute("hidden");
		t2.setAttribute("hidden", "true");
		t3.setAttribute("hidden", "true");
		t4.setAttribute("hidden", "true");
		t5.setAttribute("hidden", "true");
		t6.setAttribute("hidden", "true");
	}
	if(radioflag == 1){
		t1.setAttribute("hidden", "true");
		t2.removeAttribute("hidden");
		t3.setAttribute("hidden", "true");
		t4.setAttribute("hidden", "true");
		t5.setAttribute("hidden", "true");
		t6.setAttribute("hidden", "true");
	}
	if(radioflag == 2){
		t1.setAttribute("hidden", "true");
		t2.setAttribute("hidden", "true");
		t3.removeAttribute("hidden");
		t4.setAttribute("hidden", "true");
		t5.setAttribute("hidden", "true");
		t6.setAttribute("hidden", "true");
	}
	if(radioflag == 3){
		t1.setAttribute("hidden", "true");
		t2.setAttribute("hidden", "true");
		t3.setAttribute("hidden", "true");
		t4.removeAttribute("hidden");
		t5.setAttribute("hidden", "true");
		t6.setAttribute("hidden", "true");
	}
	if(radioflag == 4){
		t1.setAttribute("hidden", "true");
		t2.setAttribute("hidden", "true");
		t3.setAttribute("hidden", "true");
		t4.setAttribute("hidden", "true");
		t5.removeAttribute("hidden");
		t6.setAttribute("hidden", "true");
	}
	if(radioflag == 5){
		t1.setAttribute("hidden", "true");
		t2.setAttribute("hidden", "true");
		t3.setAttribute("hidden", "true");
		t4.setAttribute("hidden", "true");
		t5.setAttribute("hidden", "true");
		t6.removeAttribute("hidden");
	}
}
</script>
</head>
<body>
	<div class="container" style="font-size:15px">
		<div class="row">
			<form action="eventsearchtime.do" method="post" class="form form-horizontal">
				<div class="col-lg-12">	请选择你要比较的热点事件时间范围：</div>
				<div class="col-lg-12">
					<input type="radio" id="hotpick" name="hotpick" value="0" checked onclick="changechose()">按天得到热点事件
				</div>
				<div class="col-lg-12">
					<input type="radio" id="hotpick" name="hotpick" value="1" onclick="changechose()">按周得到热点事件
				</div>
				<div class="col-lg-12">
					<input type="radio" id="hotpick" name="hotpick" value="2" onclick="changechose()">按月得到热点事件
				</div>
				<div class="col-lg-12">
					<input type="radio" id="hotpick" name="hotpick" value="3" onclick="changechose()">指定任意时间比较（天对天）得到热点事件
				</div>
				<div class="col-lg-12">
					<input type="radio" id="hotpick" name="hotpick" value="4" onclick="changechose()">指定任意时间段比较（任意一段时间对天）得到热点事件
				</div>
				<div class="col-lg-12">
					<input type="radio" id="hotpick" name="hotpick" value="5" onclick="changechose()">指定任意时间段比较（任意一段时间对任意一段时间）得到热点事件
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
				<div class="form-group" id="time1"> 
					<label class="col-lg-3 control-label">请选择你要查询的热点事件日期</label>
   					<div class="col-lg-6">
      					<input type="date" name="t1" class="form-control" min = "2017-01-02" max = "2017-02-28">
   					</div>
				</div>
				<div class="form-group" id="time2" hidden> 
					<label class="col-lg-3 control-label">请选择你要查询的热点事件周数</label>
   					<div class="col-lg-6">
      					<select name="t2" class="form-control">
							<option value="2017-01-09:2017-01-15">1月9日-1月15日</option>
							<option value="2017-01-16:2017-01-22">1月16日-1月22日</option>
							<option value="2017-01-23:2017-01-29">1月23日-1月29日</option>
							<option value="2017-01-30:2017-02-05">1月30日-2月5日</option>
							<option value="2017-02-06:2017-02-12">2月6日-2月12日</option>
							<option value="2017-02-13:2017-02-19">2月13日-2月19日</option>
							<option value="2017-02-20:2017-02-26">2月20日-2月26日</option>
						</select>
   					</div>
				</div>	
				<div class="form-group" id="time3" hidden> 
					<label class="col-lg-3 control-label">请选择你要查询的热点事件月数</label>
   					<div class="col-lg-6">
      					<select name="t3" class="form-control">
							<option value="1">2月</option>
						</select>
   					</div>
				</div>	
				<div class="form-group" id="time4" hidden> 
					<label class="col-lg-3 control-label">请选择你要对比的热点事件日期</label>
   					<div class="col-lg-6">
      					<input type="date" name="t41" class="form-control" min = "2017-01-01" max = "2017-02-28">
   					</div>
   					
   					<div class="col-lg-12">&nbsp;</div>	
   					<label class="col-lg-3 control-label">请选择你要查询的热点事件日期</label>
   					<div class="col-lg-6">
      					<input type="date" name="t42" class="form-control" min = "2017-01-01" max = "2017-02-28">
   					</div>
				</div>
				<div class="form-group" id="time5" hidden> 
					<label class="col-lg-3 control-label">请选择你要对比的热点事件时间段</label>
   					<div class="col-lg-4">
      					<input type="date" name="t51" class="form-control">
   					</div>
   					<div class="col-lg-1" style="text-align:center">
      					-------
   					</div>
   					<div class="col-lg-4">
      					<input type="date" name="t52" class="form-control">
   					</div>
   					
   					<div class="col-lg-12">&nbsp;</div>	
   					<label class="col-lg-3 control-label">请选择你要查询的热点事件日期</label>
   					<div class="col-lg-6">
      					<input type="date" name="t53" class="form-control">
   					</div>
				</div>
				<div class="form-group" id="time6" hidden> 
					<label class="col-lg-3 control-label">请选择你要对比的热点事件时间段</label>
   					<div class="col-lg-4">
      					<input type="date" name="t61" class="form-control">
   					</div>
   					<div class="col-lg-1" style="text-align:center">
      					-------
   					</div>
   					<div class="col-lg-4">
      					<input type="date" name="t62" class="form-control">
   					</div>
   					
   					<div class="col-lg-12">&nbsp;</div>	
   					<label class="col-lg-3 control-label">请选择你要查询的热点事件时间段</label>
   					<div class="col-lg-4">
      					<input type="date" name="t63" class="form-control">
   					</div>
   					<div class="col-lg-1" style="text-align:center">
      					-------
   					</div>
   					<div class="col-lg-4">
      					<input type="date" name="t64" class="form-control">
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