package com.javy.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.javy.entity.Model;
import com.javy.entity.Order;

public class ModelHandler {

	/**
	 * 将model数据整合为map
	 * 
	 * @param model
	 * @return
	 */
	public HashMap<String, Double> getModelMap(Model model) {
		// TODO Auto-generated method stub
		HashMap<String, Double> result = new HashMap<>();
		String model_idf = model.getModel();
		String[] words_idf = model_idf.split(" ");
		for (String word_idf : words_idf) {
			String[] wordidf = word_idf.split("=");
			result.put(wordidf[0], Double.parseDouble(wordidf[1]));
		}
		return result;
	}

	/**
	 * 获取model的词列表
	 * 
	 * @param model
	 * @return
	 */
	public String getModelWord(Model model) {
		// TODO Auto-generated method stub
		String result = "";
		String model_idf = model.getModel();
		String[] words_idf = model_idf.split(" ");
		int i = 0;
		for (String word_idf : words_idf) {
			String[] wordidf = word_idf.split("=");
			if (i == words_idf.length - 1)
				result += wordidf[0];
			else
				result += wordidf[0] + " ";
			i++;
		}
		return result;
	}

	/**
	 * 获取模型
	 * 
	 * @param eventnumber
	 * @param globalIDF
	 * @param event_texts
	 * @return
	 */
	public HashMap<String, Double> getVec(String[] eventnumber, HashMap<String, Double> globalIDF,
			List<HashSet<Order>> event_texts) {
		// TODO Auto-generated method stub
		HashSet<Order> texts = new HashSet<>();
		TextHandler texthandler = new TextHandler();
		HashHandler hashhandler = new HashHandler();
		CosHandler coshandler = new CosHandler();
		// 原工单合并
		for(String eventpick : eventnumber){
			texts.addAll(event_texts.get(Integer.parseInt(eventpick)));
		}
		
		HashMap<String, Integer> wordFre = texthandler
				.getTextWordFrequency(texts);

		// tfidf计算
		HashMap<String, Double> wordtfidf = texthandler
				.getTextWordTFIDF(wordFre, globalIDF);
		List<Map.Entry<String, Double>> wordTfIdf_sort = hashhandler
				.SortHash(wordtfidf);
		String current = "";
		int size = (int)(wordTfIdf_sort.size()*0.5);
		if(size < 1)
			size = 1;
		for(int i = 0;i < size;i++){
			if(i == size -1)
				current += wordTfIdf_sort.get(i).getKey();
			else
				current += wordTfIdf_sort.get(i).getKey()+ " ";
		}
		HashMap<String, Double> wordtfidf_one =  coshandler.vec2one(current, wordtfidf);
		
		return wordtfidf_one;
	}

	public String getModel(HashMap<String, Double> modelvec) {
		// TODO Auto-generated method stub
		String result = "";
		int i = 0;
		for(String word : modelvec.keySet()){
			if(i == modelvec.size()-1)
				result += word + "=" +modelvec.get(word);
			else{
				result += word + "=" +modelvec.get(word)+" ";
				i++;}
		}
		return result;
	}

	public String getModelBymodels(String[] eventnumber,
			HashMap<String, Double> globalIDF,
			List<HashSet<Order>> event_texts, Model m) {
		// TODO Auto-generated method stub
		HashMap<String, Double> vec = getVec(eventnumber,globalIDF,event_texts);
		HashMap<String, Double> modelvec = getModelMap(m);
		CosHandler coshandler = new CosHandler();
		HashMap<String, Double> result = coshandler.merge2vecs2(vec, modelvec);
		return getModel(result);
	}

}
