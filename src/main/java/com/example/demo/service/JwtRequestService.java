package com.example.demo.service;

import com.example.demo.component.JwtRequest;

public interface JwtRequestService {

	public JwtRequest postData(JwtRequest jwtRequest);

	public void deleteData(int id);

	public void putData(JwtRequest jwtRequest, int id);
}
