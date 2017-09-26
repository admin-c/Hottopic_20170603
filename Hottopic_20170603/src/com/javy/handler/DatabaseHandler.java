package com.javy.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.javy.database.SQLJDBC;
import com.javy.entity.Model;
import com.javy.entity.Order;

public class DatabaseHandler {

	/**
	 * 从数据库中取出startdate到enddate的频繁项集集合
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String, Integer> getfreitems(Calendar startdate,
			Calendar enddate) throws SQLException {
		// TODO Auto-generated method stub
		SQLJDBC sqljdbc = new SQLJDBC();
		Connection c = sqljdbc.getConnection();
		Statement s = c.createStatement();

		HashMap<String, Integer> result = new HashMap<>();
		Calendar startdate2 = (Calendar) startdate.clone();
		Calendar enddate2 = (Calendar) enddate.clone();

		while (startdate2.getTimeInMillis() <= enddate2.getTimeInMillis()) {
			String date = startdate2.get(Calendar.YEAR) + "-"
					+ String.format("%02d", startdate2.get(Calendar.MONTH) + 1)
					+ "-"
					+ String.format("%02d", startdate2.get(Calendar.DATE));
			String sql = "select items,fre from freitems where time = '" + date
					+ "'";

			ResultSet resultset = s.executeQuery(sql);
			while (resultset.next()) {
				String items = resultset.getString("items");
				int fre = resultset.getInt("fre");
				if (result.containsKey(items)) {
					result.put(items, fre + result.get(items));
				} else {
					result.put(items, fre);
				}
			}
			startdate2.add(Calendar.DATE, 1);
		}
		s.close();
		c.close();
		return result;
	}

	/**
	 * 获取startdate到enddate的文本集合
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws SQLException
	 */
	public HashSet<Order> gettexts(Calendar startdate, Calendar enddate)
			throws SQLException {
		// TODO Auto-generated method stub
		SQLJDBC sqljdbc = new SQLJDBC();
		Connection c = sqljdbc.getConnection();
		Statement s = c.createStatement();

		HashSet<Order> result = new HashSet<>();
		Calendar startdate2 = (Calendar) startdate.clone();
		Calendar enddate2 = (Calendar) enddate.clone();

		while (startdate2.getTimeInMillis() <= enddate2.getTimeInMillis()) {
			String date = startdate2.get(Calendar.YEAR) + "-"
					+ String.format("%02d", startdate2.get(Calendar.MONTH) + 1)
					+ "-"
					+ String.format("%02d", startdate2.get(Calendar.DATE));
			String sql = "select question_text,question_text_seg,answer_text,answer_text_seg from data where date_time = '"
					+ date + "'";

			ResultSet resultset = s.executeQuery(sql);
			while (resultset.next()) {
				String question_text = resultset.getString("question_text")
						.toUpperCase();
				String question_text_seg = resultset.getString(
						"question_text_seg").toUpperCase();
				String answer_text = resultset.getString("answer_text")
						.toUpperCase();
				String answer_text_seg = resultset.getString("answer_text_seg")
						.toUpperCase();
				Order o = new Order(question_text, question_text_seg,
						answer_text, answer_text_seg);
				result.add(o);
			}
			startdate2.add(Calendar.DATE, 1);
		}
		s.close();
		c.close();
		return result;
	}

	/**
	 * 获取模型库
	 * 
	 * @return
	 * @throws SQLException
	 */
	public HashMap<Integer, Model> getModels() throws SQLException {
		// TODO Auto-generated method stub
		SQLJDBC sqljdbc = new SQLJDBC();
		Connection c = sqljdbc.getConnection();
		Statement s = c.createStatement();

		HashMap<Integer, Model> models = new HashMap<>();
		String sql = "select * from cosmodel";
		ResultSet resultset = s.executeQuery(sql);
		while (resultset.next()) {
			int id = resultset.getInt("id");
			String model = resultset.getString("model");
			String name = resultset.getString("name");
			Model m = new Model(model, name);
			models.put(id, m);
		}
		s.close();
		c.close();
		return models;
	}

	/**
	 * 获取全局idf
	 * 
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String, Double> getWholeIDF() throws SQLException {
		// TODO Auto-generated method stub
		SQLJDBC sqljdbc = new SQLJDBC();
		Connection c = sqljdbc.getConnection();
		Statement s = c.createStatement();

		HashMap<String, Double> wholeidf = new HashMap<>();
		String sql = "select * from wholeidf";
		ResultSet resultset = s.executeQuery(sql);
		while (resultset.next()) {
			String word = resultset.getString("word");
			int wordtexts = resultset.getInt("wordtexts");
			int wholetexts = resultset.getInt("wholetexts");
			double idf = Math.log((double) wholetexts
					/ (double) (wordtexts + 1));
			wholeidf.put(word, idf);
		}
		s.close();
		c.close();
		return wholeidf;
	}

	/**
	 * 获取次重要词库
	 * 
	 * @return
	 * @throws SQLException
	 */
	public HashSet<String> getLessImportant_data() throws SQLException {
		// TODO Auto-generated method stub
		SQLJDBC sqljdbc = new SQLJDBC();
		Connection c = sqljdbc.getConnection();
		Statement s = c.createStatement();

		HashSet<String> result = new HashSet<>();
		String sql = "select * from lessimportant";
		ResultSet resultset = s.executeQuery(sql);
		while (resultset.next()) {
			String word = resultset.getString("word");
			result.add(word);
		}
		s.close();
		c.close();
		return result;

	}

	/**
	 * 获取同义词词库
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<String> getsame_data() throws SQLException {
		// TODO Auto-generated method stub
		SQLJDBC sqljdbc = new SQLJDBC();
		Connection c = sqljdbc.getConnection();
		Statement s = c.createStatement();

		List<String> result = new ArrayList<>();
		String sql = "select * from sameword";
		ResultSet resultset = s.executeQuery(sql);
		while (resultset.next()) {
			String word = resultset.getString("words");
			result.add(word);
		}
		s.close();
		c.close();
		return result;
	}

	/**
	 * 将模型插入数据库
	 * 
	 * @param eventname
	 * @param model
	 * @throws SQLException
	 */
	public void insertmodel(String eventname, String model) throws SQLException {
		// TODO Auto-generated method stub
		SQLJDBC sqljdbc = new SQLJDBC();
		Connection c = sqljdbc.getConnection();
		Statement s = c.createStatement();
		String sql = "insert into cosmodel(model,name) values ('" + model
				+ "','" + eventname + "')";
		s.execute(sql);
		s.close();
		c.close();

	}
	/**
	 * 更新模型
	 * 
	 * @param eventname
	 * @param model
	 * @throws SQLException
	 */
	public void updatemodel(String model_id, String model) throws SQLException {
		// TODO Auto-generated method stub
		SQLJDBC sqljdbc = new SQLJDBC();
		Connection c = sqljdbc.getConnection();
		Statement s = c.createStatement();
		String sql = "update cosmodel set model = '" + model
				+ "where id = '"+model_id+"')";
		s.executeUpdate(sql);
		s.close();
		c.close();
	}

}
