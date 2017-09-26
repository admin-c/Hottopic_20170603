package com.javy.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javy.entity.DataKey;
import com.javy.entity.Order;
import com.javy.handler.DatabaseHandler;
import com.javy.handler.HashHandler;
import com.javy.handler.TextHandler;

/**
 * Servlet implementation class TextFindServlet
 */
@WebServlet(name = "/TextFindServlet", urlPatterns = "/textfind.do")
public class TextFindServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TextFindServlet() {
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
		HttpSession session = request.getSession();
		request.setCharacterEncoding("utf-8");

		String num = request.getParameter("num");
		int number = Integer.parseInt(num);
		try {
			List<HashSet<Order>> event_texts = (List<HashSet<Order>>) session
					.getAttribute("event_texts");
			HashSet<Order> texts = event_texts.get(number);

			TextHandler texthandler = new TextHandler();
			HashHandler hashhandler = new HashHandler();
			DatabaseHandler databasehandler = new DatabaseHandler();
			HashMap<String, Double> wholeidf = databasehandler.getWholeIDF();

			HashMap<String, Integer> wordFre = texthandler
					.getTextWordFrequency(texts);
			List<Entry<String, Integer>> wordFre_sorted = hashhandler
					.SortHashint(wordFre);

			HashMap<String, Double> wordtfidf = texthandler.getTextWordTFIDF(
					wordFre, wholeidf);
			// tfidf排序
			List<Map.Entry<String, Double>> wordTfIdf_sort = hashhandler
					.SortHash(wordtfidf);
			// 获取事件
			ArrayList<ArrayList<DataKey>> eventList = (ArrayList<ArrayList<DataKey>>) session
					.getAttribute("events");
			ArrayList<DataKey> event = eventList.get(Integer.parseInt(num));

			// 获取同义词库

			List<String> same_datasource = databasehandler.getsame_data();
			List<Order> sort_texts = texthandler.SortEventText(texts, event, same_datasource,
					wordTfIdf_sort);
			session.setAttribute("wordFre_sorted", wordFre_sorted);
			session.setAttribute("wordTfIdf_sort", wordTfIdf_sort);
			session.setAttribute("sort_texts", sort_texts);
			session.setAttribute("event", event);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RequestDispatcher rd = request.getRequestDispatcher("/Reason.jsp");
		rd.forward(request, response);
	}

}
