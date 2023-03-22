package com.example.demo.component;

public class JwtResponse {

	private String token;
	
	private String refreshToken;

	public String getToken() {
		return this.token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public JwtResponse(String token, String refreshToken) {
		super();
		this.token = token;
		this.refreshToken = refreshToken;
	}

	public JwtResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}
