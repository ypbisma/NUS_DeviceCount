package com.nusdcbackend;

public class ForecastData {
	private String id = null;
	private String location = null;
	private String locationType = null;
	private String ma3 = null;
	private String ma5 = null;
	private String wa = null;
	private String es = null;
	public String getId() {
		return id;
	}
	public ForecastData (String id, String location, String locationType, String ma3, String ma5, String wa, String es){
		this.id = id;
		this.location = location;
		this.locationType = locationType;
		this.ma3 = ma3;
		this.ma5 = ma5;
		this.wa = wa;
		this.es = es;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getMa3() {
		return ma3;
	}
	public void setMa3(String ma3) {
		this.ma3 = ma3;
	}
	public String getMa5() {
		return ma5;
	}
	public void setMa5(String ma5) {
		this.ma5 = ma5;
	}
	public String getWa() {
		return wa;
	}
	public void setWa(String wa) {
		this.wa = wa;
	}
	public String getEs() {
		return es;
	}
	public void setEs(String es) {
		this.es = es;
	}
}
