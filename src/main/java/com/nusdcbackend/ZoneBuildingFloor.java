//**NUSWATCH-DEVICECOUNT**
//**A FINAL YEAR PROJECT**
//**BY YOHANES PAULUS BISMA**
//**A0115902N**
//**INDUSTRIAL SYSTEMS ENGINEERING & MANAGEMENT**
//**2016/2017**

//This class is used to store the information of zone, building and floor of a particular location

package com.nusdcbackend;

public class ZoneBuildingFloor {

	private String zone;
	private String building;
	private String floor;
	private String count;
	
	public ZoneBuildingFloor(String zone, String building, String floor){
		this.zone = zone;
		this.building = building;
		this.floor = floor;
	}
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
}
