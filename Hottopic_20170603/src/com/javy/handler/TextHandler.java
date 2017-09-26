package com.javy.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import com.javy.entity.DataKey;
import com.javy.entity.Order;

public class TextHandler {
	/**
	 * 查找原工单
	 * 
	 * @param template
	 * @param f
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashSet<Order> findTexts(String template, HashSet<Order> list) {
		// TODO Auto-generated method stub
		String[] keywords = template.split(" ");
		// 原工单读入
		HashSet<Order> texts_clone = (HashSet<Order>) list.clone();

		for (String s : keywords) {
			HashSet<Order> texts_clone2 = new HashSet<>();
			for (Order o : texts_clone) {
				String line = o.getQuestion_segment();
				if ((" " + line.split("\\|&\\|")[1]).indexOf(" " + s + " ") != -1) {
					texts_clone2.add(o);
				}
			}
			texts_clone = (HashSet<Order>) texts_clone2.clone();
		}
		return texts_clone;
	}

	/**
	 * 计算工单中的每个词的词频
	 * 
	 * @param itemstext
	 * @return
	 */
	public HashMap<String, Integer> getTextWordFrequency(
			HashSet<Order> itemstext) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> result = new HashMap<>();
		for (Order o : itemstext) {
			String question_seg = o.getQuestion_segment();
			String question_seg_handle = question_seg.split("\\|&\\|")[1];
			String[] words = question_seg_handle.split(" ");
			for (String word : words) {
				if (result.containsKey(word)) {
					result.put(word, result.get(word) + 1);
				} else {
					result.put(word, 1);
				}
			}
		}
		return result;
	}

	/**
	 * 计算每个词的tfidf值
	 * 
	 * @param wordFre
	 * @param wholeidf
	 * @return
	 */
	public HashMap<String, Double> getTextWordTFIDF(
			HashMap<String, Integer> wordFre, HashMap<String, Double> wholeidf) {
		// TODO Auto-generated method stub
		HashMap<String, Double> result = new HashMap<>();
		for (String word : wordFre.keySet()) {
			if (wholeidf.containsKey(word)) {
				double tfidf = wordFre.get(word) * wholeidf.get(word);
				result.put(word, tfidf);
			} else {
				double tfidf = wordFre.get(word) * 0;
				result.put(word, tfidf);
			}
		}
		return result;
	}

	/**
	 * 合并wordTfIdf_sort前size个词
	 * 
	 * @param wordTfIdf_sort
	 * @param size
	 * @return
	 */
	public String Combinetfidfwords(List<Entry<String, Double>> wordTfIdf_sort,
			int size) {
		// TODO Auto-generated method stub
		String result = "";
		for (int j = 0; j < size; j++) {
			if (j == size - 1)
				result += wordTfIdf_sort.get(j).getKey();
			else
				result += wordTfIdf_sort.get(j).getKey() + " ";
		}
		return result;
	}

	/**
	 * 根据原工单位置排序。“0-1”串
	 * 
	 * @param segment_numbers
	 * @param lessImportant_datasourceMap
	 * @param wordTfIdf_delete
	 * @param hashSet
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DataKey> SortWordAccording2Text(int segment_numbers,
			HashSet<String> lessImportant_datasourceMap,
			List<Entry<String, Double>> wordTfIdf_delete, HashSet<Order> hashSet) {
		// TODO Auto-generated method stub
		// 设置计数器，若遍历到segment_numbers个词，则停止遍历
		int counter = 0;

		ArrayList<DataKey> event = new ArrayList<DataKey>();
		for (int i = 0; i < wordTfIdf_delete.size(); i++) {
			String word = wordTfIdf_delete.get(i).getKey();
			// 过滤次重要词库
			if (lessImportant_datasourceMap.contains(word))
				continue;
			// 若为空，直接存入
			if (event.isEmpty()) {
				DataKey datakey = new DataKey();
				datakey.setKey(wordTfIdf_delete.get(i).getKey());
				datakey.setValue(wordTfIdf_delete.get(i).getValue());
				event.add(datakey);
				counter++;
			} else {
				// 下一个词
				String next = "";
				// 0-1串临时存储
				HashMap<String, Integer> tmp_01 = new HashMap<String, Integer>();
				HashMap<String, Integer> tmp_01back = new HashMap<String, Integer>();

				// 跳出标志
				boolean breakout = false;

				// 要从包含热词的原工单词组中寻找
				for (Order o : hashSet) {
					String line = o.getQuestion_segment();
					next = wordTfIdf_delete.get(i).getKey();
					breakout = false;

					String line_sub = " " + line.split("\\|&\\|")[1];
					String next_handle = " " + next + " ";
					// 找得到包含next的原工单
					if (line_sub.indexOf(next_handle) != -1) {
						String line_split[] = line_sub.split(" ");
						// next位置记录
						ArrayList<Integer> nextlocate = new ArrayList<Integer>();
						for (int nextcounter = 0; nextcounter < line_split.length; nextcounter++) {
							if (line_split[nextcounter].trim().equals(next))
								nextlocate.add(nextcounter);
						}
						// 对event中的每个词记录位置
						ArrayList<ArrayList<Integer>> eventlocate = new ArrayList<ArrayList<Integer>>();
						for (int eventcounter = 0; eventcounter < event.size(); eventcounter++) {
							ArrayList<Integer> wordlocate = new ArrayList<Integer>();
							for (int wordcounter = 0; wordcounter < line_split.length; wordcounter++) {
								if (line_split[wordcounter].trim().equals(
										event.get(eventcounter).getKey()))
									wordlocate.add(wordcounter);
							}
							if (wordlocate.isEmpty())
								breakout = true;
							else
								eventlocate.add(wordlocate);
						}
						// 其中一个词无法共现则继续下一条
						if (breakout == true)
							continue;
						for (int j = 0; j < nextlocate.size(); j++) {
							// event里已经有多少词则创建几位的bitmap
							int[] bit = new int[event.size()];
							// 添加遍历bit：值全部置为2；
							for (int k = 0; k < bit.length; k++)
								bit[k] = 2;
							// 得到下一个词的位置
							int nextword = nextlocate.get(j);
							// 与已知序列中每个词的位置对比
							for (int k = 0; k < eventlocate.size(); k++) {
								for (int l = 0; l < eventlocate.get(k).size(); l++) {
									int eventword = eventlocate.get(k).get(l);
									if (eventword > nextword)
										bit[k] = 0;
									if (eventword < nextword)
										bit[k] = 1;
									for (String key : tmp_01.keySet()) {
										if (key.substring(k, k + 1).equals("2")) {
											String key2 = key.substring(0, k);
											for (int biti = k; biti < bit.length; biti++) {
												key2 += bit[biti];
											}
											// 最后检测只检测0的位置
											key2 += "0";
											if (tmp_01.containsKey(key2)) {
												tmp_01.put(
														key2,
														tmp_01.get(key2)
																+ tmp_01.get(key));
											} else {
												if (tmp_01back
														.containsKey(key2))
													tmp_01back
															.put(key2,
																	tmp_01back
																			.get(key) + 1);
												else
													tmp_01back.put(key2,
															tmp_01.get(key));
											}
										}
									}
									for (String key : tmp_01back.keySet()) {
										if (tmp_01.containsKey(key))
											tmp_01.put(key, tmp_01.get(key)
													+ tmp_01back.get(key));
										else
											tmp_01.put(key, tmp_01back.get(key));
									}
									tmp_01back.clear();
									if (tmp_01.isEmpty() || k == 0) {
										String bmap = "";
										for (int biti = 0; biti < bit.length; biti++) {
											bmap += bit[biti];
										}
										bmap += "0";
										if (tmp_01.containsKey(bmap)) {
											tmp_01.put(bmap,
													tmp_01.get(bmap) + 1);
										} else {
											tmp_01.put(bmap, 1);
										}
									}
								}
							}
							HashMap<String, Integer> back2 = new HashMap<String, Integer>();
							for (String key : tmp_01.keySet()) {
								if (!key.contains("2"))
									back2.put(key, tmp_01.get(key));
							}
							tmp_01.clear();
							tmp_01 = (HashMap<String, Integer>) back2.clone();
						}
					}
				}
				List<Entry<String, Integer>> entryList3 = new ArrayList<Entry<String, Integer>>(
						tmp_01.entrySet());
				Collections.sort(entryList3,
						new Comparator<Entry<String, Integer>>() {
							// 降序排序
							@Override
							public int compare(Entry<String, Integer> o1,
									Entry<String, Integer> o2) {
								// TODO Auto-generated method stub
								return (o2.getValue() - o1.getValue());
							}
						});
				for (int j = 0; j < entryList3.size(); j++) {
					if (entryList3.get(j).getKey().contains("01")
							|| entryList3.get(j).getKey().contains("2"))
						continue;
					else {
						for (int st3 = 0; st3 < entryList3.get(j).getKey()
								.length(); st3++) {
							if (entryList3.get(j).getKey()
									.substring(st3, st3 + 1).equals("0")) {// 在前
								DataKey datakey = new DataKey();
								datakey.setKey(next);
								datakey.setValue(wordTfIdf_delete.get(i)
										.getValue());
								event.add(st3, datakey);
								break;
							} else if (entryList3.get(j).getKey()
									.substring(st3, st3 + 1).equals("1")) {
							}

						}
						break;
					}
				}
				counter++;
			}
			if (counter >= segment_numbers)
				break;
		}
		return event;
	}

	/**
	 * 寻找一条包含事件描述中所有词的典型事件
	 * 
	 * @param hashSet
	 * @param event
	 * @param same_datasource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Order FindTypicalText(HashSet<Order> hashSet2, List<DataKey> event,
			List<String> same_datasource) {
		// TODO Auto-generated method stub
		// 存放包含事件中所有词的工单集合
		HashSet<Order> hashSet = (HashSet<Order>) hashSet2.clone();
		HashSet<Order> texts = null;
		for (int i = 0; i < event.size(); i++) {
			texts = new HashSet<>();
			// 获得事件中的词
			String word = event.get(i).getKey();
			// if(event.get(i).getTimes() == -1)
			// continue;
			// 存放word的同义词
			HashMap<String, Integer> sameword = new HashMap<String, Integer>();

			// 判断同义词库中是否存在word
			for (String line : same_datasource) {
				String linetmp = line.toUpperCase();
				if (linetmp.indexOf(" " + word + " ") != -1) {
					String same[] = linetmp.split(" ");
					for (String wordd : same) {
						if (wordd.equals(""))
							continue;
						sameword.put(wordd, 0);
					}
				}
			}
			for (Order o : hashSet) {
				String line = o.getQuestion_segment();
				String linetmp = " " + line.toUpperCase().split("\\|&\\|")[1];
				if (sameword.isEmpty()) {
					if (linetmp.indexOf(" " + word + " ") != -1)
						texts.add(o);
				} else {
					// 对所有同义词查找
					for (String wordd : sameword.keySet()) {
						if (linetmp.indexOf(" " + wordd + " ") != -1) {
							texts.add(o);
						}
					}
				}
			}
			hashSet = texts;
		}
		if (!hashSet.isEmpty()) {
			Order result = new Order();
			for (Order o : hashSet) {
				result = o;
				break;
			}
			return result;
		} else {
			Order result = new Order("-1", "-1", "-1", "-1");
			return result;

		}
	}
	/**
	 * 对原工单进行排序(包含热词多的排名靠前)
	 * 
	 * @param texts
	 * 
	 * @param event
	 * 
	 * @param same_datasource
	 * 
	 * @param word_tfidf
	 * @return 
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Order> SortEventText(HashSet<Order> texts, ArrayList<DataKey> event,
			List<String> same_datasource, List<Entry<String, Double>> word_tfidf) {
		// TODO Auto-generated method stub
		ArrayList<DataKey> event2 = new ArrayList<DataKey>();
		event2 = (ArrayList<DataKey>) event.clone();
		HashSet<Order> texts2 = (HashSet<Order>) texts.clone();
		// 新工单列表
		List<Order> textnew = new ArrayList<Order>();
		// 定位
		// tf-idf列表中的位置
		HashMap<String, Integer> eventwordlocate = new HashMap<String, Integer>();
		// event中的位置
		HashMap<String, Integer> eventlocate = new HashMap<String, Integer>();
		for(int i = 0;i < event2.size();i++){
			DataKey d = event2.get(i);
			eventlocate.put(d.getKey(), i);
			eventwordlocate.put(d.getKey(), 0);
		}
		for(int i = 0;i < word_tfidf.size();i++){
			Entry<String, Double> e = word_tfidf.get(i);
			if(eventwordlocate.containsKey(e.getKey()))
				eventwordlocate.put(e.getKey(), i);
		}
		
		// 排序
		List<Entry<String, Integer>> locate = new ArrayList<Entry<String, Integer>>(
				eventwordlocate.entrySet());
		Collections.sort(locate, new Comparator<Entry<String, Integer>>() {
			// 降序排序
			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				return (o2.getValue() - o1.getValue());
			}
		});
		
		for(int i = 0; i < locate.size();i++ ){
			if(event2.size() == 1){
				textnew.addAll(texts2);
			}
				
			else{
				HashSet<Order> textfind =  FindTypicalTextList(texts2, event2, same_datasource);
				textnew.addAll(textfind);
				texts2.removeAll(textfind);
				String word = locate.get(i).getKey();
				int index = eventlocate.get(word);
				event2.remove(index);
				for(int j = 0;j < event2.size();j++){
					DataKey d = event2.get(j);
					eventlocate.put(d.getKey(), j);
				}
			}
		}
		return textnew;
	}
	/**
     * 寻找所有包含事件描述中所有词的典型事件
     * @param texts2
     * @param event
     * @param same_datasource
     * @return
     */
	@SuppressWarnings("unchecked")
	public HashSet<Order> FindTypicalTextList(HashSet<Order> hashSet2, List<DataKey> event,
			List<String> same_datasource) {
		// TODO Auto-generated method stub
		// 存放包含事件中所有词的工单集合
		HashSet<Order> hashSet = (HashSet<Order>) hashSet2.clone();
		HashSet<Order> texts = null;
		for (int i = 0; i < event.size(); i++) {
			texts = new HashSet<>();
			// 获得事件中的词
			String word = event.get(i).getKey();
			// if(event.get(i).getTimes() == -1)
			// continue;
			// 存放word的同义词
			HashMap<String, Integer> sameword = new HashMap<String, Integer>();

			// 判断同义词库中是否存在word
			for (String line : same_datasource) {
				String linetmp = line.toUpperCase();
				if (linetmp.indexOf(" " + word + " ") != -1) {
					String same[] = linetmp.split(" ");
					for (String wordd : same) {
						if (wordd.equals(""))
							continue;
						sameword.put(wordd, 0);
					}
				}
			}
			for (Order o : hashSet) {
				String line = o.getQuestion_segment();
				String linetmp = " " + line.toUpperCase().split("\\|&\\|")[1];
				if (sameword.isEmpty()) {
					if (linetmp.indexOf(" " + word + " ") != -1)
						texts.add(o);
				} else {
					// 对所有同义词查找
					for (String wordd : sameword.keySet()) {
						if (linetmp.indexOf(" " + wordd + " ") != -1) {
							texts.add(o);
						}
					}
				}
			}
			hashSet = texts;
		}
			return hashSet;
	}
}
