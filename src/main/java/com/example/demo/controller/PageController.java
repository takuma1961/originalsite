package com.example.demo.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PageController {
	public final CartService cartService;

	public PageController(CartService cartService) {
		this.cartService = cartService;
	}

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
		return "user_login"; // resources/templates/login.html を表示
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

	//カート画面表示
	@GetMapping("/cart")
	public String showCart(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		Long userId = userDetails.getUserId();

		List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
		int totalPrice = cartService.calculateTotal(cartItems);

		model.addAttribute("cartItems", cartItems);
		model.addAttribute("totalPrice", totalPrice);

		return "cart_page";
	}

}