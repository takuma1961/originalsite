package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entity.User;

@SpringBootTest
public class UserRepositryTest {
	//UserRepositoryの機能を有効化
	@Autowired
	private UserRepository userRepository;

	@Test
	public void testFindBtEmail() {
		//テスト対象のメールアドレス
		String testEmail = "takuma1961@outlook.com";

		// findByEmailを実行
		Optional<User> user = userRepository.findByEmail(testEmail);

		//結果を確認
		//userがデータを保持しているか
		if (user.isPresent()) {
			System.out.println("User found: " + user.get().getEmail());
			//期待値と一致していることを確認
			assertEquals(testEmail, user.get().getEmail());
		} else {
			System.out.println("No user found with email: " + testEmail);
			fail("User not found with email: " + testEmail); // テスト失敗を表示
		}
	}

}
