package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// Verification Tokenでユーザーを検索するメソッド
	User findByVerificationToken(String verificationToken);

	//emaiでユーザーを検索するメソッド
	Optional<User> findByEmail(String email);
}
