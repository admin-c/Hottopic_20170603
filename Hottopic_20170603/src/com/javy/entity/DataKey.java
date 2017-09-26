package com.javy.entity;

import java.io.Serializable;

public class DataKey implements Serializable{
	private static final long serialVersionUID = 1L;
	private String key;
	private double value;

	public DataKey() {
		super();
	}

	public DataKey(String key, double value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	
}
