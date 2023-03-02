package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.component.JwtRequest;

@Repository
public interface JwtRequestRepository extends JpaRepository<JwtRequest, Integer> {
	JwtRequest findByUsername(String username);

}
