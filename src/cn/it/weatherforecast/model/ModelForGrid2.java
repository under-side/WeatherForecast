package cn.it.weatherforecast.model;

import java.io.Serializable;

public class ModelForGrid2 implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int id;
	String name,name_txt,name_txt_detail;
	public String getName_txt_detail() {
		return name_txt_detail;
	}
	public void setName_txt_detail(String name_txt_detail) {
		this.name_txt_detail = name_txt_detail;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName_txt() {
		return name_txt;
	}
	public void setName_txt(String name_txt) {
		this.name_txt = name_txt;
	}
	
}
