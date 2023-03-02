package com.example.demo.service.serviceImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.component.JwtRequest;
import com.example.demo.repository.JwtRequestRepository;
import com.example.demo.service.JwtRequestService;

@Service
public class JwtRequestServiceImplementation implements JwtRequestService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtRequestRepository jwtRequestRepository;

	@Override
	public JwtRequest postData(JwtRequest jwtRequest) {
		JwtRequest jw = new JwtRequest();
		jw.setUsername(jwtRequest.getUsername());
		String pass = jwtRequest.getPassword();
		String ks = passwordEncoder.encode(pass);
		jw.setPassword(ks);

		return jwtRequestRepository.save(jw);
	}

	@Override
	public void deleteData(int id) {
		this.jwtRequestRepository.deleteById(id);

	}

	@Override
	public void putData(JwtRequest jwtRequest, int id) {
		JwtRequest j = jwtRequestRepository.findById(id).orElseThrow();
		j.setUsername(jwtRequest.getUsername());
		String s = jwtRequest.getPassword();
		String k = passwordEncoder.encode(s);
		j.setPassword(k);

		jwtRequestRepository.save(jwtRequest);
	}

}
