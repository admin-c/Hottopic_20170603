package com.javy.handler;

import java.util.HashMap;

public class BayesianHandler {
	/**
	 * 计算一个int值的hashmap的平均数
	 * 
	 * @param merge
	 * @return
	 */
	public double getIntAverage(HashMap<String, Integer> merge) {
		// TODO Auto-generated method stub
		int all = 0;

		for (String items : merge.keySet()) {
			all += merge.get(items);
		}
		double avg = (double) all / (double) merge.size();
		return avg;
	}

	/**
	 * 计算一个double值的hashmap的平均数
	 * 
	 * @param merge
	 * @return
	 */
	public double getDoubleAverage(HashMap<String, Double> todaypercent) {
		// TODO Auto-generated method stub
		double all = 0;

		for (String items : todaypercent.keySet()) {
			all += todaypercent.get(items);
		}
		double avg = (double) all / (double) todaypercent.size();
		return avg;
	}

	/**
	 * 
	 * 计算贝叶斯平均值
	 *
	 * @param todaypercent
	 * @param merge
	 * @param mergeavg
	 * @param percentavg
	 * @return
	 */
	public HashMap<String, Double> getTodayBayesian(
			HashMap<String, Double> todaypercent,
			HashMap<String, Integer> merge, double mergeavg, double percentavg) {
		// TODO Auto-generated method stub
		HashMap<String, Double> result = new HashMap<>();
		for (String items : todaypercent.keySet()) {
			double itemsbayesian = (todaypercent.get(items) * merge.get(items) + mergeavg
					* percentavg)
					/ (merge.get(items) + mergeavg);
			result.put(items, itemsbayesian);
		}
		return result;
	}

}
