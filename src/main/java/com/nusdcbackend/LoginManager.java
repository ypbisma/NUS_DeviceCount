package com.nusdcbackend;

import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.nusdcbackend.LoginCredential;
import com.nusdcbackend.LoginManager;
import com.nusdcbackend.LoginResponse;
import com.google.gson.Gson;

public class LoginManager {
	private static final String API_URL = "https://api.ami-lab.org";
	private static final String LOGIN_BRANCH = "/api/v1/user/login";
	private static final String domain = "nusstu";
	private static final String username = "A0115902";
	private static final String password = "Password@41";

	private String token;
	private String loginUrl;
	private LoginCredential loginCred;
	private LoginResponse loginRes;

	public LoginManager() {
		this.loginUrl = API_URL + LOGIN_BRANCH;
		this.loginCred = new LoginCredential();
		this.loginCred.setDomain(LoginManager.domain);
		this.loginCred.setName(LoginManager.username);
		this.loginCred.setPassword(LoginManager.password);
	}

	public void login() throws Exception {
		try {
			String output = null;
			Gson gson = new Gson();

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(this.loginUrl);
			StringEntity postingString = new StringEntity(gson.toJson(this.loginCred));

			post.setEntity(postingString);
			post.setHeader("content-type", "application/json");
			HttpResponse response = httpClient.execute(post);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				output = EntityUtils.toString(responseEntity);
			}

			this.loginRes = gson.fromJson(output, LoginResponse.class);
			this.setToken(loginRes.getToken());
		} catch (UnknownHostException e) {
			System.out.println("no internet connection!");
		}
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}

