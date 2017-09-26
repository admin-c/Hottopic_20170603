package com.javy.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CosHandler {
	/**
	 * 相似度计算，根据词的tf-idf列表计算
	 * 
	 * @param str1
	 * @param wordTfIdf 
	 * @param str2
	 * @param historytfidfMap 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public double getSimilarDegreeByTfidf(String str1, HashMap<String, Double> wordTfIdf, String str2, HashMap<String, Double> historytfidfMap) {
		// 创建向量空间模型，使用map实现，主键为词项，值为长度为2的数组，存放着对应词项在字符串中的tf-idf值
		Map<String, double[]> vectorSpace = new HashMap<String, double[]>();
		double[] itemCountArray = null;// 为了避免频繁产生局部变量，所以将itemCountArray声明在此

		// 以空格为分隔符，分解字符串
		String strArray[] = str1.split(" ");
		for (int i = 0; i < strArray.length; ++i) {
				itemCountArray = new double[2];
//				if(!wordTfIdf.containsKey(strArray[i]))
//					wordTfIdf.put(strArray[i], 0.0);
				itemCountArray[0] = wordTfIdf.get(strArray[i]);
				itemCountArray[1] = 0;
				vectorSpace.put(strArray[i], itemCountArray);
		}

		strArray = str2.split(" ");
		for (int i = 0; i < strArray.length; ++i) {
			if (vectorSpace.containsKey(strArray[i])){
				itemCountArray = vectorSpace.get(strArray[i]);
				itemCountArray[1] = historytfidfMap.get(strArray[i]);
				vectorSpace.put(strArray[i], itemCountArray);
			}
			else {
				itemCountArray = new double[2];
				itemCountArray[0] = 0;
				itemCountArray[1] = historytfidfMap.get(strArray[i]);
				vectorSpace.put(strArray[i], itemCountArray);
			}
		}

		// 计算相似度
		double vector1Modulo = 0.00;// 向量1的模
		double vector2Modulo = 0.00;// 向量2的模
		double vectorProduct = 0.00; // 向量积
		Iterator iter = vectorSpace.entrySet().iterator();

		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			itemCountArray = (double[]) entry.getValue();

			vector1Modulo += itemCountArray[0] * itemCountArray[0];
			vector2Modulo += itemCountArray[1] * itemCountArray[1];

			vectorProduct += itemCountArray[0] * itemCountArray[1];
		}

		vector1Modulo = Math.sqrt(vector1Modulo);
		vector2Modulo = Math.sqrt(vector2Modulo);

		// 返回相似度
		return (vectorProduct / (vector1Modulo * vector2Modulo));
	}
	/**
	 * 单向量归一化操作
	 * @param str1
	 * @param wordTfIdf
	 * @return
	 */
	public HashMap<String, Double> vec2one(String str1, HashMap<String, Double> wordTfIdf){
		HashMap<String, Double> h = new HashMap<String, Double>();
		String strArray[] = str1.split(" ");
		// 算模
		double norm = 0.0;
		for(String str : strArray){
			if(wordTfIdf.containsKey(str)){
				norm += wordTfIdf.get(str)*wordTfIdf.get(str);
			}
			else{
				System.err.println("找不到"+str+"对应tf-idf值！");
			}
		}

		// 归一化
		for(String word : strArray){
			h.put(word, wordTfIdf.get(word)/Math.sqrt(norm));
		}
		return h;
	}
	/**
	 * 数值向量归一化
	 * @param wordTfIdf
	 * @return
	 */
	public HashMap<String, Double> vec2one2(HashMap<String, Double> wordTfIdf){
		HashMap<String, Double> h = new HashMap<String, Double>();
//		String strArray[] = str1.split(" ");
		// 算模
		double norm = 0.0;
		for(String str : wordTfIdf.keySet()){
			if(wordTfIdf.containsKey(str)){
				norm += wordTfIdf.get(str)*wordTfIdf.get(str);
			}
			else{
				System.err.println("找不到"+str+"对应tf-idf值！");
			}
		}

		// 归一化
		for(String word : wordTfIdf.keySet()){
			h.put(word, wordTfIdf.get(word)/Math.sqrt(norm));
		}
		return h;
	}
	/**
	 * 合并两个归一化向量
	 * @param str1
	 * @param wordTfIdf
	 * @param str2
	 * @param wordTfIdf2
	 * @return
	 */
	public HashMap<String, Double> merge2vecs(String str1, HashMap<String, Double> wordTfIdf, String str2, HashMap<String, Double> wordTfIdf2){
		HashMap<String, Double> h = new HashMap<String, Double>();
		String strArray1[] = str1.split(" ");
		String strArray2[] = str2.split(" ");
		// 寻找两个向量的相同部分
		HashMap<String ,Double> str1Map = new HashMap<String ,Double>();
		HashMap<String ,Double> str2Map = new HashMap<String ,Double>();
		HashMap<String ,Integer> sameMap = new HashMap<String ,Integer>();
		for(String word : strArray1)
			str1Map.put(word, wordTfIdf.get(word));
		for(String word : strArray2)
			str2Map.put(word, wordTfIdf2.get(word));
		for(String word : strArray2){
			if(str1Map.containsKey(word))
				sameMap.put(word, 0);
		}

		double str1same = 0.0;
		double str2same = 0.0;
		for(String same : sameMap.keySet()){
			str1same += str1Map.get(same);
			str2same += str2Map.get(same);
		}

		// str1Map中每个词除以str1same
		for(String word : str1Map.keySet())
			h.put(word, (str1Map.get(word)/str1same));
		// str2Map中每个词除以str2same
		for(String word : str2Map.keySet())
			h.put(word, str2Map.get(word)/str2same);
		h = vec2one2(h);
		return h;
	}/**
	 * 合并两个归一化向量
	 * @param str1
	 * @param wordTfIdf
	 * @param str2
	 * @param wordTfIdf2
	 * @return
	 */
	public HashMap<String, Double> merge2vecs2(HashMap<String, Double> wordTfIdf, HashMap<String, Double> wordTfIdf2){
		String str1 = "";
		int i = 0;
		for(String word : wordTfIdf.keySet()){
			if(i == wordTfIdf.size()-1)
				str1 += word;
			else{
				str1 += word+" ";
				i++;}
		}
		String str2 = "";
		i = 0;
		for(String word : wordTfIdf2.keySet()){
			if(i == wordTfIdf2.size()-1)
				str2 += word;
			else{
				str2 += word+" ";
				i++;}
		}
		return merge2vecs(str1, wordTfIdf, str2, wordTfIdf2);
	}
}
