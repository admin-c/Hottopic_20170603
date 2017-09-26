<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.javy.entity.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ArrayList<ArrayList<DataKey>> eventList = (ArrayList<ArrayList<DataKey>>)session.getAttribute("events");
	String time = (String)session.getAttribute("time");
	List<HashSet<Order>> event_texts = (List<HashSet<Order>>)session.getAttribute("event_texts");
	List<Order> event_typical = (List<Order>)session.getAttribute("event_typical");
	HashMap<Integer, List<String>> event_freitem = (HashMap<Integer, List<String>>)session.getAttribute("event_freitem");
// 	double percent = (double)session.getAttribute("percent");
	int textsize = (int)session.getAttribute("textsize");
	int numbers = (int)session.getAttribute("number");
	
	int sz = 0;
	if(eventList.size() > numbers)
		sz = numbers;
	else
		sz = eventList.size();
		for(int i = 0;i < sz;i++){
			
		}
		HashSet<Order> wholepercent = new HashSet<>();
		for (int i = 0; i < sz; i++) {
			wholepercent.addAll(event_texts.get(i));
		}
		double percent = (double)(wholepercent.size())/(double)(textsize);
		String formatpercent =  String.format("%.2f", percent * 100).toString() + "%";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript" src="js/displayred.js"></script>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/dataTables.bootstrap.min.css">
</head>
<body>
	<div class="container" style="font-size: 15px">
		<div class="row">
			<label class="col-lg-2">计算用时：</label>
			<div class="col-lg-2"><%=time%></div>
			<label class="col-lg-2">工单覆盖率：</label>
			<div class="col-lg-2"><%=formatpercent%></div>
			<label class="col-lg-2">文本总数：</label>
			<div class="col-lg-2"><%=textsize%></div>
			<form action="merge.do" method="post">
				<div class="col-lg-4">
					<div class="form-group">
						<input type="submit" value="合并" class="btn btn-block btn-primary">
					</div>
				</div>
				<div class="col-lg-12">
					<table class="table table-hover table-bordered display" id="sorted">
						<thead>
							<tr>
								<th>序号</th>
								<th>勾选</th>
								<th>频繁项集</th>
								<th>事件描述</th>
								<th>典型问题</th>
								<th>典型答案</th>
								<th>事件问题数占比</th>
								<th>查询</th>
							</tr>
						</thead>
						<%
						
							for(int i = 0;i < sz;i++){
						%>
						<tr>
							<td><%=i+1%></td>
							<%
								if(eventList.get(i).get(0).getValue() == -1){
							%>
							<td>-</td>
							<%
								} else{
							%>
							<td><input type="checkbox" value="<%=i%>" name="chosemerge"></td>
							<%
								}
							%>
							<td>
							<%List<String> s = event_freitem.get(i);
							if(s.size() == 1){%>
							<%=s.get(0) %>
							<%} else{%>
							<a data-toggle="collapse"
							href="#collapseExample<%=i%>" aria-expanded="false"
							aria-controls="collapseExample"><%=s.get(0) %></a>&nbsp;&nbsp;<%=s.size() %>
							<div class="collapse" id="collapseExample<%=i%>">
							  <%
							  	for(int j = 0;j < s.size();j++){%>
							  		<%=s.get(j) %><br><br>
							  	<%}
							  	
							%>
							</div>
							<%} %>
							</td>
							<td width=13%>[ <%
								for(int j = 0;j < eventList.get(i).size();j++){
												//最后一个词
												if(j == eventList.get(i).size()-1){
							%> <%=eventList.get(i).get(j).getKey()%> <%
 	} 
  						   //不是最后一个词
  						   else{
 %> <%=eventList.get(i).get(j).getKey()%> , <%
 	} }
 %> ]
							</td>
							<td
								style="width: 30%; word-wrap: break-word; word-break: break-all;">
								<%
									String typicalquestion = event_typical.get(i).getQuestion_texts();
														for(int j = 0;j < eventList.get(i).size();j++){
															String word2 = eventList.get(i).get(j).getKey();
															typicalquestion = typicalquestion.replaceAll(word2, "<font color='red'>"+word2+"</font>");
														}
								%> <%=typicalquestion%>
							</td>
							<td
								style="width: 30%; word-wrap: break-word; word-break: break-all;">
								<%
									String typicalanswer = event_typical.get(i).getAnswer_texts();
								%>
								<%=typicalanswer%>
							</td>
							<td>
								<%
									double event_percent = (double)event_texts.get(i).size()/(double)textsize;
								String formateventpercent =  String.format("%.2f", event_percent * 100).toString() + "%";
								%>
								<%=formateventpercent%>
							</td>
							<td><input type="button" value="查询"
								class="btn btn-block btn-primary" onclick="buttonclick(<%=i%>)">
							</td>
						</tr>
						<%
							}
						%>
					</table>
				</div>
			</form>
		</div>

	</div>
	<form action="textfind.do" method="post" id="query2"
		class="form form-horizontal">
		<input type="hidden" name="num" id="num">
	</form>
</body>
</html>