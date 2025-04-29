package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PageController {
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		return "register"; // resources/templates/register.html を表示
	}

	@GetMapping("/home")
	public String showHomePage(Model model, HttpServletRequest request) {
		// 管理者かどうかを確認
		boolean isAdmin = request.isUserInRole("ADMIN");
		model.addAttribute("isAdmin", isAdmin);

		return "home"; // resources/templates/home.html を表示
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("user", new User());
		return "login"; // resources/templates/login.html を表示
	}

	@GetMapping("/admin_register")
	public String showadmin_registerForm(Model model) {
		model.addAttribute("user", new User());
		return "admin_register"; // resources/templates/admin_register.html を表示
	}
	@GetMapping("/login_admin")
public String showAdminLoginForm(Model model) {
		model.addAttribute("user", new User());
		return "login_admin";//login_admin.htmlを表示
	}
	@GetMapping("/admin_dashboard")
    public String adminDashboard() {
        return "admin/admin_dashboard"; // templates/admin/admin_dashboard.html を返す
    }
}