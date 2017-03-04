package com.nusdcbackend;

import java.util.ArrayList;
import java.util.Vector;

public class JobDeviceCount {
	private String token;
	private DeviceCountManager deviceCountManager;
	private ArrayList<ZoneBuildingFloor> zoneBuildingFloorList = new ArrayList<>();
	private DeviceCountDatabaseManager deviceCountDatabaseManager = new DeviceCountDatabaseManager();
	private ZoneBuildingFloorDatabaseManager zoneBuildingFloorDatabaseManager;

	public JobDeviceCount(String token) throws Exception {
		deviceCountManager = new DeviceCountManager(token);
		zoneBuildingFloorDatabaseManager = new ZoneBuildingFloorDatabaseManager(token);
	}

	public void execute() throws Exception {

		zoneBuildingFloorList = zoneBuildingFloorDatabaseManager.getZoneBuildingFloor();

		for (ZoneBuildingFloor item : zoneBuildingFloorList) {
			deviceCountManager.setZoneName(item.getZone());
			deviceCountManager.setBuildingName(item.getBuilding());
			deviceCountManager.setFloorName(item.getFloor());
			deviceCountManager.syncDeviceCount();
			writeDeviceCount();
		}
	}

	public void writeDeviceCount() {
		deviceCountDatabaseManager.insertDeviceCount(deviceCountManager.getZoneName(),
				deviceCountManager.getBuildingName(), deviceCountManager.getFloorName(),
				deviceCountManager.getDeviceCount().getCount().toString());
	}

	public void writeZoneBuildingFloor(String zone, String building, String floor) {
		zoneBuildingFloorDatabaseManager.insertZoneBuildingFloor(zone, building, floor);
	}
}
