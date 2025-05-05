package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	//	@Bean
	//	public PasswordEncoder passwordEncoder() {
	//		return new BCryptPasswordEncoder();
	//	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}
	@Bean
	public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authenticationProvider(authenticationProvider())
				.authorizeHttpRequests(auth -> auth
						//.requestMatchers("/admin/**").hasRole("ADMIN") // 管理者専用
						.requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**", "/admin_register",
								"/login_admin","/Admin/AddAdminRegister","/admin/products","/api/**","/products")
						.permitAll() // これらは一般ユーザーもアクセス可能
						.anyRequest().authenticated())
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(customAuthenticationEntryPoint))
				.formLogin(form -> form
						.loginPage("/login") // 初期ログイン画面（必要）
						.loginProcessingUrl("/perform_login") // 両者共通でOK
						.usernameParameter("username")
						.passwordParameter("password")
						.successHandler(customAuthenticationSuccessHandler()) // ロールで振り分ける
						.failureHandler(customAuthenticationFailureHandler())//管理者ログイン、ユーザーログインでエラー画面を制御
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout=true")
						.permitAll());

		return http.build();
	}

}
