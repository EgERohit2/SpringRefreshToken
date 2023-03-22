package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.component.CustomUserDetailsService;
import com.example.demo.component.JwtRequest;
import com.example.demo.component.JwtResponse;
import com.example.demo.repository.JwtRequestRepository;
import com.example.demo.service.JwtInteface;

@RestController
public class JwtController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtInteface jwtUtility;

	@Autowired
	private JwtRequestRepository jwtRequestRepository;

	@PostMapping("/generatetoken")
	public ResponseEntity<?> createToken(@RequestBody JwtRequest jwtRequest) throws Exception {
		System.out.println(jwtRequest);
		try {

			authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
		} catch (UsernameNotFoundException e) {
			throw new Exception("bad credential");
		} catch (BadCredentialsException e) {
			throw new Exception("bad credential");
		}

		final UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

		final String token = this.jwtUtility.generateToken(userDetails);
		final String refreshToken = this.jwtUtility.refreshToken(token, userDetails);

		return ResponseEntity.ok(new JwtResponse(token,refreshToken));

	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("user blocked", e);
		} catch (BadCredentialsException e) {
			throw new Exception("invalid credentials", e);
		}

	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAuthenticationToken(@RequestParam(defaultValue="") String refreshToken )
	{
		String name=jwtUtility.getUsernameFromToken(refreshToken);
		JwtRequest users=jwtRequestRepository.findByUsername(name);
		if(users==null)
		{
			return new ResponseEntity<>("InvalidUser",HttpStatus.UNAUTHORIZED);
		}	
		final UserDetails userDetails = customUserDetailsService.loadUserByUsername(name);
		
		if(jwtUtility.canTokenBeRefreshed(refreshToken)&& jwtUtility.validateToken(refreshToken, userDetails)&& jwtUtility.getTokenType(refreshToken).equalsIgnoreCase("refresh"))
		{
			String  newaccessToken=jwtUtility.generateToken(userDetails);
			return new ResponseEntity<>(newaccessToken,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("invalid Users",HttpStatus.UNAUTHORIZED);
		}
		
	}
}
