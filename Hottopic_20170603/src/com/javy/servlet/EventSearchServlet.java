package com.javy.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.javy.handler.CosHandler;
import com.javy.handler.DatabaseHandler;
import com.javy.handler.HashHandler;
import com.javy.handler.ModelHandler;
import com.javy.handler.TextHandler;

/**
 * 无时间比较的热点事件查询 Servlet implementation class EventSearchTimeServlet
 */
@WebServlet(name = "/EventSearchServlet", urlPatterns = "/eventsearch.do")
public class EventSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EventSearchServlet() {
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
		response.getWriter().append("Served at: ")
				.append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// 响应头设置
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(-1);

		// 记录算法开始时间
		long startTime = System.currentTimeMillis();

		// 获取前端选择
		String hotpick = request.getParameter("hotpick");
		// 接收前端传来需要显示的热点事件条数
		int numbers = Integer.parseInt(request.getParameter("number"));
		// 接收前端传来选择的查重算法
		// String similar = request.getParameter("similar");
		// 接收前端传来的相似度阈值
		String sim = request.getParameter("sim");

		// 昨日数据
		HashMap<String, Integer> yesterday = null;
		// 今日数据存储
		// 工单列表
		HashSet<Order> texts = null;
		// 存储事件库事件，int用于标志其在热点事件列表的位置
		HashMap<String, Integer> event_models = new HashMap<>();
		// 事件列表对应工单存储
		List<HashSet<Order>> event_texts = new ArrayList<>();
		// 事件列表对应频繁项集存储
		HashMap<Integer, List<String>> event_freitem = new HashMap<>();
		// 最后展示的事件列表
		List<List<DataKey>> events = new ArrayList<>();
		// 典型事件列表
		List<Order> event_typical = new ArrayList<>();

		// 查重时使用，历史事件列表
		HashMap<Integer, String> historyList = new HashMap<Integer, String>();
		// 查重时使用，历史事件列表tfidf
		HashMap<Integer, HashMap<String, Double>> historyListtf = new HashMap<Integer, HashMap<String, Double>>();
		// 设置日期处理格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 各种处理器申明
		DatabaseHandler databasehandler = new DatabaseHandler();
		HashHandler hashhandler = new HashHandler();
		TextHandler texthandler = new TextHandler();
		ModelHandler modelhandler = new ModelHandler();
		CosHandler coshandler = new CosHandler();
		// ---------------------------------------------各种后续使用到的命名容器申明-----------------------------------------//
		try {
			if (hotpick.equals("4")) {
				// 对比时间
				String t51 = request.getParameter("t51");
				String t52 = request.getParameter("t52");
				// 查询时间
				Calendar yesdate1 = Calendar.getInstance();
				yesdate1.setTime(sdf.parse(t51));
				Calendar yesdate2 = Calendar.getInstance();
				yesdate2.setTime(sdf.parse(t52));

				// 得到两个map
				yesterday = databasehandler.getfreitems(yesdate1, yesdate2);
				// 得到原文本
				texts = databasehandler.gettexts(yesdate1, yesdate2);
			}
			// ---------------------------------------------数据获取-----------------------------------------//
			HashMap<Integer, Model> models = databasehandler.getModels();
			// ---------------------------------------------模型获取-----------------------------------------//
			HashMap<String, Double> wholeidf = databasehandler.getWholeIDF();
			// ---------------------------------------------全局idf获取-----------------------------------------//
			HashSet<String> lessImportant_datasourceSet = databasehandler
					.getLessImportant_data();
			// ---------------------------------------------次重要词库获取-----------------------------------------//
			List<String> same_datasource = databasehandler.getsame_data();
			// ---------------------------------------------同义词库获取-----------------------------------------//
			List<Map.Entry<String, Integer>> sortedbayesian = hashhandler
					.SortHashint(yesterday);
			// -----------------------------------------贝叶斯平均计算-----------------------------------------//

			for (int i = 0; i < sortedbayesian.size(); i++) {
				// 获取频繁项集
				String freitem = sortedbayesian.get(i).getKey();
				HashSet<Order> itemstext = texthandler
						.findTexts(freitem, texts);

				// 如果原工单为空则跳过
				if (itemstext.isEmpty()) {
					continue;
				}

				// 词频计算
				HashMap<String, Integer> wordFre = texthandler
						.getTextWordFrequency(itemstext);

				// tfidf计算
				HashMap<String, Double> wordtfidf = texthandler
						.getTextWordTFIDF(wordFre, wholeidf);
				// tfidf排序
				List<Map.Entry<String, Double>> wordTfIdf_sort = hashhandler
						.SortHash(wordtfidf);
				// ------------------------------------------文本数值计算-----------------------------------------//
				// 查重
				// 当前事件序列

				// 需要将事件[w1,w2,w3]组合成w1 w2 w3
				int size = (int) (wordTfIdf_sort.size() * 0.5);
				if (size <= 1)
					size = 1;

				String current = texthandler.Combinetfidfwords(wordTfIdf_sort,
						size);
				double model_max = 0;
				int model_max_id = -1;
				// 归一化向量
				HashMap<String, Double> vec2one_current = coshandler.vec2one(
						current, wordtfidf);
				// 先和模型合并
				for (int id : models.keySet()) {
					HashMap<String, Double> modelmap = modelhandler
							.getModelMap(models.get(id));
					String model_word = modelhandler.getModelWord(models
							.get(id));
					if (model_word.equals("")) {
						continue;
					}
					double simlar_model = coshandler.getSimilarDegreeByTfidf(
							current, vec2one_current, model_word, modelmap);
					if (simlar_model > model_max) {
						model_max = simlar_model;
						model_max_id = id;
					}
				}
				// 有模型
				if (model_max_id != -1) {
					String model_name = models.get(model_max_id).getName();
					// 能匹配到模型中
					if (model_max >= Double.parseDouble(sim)) {
						// 原事件列表中已有该模型事件存在
						if (event_models.containsKey(model_name)) {
							int event_location = event_models.get(model_name);
							// 工单列表合并
							HashSet<Order> event_text = event_texts
									.get(event_location);
							event_text.addAll(itemstext);
							// 原工单重新加入列表
							event_texts.set(event_location, event_text);
							// 频繁项集存储
							List<String> fre = event_freitem
									.get(event_location);
							fre.add(freitem);
							event_freitem.put(event_location, fre);
							System.out.println("（频繁项集" + freitem + "）:合并至模型"
									+ model_name);
						} else {
							int locate_size = event_texts.size();
							event_models.put(model_name, locate_size);
							// 频繁项集存储
							List<String> fre = new ArrayList<>();
							fre.add(freitem);
							event_freitem.put(locate_size, fre);
							// 原工单存储
							event_texts.add(locate_size, itemstext);

							System.out.println("（频繁项集" + freitem + "）:匹配至模型"
									+ model_name);
						}
					}
				}
				// 无匹配模型
				else {
					// 无历史事件
					if (historyList.isEmpty()) {
						int locate_size = event_texts.size();
						// 频繁项集存储
						List<String> fre = new ArrayList<>();
						fre.add(freitem);
						event_freitem.put(locate_size, fre);
						// 原工单存储
						event_texts.add(locate_size, itemstext);
						// 事件对应词列表与词向量存储
						historyList.put(locate_size, current);
						historyListtf.put(locate_size, vec2one_current);
						System.out.println("（频繁项集" + freitem + "）:加入事件列表");
					} else {
						double history_max = 0;
						int history_max_id = -1;
						for (int id : historyList.keySet()) {
							HashMap<String, Double> historymap = historyListtf
									.get(id);
							String history_word = historyList.get(id);

							double simlar_history = coshandler
									.getSimilarDegreeByTfidf(current,
											vec2one_current, history_word,
											historymap);
							if (simlar_history > history_max) {
								history_max = simlar_history;
								history_max_id = id;
							}
						}
						if (history_max_id != -1) {
							// 能匹配到事件列表中
							if (history_max >= Double.parseDouble(sim)) {
								// 工单列表合并
								HashSet<Order> event_text = event_texts
										.get(history_max_id);
								event_text.addAll(itemstext);
								// 原工单重新加入列表
								event_texts.set(history_max_id, event_text);
								// 频繁项集存储
								List<String> fre = event_freitem
										.get(history_max_id);
								fre.add(freitem);
								event_freitem.put(history_max_id, fre);
								// tf-idf重算与排序
								HashMap<String, Integer> newwordFre = texthandler
										.getTextWordFrequency(event_text);
								// tfidf计算
								HashMap<String, Double> newwordtfidf = texthandler
										.getTextWordTFIDF(newwordFre, wholeidf);
								// tfidf排序
								List<Map.Entry<String, Double>> newwordTfIdf_sort = hashhandler
										.SortHash(newwordtfidf);
								String newcurrent = texthandler
										.Combinetfidfwords(newwordTfIdf_sort,
												size);
								// 归一化
								HashMap<String, Double> vec2one_newcurrent = coshandler
										.vec2one(newcurrent, newwordtfidf);
								historyList.put(history_max_id, newcurrent);
								historyListtf.put(history_max_id,
										vec2one_newcurrent);
								System.out.println("（频繁项集" + freitem
										+ "）:合并至事件" + fre.get(0));
							} else {
								int locate_size = event_texts.size();
								// 频繁项集存储
								List<String> fre = new ArrayList<>();
								fre.add(freitem);
								event_freitem.put(locate_size, fre);
								// 原工单存储
								event_texts.add(locate_size, itemstext);
								// 事件对应词列表与词向量存储
								historyList.put(locate_size, current);
								historyListtf.put(locate_size, vec2one_current);
								System.out.println("（频繁项集" + freitem
										+ "）:加入事件列表");
							}
						}
					}
				}
			}
			// ------------------------------------------事件查重--------------------------------------------//
			for (int i = 0; i < event_texts.size(); i++) {
				if (event_models.containsValue(i)) {
					// 构建假的事件描述为模型名字
					for (String event_name : event_models.keySet()) {
						if (event_models.get(event_name) == i) {
							ArrayList<DataKey> event = new ArrayList<>();
							event.add(new DataKey(event_name, -1));
							// 加入事件列表
							events.add(event);
							// 典型事件
							Order ty = new Order();
							for (Order o : event_texts.get(i)) {
								ty = o;
								break;
							}
							event_typical.add(ty);
						}
					}
				} else {
					HashSet<Order> eventtext = event_texts.get(i);
					ArrayList<DataKey> event = texthandler
							.SortWordAccording2Text(10,
									lessImportant_datasourceSet,
									hashhandler.SortHash(historyListtf.get(i)),
									eventtext);
					events.add(event);
					// 典型事件
					Order ty = texthandler.FindTypicalText(eventtext, event,
							same_datasource);
					while (ty.getQuestion_texts().equals("-1")) {
						ArrayList<DataKey> et = removemin(event);
						ty = texthandler.FindTypicalText(eventtext, et,
								same_datasource);
					}
					event_typical.add(ty);
				}
			}
			System.out.println("done");

			// ------------------------------------------事件描述--------------------------------------------//
			HashSet<Order> wholepercent = new HashSet<>();
			for (int i = 0; i < event_texts.size(); i++) {
				wholepercent.addAll(event_texts.get(i));
			}
			double percent = (double) (wholepercent.size())
					/ (double) (texts.size());
			long endTime = System.currentTimeMillis();
			// 计算用时
			session.setAttribute("time", (endTime - startTime) / 1000 + "sec");
			// 工单
			session.setAttribute("event_texts", event_texts);
			// 典型事件
			session.setAttribute("event_typical", event_typical);
			// 事件
			session.setAttribute("events", events);
			// 总覆盖率
			session.setAttribute("percent", percent);
			// 总工单条数
			session.setAttribute("textsize", texts.size());
			// 显示条数
			session.setAttribute("number", numbers);
			// 频繁项集
			session.setAttribute("event_freitem", event_freitem);
			// 模型存储
			session.setAttribute("models", models);
			RequestDispatcher rd = request
					.getRequestDispatcher("/DisplayWordred.jsp");
			rd.forward(request, response);
			// ------------------------------------------事件描述--------------------------------------------//
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<DataKey> removemin(ArrayList<DataKey> ee) {
		ArrayList<DataKey> result = ee;
		double min = ee.get(0).getValue();
		int flag = 0;
		for (int i = 1; i < ee.size(); i++) {
			if (min > ee.get(i).getValue()) {
				min = ee.get(i).getValue();
				flag = i;
			}
		}
		result.remove(flag);
		return result;
	}
}
