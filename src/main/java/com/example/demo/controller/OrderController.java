package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.CustomUserDetails;
import com.example.demo.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	public String checkout(@AuthenticationPrincipal UserDetails userDetails, Model model) {

		try {
			// CustomUserDetails にキャストして userId を取得
			CustomUserDetails customUser = (CustomUserDetails) userDetails;
			String email = customUser.getUsername(); // = email
			Long userId = customUser.getUserId();

			orderService.placeOrder(email, userId);

			model.addAttribute("message", "注文が正常に完了しました。");
			return "order/complete"; // 成功時のビュー名

		} catch (Exception e) {
			model.addAttribute("error", "注文処理中にエラーが発生しました: " + e.getMessage());
			return "order/error"; // エラー時のビュー名
		}
	}

}
