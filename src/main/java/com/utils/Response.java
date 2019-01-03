package com.utils;

import java.util.HashMap;
import java.util.Map;

public class Response {

	
	private int status;
	private String result;
	private Map<String, String> header = new HashMap<String, String>();

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
