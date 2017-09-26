package com.javy.entity;

/**
 * Created by Administrator on 2017/3/28.
 */
public class Order {
	private String question_texts;
	private String question_segment;
	private String answer_texts;
	private String answer_segments;

	public Order(String question_texts, String question_segment,
			String answer_texts, String answer_segments) {
		this.question_texts = question_texts;
		this.question_segment = question_segment;
		this.answer_texts = answer_texts;
		this.answer_segments = answer_segments;
	}

	public Order() {
	}

	public String getQuestion_texts() {
		return question_texts;
	}

	public void setQuestion_texts(String question_texts) {
		this.question_texts = question_texts;
	}

	public String getQuestion_segment() {
		return question_segment;
	}

	public void setQuestion_segment(String question_segment) {
		this.question_segment = question_segment;
	}

	public String getAnswer_texts() {
		return answer_texts;
	}

	public void setAnswer_texts(String answer_texts) {
		this.answer_texts = answer_texts;
	}

	public String getAnswer_segments() {
		return answer_segments;
	}

	public void setAnswer_segments(String answer_segments) {
		this.answer_segments = answer_segments;
	}
}
