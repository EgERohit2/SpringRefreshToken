package com.example.demo.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

public interface JwtInteface {

	String getUsernameFromToken(String token);

	Date getExpirationDateFromToken(String token);

	<T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

	Claims getAllClaimsFromToken(String token);

//	String getTokenType(String token) throws JwtException;

	Boolean isTokenExpired(String token);

	String generateToken(UserDetails userDetails);

	String doGenerateToken(Map<String, Object> claims, String subject);

	String refreshToken(String accessToken, UserDetails userDetails);

	Date calculateExpirationDate(Date createdDate);

	boolean canTokenBeRefreshed(String token);

	boolean ignoreTokenExpiration(String token);

	Boolean validateToken(String token, UserDetails userDetails);

	String getTokenType(String token) throws JwtException;

}
