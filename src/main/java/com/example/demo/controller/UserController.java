package com.example.demo.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	// ユーザー登録
	@PostMapping("/register")
	public String register(@ModelAttribute User user, HttpServletRequest request, Model model) {
		try {
			user.setRole(Role.USER);
			userService.registerUser(user); // メール認証を省略して即登録
			return "redirect:/login";//登録成功時はログイン画面にリダイレクト
			//			model.addAttribute("message", "登録が完了しました。");
		} catch (DataIntegrityViolationException e) {
			if (e.getRootCause() instanceof SQLIntegrityConstraintViolationException &&
					e.getRootCause().getMessage().contains("Duplicate entry")) {
				model.addAttribute("errorMessage", "このメールアドレスは既に登録されています。");
			} else {
				model.addAttribute("errorMessage", "登録中に予期せぬエラーが発生しました。");
			}
		}
		return "register"; // 成功・失敗に関係なく登録画面に戻す	
	}

	@GetMapping("/verify")
	public String verify(@RequestParam("token") String token) {
		boolean result = userService.verifyUser(token);
		return result ? "認証完了しました！" : "無効なトークンです。";
	}

}
