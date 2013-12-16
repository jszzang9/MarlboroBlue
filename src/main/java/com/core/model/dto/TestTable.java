package com.core.model.dto;

public class TestTable extends ADto{
	
	private String id;
	private String pw;
	private String content;
	
	public TestTable() {
	}
	
	public TestTable(String id, String pw, String content) {
		this.id = id;
		this.pw = pw;
		this.content = content;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
