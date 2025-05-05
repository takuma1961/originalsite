package com.example.demo.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.CartService;
import com.example.demo.service.CustomUserDetails;
import com.example.demo.service.UserService;

@Controller
public class CartController {
	private final CartService cartService;
	private final UserService userService;

	public CartController(CartService cartService, UserService userService) {
		this.cartService = cartService;
		this.userService = userService;
	}

	@PostMapping("/cart/add")
	public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		Long userId = userDetails.getUserId();

		cartService.addToCart(userId, productId, quantity);
		return "redirect:/products";
	}

	//カートから商品削除
	@PostMapping("/cart/delete")
	public String deleteCartItem(@RequestParam Long cartItemId, Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Long userId = userDetails.getUserId();

		cartService.deleteCartItem(cartItemId, userId);
		return "redirect:/cart";
	}

	//カートの商品個数変更
	@PostMapping("/cart/update")
	public String updateCart(@RequestParam("cartItemIds") List<Long> cartItemIds,
			@RequestParam("quantities") List<Integer> quantities) {
		for (int i = 0; i < cartItemIds.size(); i++) {
			cartService.updateQuantity(cartItemIds.get(i), quantities.get(i));
		}
		return "redirect:/cart";
	}

}
