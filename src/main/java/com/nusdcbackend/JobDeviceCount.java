package com.nusdcbackend;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class JobDeviceCount {
	private String token;
	private DeviceCountManager deviceCountManager;
	private ArrayList<ZoneBuildingFloor> zoneBuildingFloorList = new ArrayList<>();
	private ArrayList<Zone> zoneList = new ArrayList<>();
	private ArrayList<Building> buildingList = new ArrayList<>();
	private ArrayList<Floor> floorList = new ArrayList<>();
	private DeviceCountDatabaseManager deviceCountDatabaseManager = new DeviceCountDatabaseManager();
	private ZoneBuildingFloorDatabaseManager zoneBuildingFloorDatabaseManager;
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private Integer zoneSum = 0;
	private Integer buildingSum;

	public JobDeviceCount(String token) throws Exception {
		deviceCountManager = new DeviceCountManager(token);
		zoneBuildingFloorDatabaseManager = new ZoneBuildingFloorDatabaseManager(token);
	}

	public void execute() throws Exception {
		String executeTime = sdf.format(cal.getTime());

		zoneBuildingFloorList = zoneBuildingFloorDatabaseManager.getZoneBuildingFloor();
		zoneList = zoneBuildingFloorDatabaseManager.getZones();
		buildingList = zoneBuildingFloorDatabaseManager.getBuildings();
		floorList = zoneBuildingFloorDatabaseManager.getFloors();

		for (ZoneBuildingFloor item : zoneBuildingFloorList) {
			deviceCountManager.setZoneName(item.getZone());
			deviceCountManager.setBuildingName(item.getBuilding());
			deviceCountManager.setFloorName(item.getFloor());
			deviceCountManager.syncDeviceCount();
			deviceCountDatabaseManager.insertDeviceCount(deviceCountManager.getZoneName(),
					deviceCountManager.getBuildingName(), deviceCountManager.getFloorName(),
					deviceCountManager.getDeviceCount().getCount().toString(), executeTime);
			item.setCount(deviceCountManager.getDeviceCount().getCount().toString());
		}
		
		
		for (Building buildingItem : buildingList) {
			buildingSum = 0;
			for (ZoneBuildingFloor location : zoneBuildingFloorList) {
				if (location.getBuilding().equals(buildingItem.getBuildingName())) {
					Integer count = Integer.parseInt(location.getCount());
					buildingSum = buildingSum + count;
				}
			}
			deviceCountDatabaseManager.insertBuildingAggregate(buildingItem.getBuildingId(),
					buildingItem.getBuildingName(), buildingSum.toString(), executeTime);
		}
	}
}
