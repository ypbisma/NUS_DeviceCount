//**NUSWATCH-DEVICECOUNT**
//**A FINAL YEAR PROJECT**
//**BY YOHANES PAULUS BISMA**
//**A0115902N**
//**INDUSTRIAL SYSTEMS ENGINEERING & MANAGEMENT**
//**2016/2017**

//This class is designed to execute the different jobs

package com.nusdcbackend;

import java.util.Calendar;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nusdcbackend.JobDeviceCount;
import com.nusdcbackend.LoginManager;

public class ScheduledJob implements org.quartz.Job {
	String token;
	private Calendar executeTime = Calendar.getInstance();

	public ScheduledJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {

			// login and obtain token
			LoginManager loginManager = new LoginManager();
			loginManager.login();
			token = loginManager.getToken();
			JobDeviceCount jobDeviceCount = new JobDeviceCount(token);
//			jobDeviceCount.execute(executeTime);
			JobForecast jobForecast = new JobForecast(token);
			
//			jobForecast.forecastZone();
//			jobForecast.forecastBuilding();
//			jobForecast.forecastUni();
			System.out.println("begin filling uni");
			jobForecast.fillForecastUni();
			System.out.println("begin filling zone");
			jobForecast.fillForecastZone();
			System.out.println("begin filling building");
			jobForecast.fillForecastBuilding();
			System.out.println("done");

		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//
		}
	}
}
