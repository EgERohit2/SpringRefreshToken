package com.example.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	public static final String[] PUBLIC_URLS = { "/v3/api-docs", "/v2/api-docs", "/swagger-resources/**",
			"/swagger-ui/**", "/webjars/**", "/api/swagger-ui/index.html" };
	@Autowired
	private jwtAuthentictionEntryPoint entryPoint;

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers("/generatetoken", "/JwtReq", "/refresh").permitAll()
				.antMatchers(PUBLIC_URLS).permitAll().

				anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(entryPoint).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	}

	@Bean
	public AuthenticationManager getAuthenticationManager() throws Exception {
		return super.authenticationManagerBean();
	}
}
