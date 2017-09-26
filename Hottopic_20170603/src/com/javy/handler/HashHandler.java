package com.javy.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.javy.entity.Order;

public class HashHandler {
	/**
	 * 合并两个hashmap
	 * 
	 * @param yesterday
	 * @param today
	 * @return
	 */
	public HashMap<String, Integer> mergeHashMap(HashMap<String, Integer> yesterday, HashMap<String, Integer> today) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> merge = new HashMap<>();
		for (String items : yesterday.keySet()) {
			merge.put(items, yesterday.get(items));
		}
		for (String items : today.keySet()) {
			if (merge.containsKey(items)) {
				merge.put(items, merge.get(items) + today.get(items));
			} else {
				merge.put(items, today.get(items));
			}
		}
		return merge;
	}

	/**
	 * 计算今日频繁程度占总频繁程度的占比
	 * 
	 * @param today
	 * @param merge
	 * @return
	 */
	public HashMap<String, Double> getTodayPercent(HashMap<String, Integer> today, HashMap<String, Integer> merge) {
		// TODO Auto-generated method stub
		HashMap<String, Double> todaypercent = new HashMap<>();
		for (String items : today.keySet()) {
			double percent = (double) today.get(items) / (double) merge.get(items);
			todaypercent.put(items, percent);
		}
		return todaypercent;
	}
	/**
	 * 对HashMap针对Value（double类型）进行降序排序
	 * 
	 * @param map
	 * @return
	 */
	public List<Map.Entry<String, Double>> SortHash(
			HashMap<String, Double> map) {
		List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(
				map.entrySet());
		Collections.sort(entryList,
				new Comparator<Map.Entry<String, Double>>() {
					// 降序排序
					public int compare(Map.Entry<String, Double> o1,
							Map.Entry<String, Double> o2) {
						// TODO Auto-generated method stub
						if ((o2.getValue() - o1.getValue()) > 0)
							return 1;
						else if ((o2.getValue() - o1.getValue()) == 0)
							return 0;
						else
							return -1;
					}
				});
		return entryList;

	}
	/**
	 * 对HashMap针对Value（double类型）进行降序排序
	 * 
	 * @param map
	 * @return
	 */
	public List<Map.Entry<String, Integer>> SortHashint(
			HashMap<String, Integer> map) {
		List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
				map.entrySet());
		Collections.sort(entryList,
				new Comparator<Map.Entry<String, Integer>>() {
					// 降序排序
					public int compare(Map.Entry<String, Integer> o1,
							Map.Entry<String, Integer> o2) {
						// TODO Auto-generated method stub
						return (o2.getValue() - o1.getValue());
					}
				});
		return entryList;

	}

	public List<Order> Set2List(HashSet<Order> texts2) {
		// TODO Auto-generated method stub
		List<Order> result = new ArrayList<>();
		for(Order o : texts2)
			result.add(o);
		return result;
	}
}
