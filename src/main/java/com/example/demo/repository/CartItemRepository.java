package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

	List<CartItem> findByUserId(Long userId);

	void deleteById(Long id);

	Optional<CartItem> findById(Long id);

}
