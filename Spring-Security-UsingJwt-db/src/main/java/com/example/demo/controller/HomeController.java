package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.component.JwtRequest;
import com.example.demo.service.serviceImplementation.JwtRequestServiceImplementation;

@RestController
public class HomeController {

	@Autowired
	private JwtRequestServiceImplementation jwtRequestServiceImplementation;

	@GetMapping("/home")
	public String Home() {
		return "Home page";
	}

	@PostMapping("/JwtReq")
	public String postAllData(@RequestBody JwtRequest jwtRequest) {
		this.jwtRequestServiceImplementation.postData(jwtRequest);
		return "posted";
	}

	@DeleteMapping("/JwtReq/{id}")
	public String deleteAllData(@PathVariable int id) {
		this.jwtRequestServiceImplementation.deleteData(id);
		return "deleted";
	}

	@PutMapping("/JwtReq/update/{id}")
	public String postAllData(@RequestBody JwtRequest jwtRequest, @PathVariable int id) {
		this.jwtRequestServiceImplementation.putData(jwtRequest, id);
		return "updated";
	}
	

	
	
}
