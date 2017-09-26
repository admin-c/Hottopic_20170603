package com.javy.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javy.entity.DataKey;
import com.javy.entity.Model;
import com.javy.entity.Order;
import com.javy.handler.DatabaseHandler;
import com.javy.handler.ModelHandler;

/**
 * Servlet implementation class LogisticsTrainServlet
 */
@WebServlet(name = "/ModelsTrainServlet", urlPatterns = "/mergeevent.do")
public class ModelsTrainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ModelsTrainServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 响应头设置
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		// 获取前端是否为事件库已有事件的选择
		String eventpick = request.getParameter("eventpick2");
		// 获取选择需要合并的事件列表的选择
		String[] eventnumber = (String[]) session.getAttribute("eventpick");
		// 事件列表
		ArrayList<ArrayList<DataKey>> eventList = (ArrayList<ArrayList<DataKey>>) session
				.getAttribute("events");
		// 原工单
		List<HashSet<Order>> event_texts = (List<HashSet<Order>>)session.getAttribute("event_texts");
		// 模型库
		HashMap<Integer, Model> models = (HashMap<Integer, Model>) session
				.getAttribute("models");
		DatabaseHandler databasehandler = new DatabaseHandler();
		ModelHandler modelhandler = new ModelHandler();
		

		try {
			// 获取全局idf
			HashMap<String, Double> globalIDF = databasehandler.getWholeIDF();
			// cos模型训练
			// 训练全新模型
			if (eventpick.equals("1")) {
				// 事件库描述
				String eventname = request.getParameter("eventname");
				
				// 获取选择事件tf-idf前50%词的并集
				HashMap<String, Double> modelvec = modelhandler.getVec(eventnumber, globalIDF,event_texts);
				String model = modelhandler.getModel(modelvec);
				// 将模型存入数据库
				databasehandler.insertmodel(eventname,model);
			}
			// 已有模型增量训练
			else {
				
				// 获得前端选择事件库事件
				String model_id = request.getParameter("modelselect");
				// 获得模型
				Model m = models.get(Integer.parseInt(model_id));
				
				// 获取选择事件tf-idf前50%词的并集
				String model = modelhandler.getModelBymodels(eventnumber, globalIDF,event_texts, m);
				databasehandler.updatemodel(model_id,model);
			}
			
			String message = "模型训练成功！";
			session.setAttribute("message", message);
			RequestDispatcher rd = request
					.getRequestDispatcher("/CallBack.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
