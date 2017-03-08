package com.nusdcbackend;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nusdcbackend.JobDeviceCount;
import com.nusdcbackend.LoginManager;

public class ScheduledJob implements org.quartz.Job {
	String token;
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private static String executeTime;

	public ScheduledJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {

			// login and obtain token
			LoginManager loginManager = new LoginManager();
			loginManager.login();
			token = loginManager.getToken();
			executeTime = sdf.format(cal.getTime());
			System.out.println(executeTime);

			JobDeviceCount jobDeviceCount = new JobDeviceCount(token);
			jobDeviceCount.execute(executeTime);
			JobForecast jobForecast = new JobForecast(token);
			jobForecast.forecastZone();

		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//
		}
	}
}
