package com.example.demo.component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.service.JwtInteface;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service 
public class JwtUtils implements Serializable,JwtInteface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final long JWT_TOKEN_VALIDITY = 1* 60;
	
	public static final long JWT_TOKEN_VALIDITY_FOR_REFRESH_TOKEN=60000;

	// @Value("${jwt.secret}")
	private String secretKey = "rohit";

	// retrieve username from jwt token
	@Override
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	@Override
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	@Override
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieving any information from token we will need the secret key
	@Override
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
	@Override
	public String getTokenType(String token) throws JwtException {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return (String) claims.get("type");

    }

	// check if the token has expired
	@Override
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// generate token for user
	@Override
	public String generateToken(UserDetails userDetails) {

		Map<String, Object> claims = new HashMap<>();

		return doGenerateToken(claims, userDetails.getUsername());
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	@Override
	public String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).claim("type", "access").setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	@Override
	public String refreshToken(String accessToken, UserDetails userDetails) {
		final Date createdDate = new Date();
		final Date expirationDate = calculateExpirationDate(createdDate);

		final Claims claims = getAllClaimsFromToken(accessToken);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);
		return Jwts.builder().setClaims(claims).claim("type", "refresh").setSubject(userDetails.getUsername())
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

	@Override
	public Date calculateExpirationDate(Date createdDate) {
		return new Date(createdDate.getTime() + JWT_TOKEN_VALIDITY_FOR_REFRESH_TOKEN * 1000);
	}

	@Override
	public boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	@Override
	public boolean ignoreTokenExpiration(String token) {
		return false;
	}

	// validate token
	
	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

}
