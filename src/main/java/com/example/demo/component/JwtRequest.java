package com.example.demo.component;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class JwtRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private int id;
	private String username;
	private String password;
	private Boolean isActive;
	@CreationTimestamp
	private LocalDate date;

//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

// why & what did they
	public JwtRequest( String username, String password) {
		super();
		 
		this.username = username;
		this.password = password;
	}

// why & what did they
	public JwtRequest() {
		super();
	}

}
