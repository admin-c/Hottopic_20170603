function buttonclick(i){
	var iteratr = i;
	var form2 = document.getElementById("query2");
	var num = document.getElementById("num");
	num.setAttribute("value", iteratr);
	form2.submit();
};
$(document).ready(function() {
	$('#sorted').DataTable({
		stateSave: true,
		"columns" : [
		{ "data": "序号" },
		{ "data": "勾选" },
		{ "data": "频繁项集" }, 
		{ "data": "事件描述" }, 
		{ "data": "典型问题" }, 
		{ "data": "典型答案" },
		{ "data": "事件问题数占比" },
		{ "data": "查询" } 
		],
		 "aoColumns": [
null,
{ "orderable": false,"targets":1 },
{ "orderable": false,"targets":2 },
{ "orderable": false,"targets":3 },
{ "orderable": false,"targets":4 },
{ "orderable": false,"targets":5 },
{ "orderSequence": [ "desc", "asc", "asc" ] },
{ "orderable": false,"targets":7 },
],
		
		language : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "显示 _MENU_ 项结果",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
			"sInfoEmpty" : "显示第 0 至 0 项结果，共 0 项",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});

});