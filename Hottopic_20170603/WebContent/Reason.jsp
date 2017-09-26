<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import ="java.util.*"%>
    <%@ page import ="java.util.Map.Entry"%>
    <%@ page import ="com.javy.entity.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<Entry<String, Integer>> wordFre_sorted = (List<Entry<String, Integer>>)session.getAttribute("wordFre_sorted");
	List<Map.Entry<String, Double>> wordTfIdf_sort = (List<Map.Entry<String, Double>>)session.getAttribute("wordTfIdf_sort");
	List<Order> sort_texts = (List<Order>)session.getAttribute("sort_texts");
	ArrayList<DataKey> event = (ArrayList<DataKey>)session.getAttribute("event");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/bootstrap.min.css">
<title>Insert title here</title>
</head>
<body>
	<div class="container" style="font-size:15px">
		<div class="row">
			<label class="col-lg-2">本事件描述：</label>
			<div class="col-lg-2 text-primary">
			<% String name = "";
			for(DataKey d : event){
				name+=d.getKey()+" ";
			}  %>
			<%=name%>
			</div>
			<label class="col-lg-2">本事件文本总数：</label>
			<div class="col-lg-2"><%=sort_texts.size()%></div>
			<div class="col-lg-12">&nbsp;</div>
			<form action="backindex.do" method="post">
				<div class="col-lg-4">
      				<div class="form-group"> 
						<input type="submit" value="返回主页" class="btn btn-block btn-primary">
					</div>	
   				</div>
			</form>	
			<div class="col-lg-12">&nbsp;</div>
			<div class="col-lg-6 table-responsive">
				<table class="table table-hover table-bordered" style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
					<tr>
						<th width="10%">序号</th>
						<th>原工单</th>
						<th>答案</th>
					</tr>
					<%
					for(int i = 0;i < sort_texts.size();i++){ %>
					<tr>
						<td><%= i+1 %></td>
						<td>
						<% 
						String text = sort_texts.get(i).getQuestion_texts();
						String answer = sort_texts.get(i).getAnswer_texts();
						for(int j = 0;j < event.size();j++){
							String word2 = event.get(j).getKey();
							HashMap<String,Integer> sameword = new HashMap<String,Integer>();
							text = text.replaceAll(word2, "<font color='red'>"+word2+"</font>");
						} %>
						<%= text%>
						</td>
						<td><%=answer%></td>
					</tr>
					<%} %>
				</table>
			</div>
			<div class="col-lg-3">
				<table class="table table-hover table-condensed table-bordered">
					<tr>
						<th>序号</th>
						<th>词</th>
						<th>词频统计</th>
					</tr>
					<%for(int i = 0;i < wordFre_sorted.size();i++){ %>
					<tr>
						<td><%= i+1 %></td>
						<td><%= wordFre_sorted.get(i).getKey()%></td>
						<td><%= wordFre_sorted.get(i).getValue()%></td>
					</tr>
					<%} %>
				</table>
			</div>
			<div class="col-lg-3">
				<table class="table table-hover table-condensed table-bordered">
					<tr>
						<th>序号</th>
						<th>词</th>
						<th>本地tf全局idf</th>
					</tr>
					<%for(int i = 0;i < wordTfIdf_sort.size();i++){ %>
					<tr>
						<td><%= i+1 %></td>
						<td><%= wordTfIdf_sort.get(i).getKey()%></td>
						<td><%= wordTfIdf_sort.get(i).getValue()%></td>
					</tr>
					<%} %>
				</table>
			</div>
		</div>
	</div>
</body>
</html>