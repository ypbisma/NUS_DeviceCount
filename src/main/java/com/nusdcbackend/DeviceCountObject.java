//**NUSWATCH-DEVICECOUNT**
//**A FINAL YEAR PROJECT**
//**BY YOHANES PAULUS BISMA**
//**A0115902N**
//**INDUSTRIAL SYSTEMS ENGINEERING & MANAGEMENT**
//**2016/2017**

package com.nusdcbackend;

import com.nusdcbackend.DeviceCount;
import com.google.gson.annotations.SerializedName;

public class DeviceCountObject {
	@SerializedName("DeviceCount")
	DeviceCount deviceCount;

	public DeviceCount getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(DeviceCount deviceCount) {
		this.deviceCount = deviceCount;
	}
}