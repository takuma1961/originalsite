package com.example.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.Order.OrderStatus;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Transactional //メソッド内の処理がすべて正常終了しないとDBに反映しない
	public void placeOrder(String useremail, Long userId) {
		// 1. ユーザー認証
		User user = userRepository.findByEmail(useremail).orElseThrow();

		// 2. カート情報取得
		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

		if (cartItems.isEmpty()) {
			throw new RuntimeException("カートが空です");
		}

		// 3. 合計金額計算
		BigDecimal total = cartItems.stream()
				.map(item -> {
					BigDecimal price = item.getProduct().isOnSale()
							? item.getProduct().getSalePrice()
							: item.getProduct().getPrice();
					return price.multiply(BigDecimal.valueOf(item.getQuantity()));
				})
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// 4. 注文作成
		Order order = new Order();
		order.setUser(user);
		order.setTotalPrice(total);
		order.setStatus(OrderStatus.PENDING);
		orderRepository.save(order);

		// 5. 注文詳細を保存
		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem item : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setPrice(
					item.getProduct().isOnSale()
							? item.getProduct().getSalePrice()
							: item.getProduct().getPrice());
			orderItems.add(orderItem);
		}
		orderItemRepository.saveAll(orderItems);

		// 6. カートを空にする
		cartItemRepository.deleteAll(cartItems);
	}

}
