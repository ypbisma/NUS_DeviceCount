package com.nusdcbackend;

public class LoginResponse {

	private boolean error;
	private String message;
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}

