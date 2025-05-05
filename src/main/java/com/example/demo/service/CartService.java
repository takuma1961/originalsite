package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class CartService {

	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
	}

	public void addToCart(Long userId, Long productId, int quantity) {
		Product product = productRepository.findById(productId)//productsテーブルからカートに追加した商品情報を取得
				.orElseThrow(() -> new IllegalArgumentException("商品が見つかりません"));
		int stock = product.getStock();//商品の在庫数を取得

		//在庫数より多い数を追加した場合にエラー
		if (stock < quantity) {
			throw new IllegalArgumentException("在庫数を超える数量はカートに追加できません");
		}
		CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
				.orElseGet(() -> {
					CartItem newItem = new CartItem();
					newItem.setUserId(userId);
					newItem.setProduct(product);
					newItem.setQuantity(0);
					return newItem;
				});

		int newQuantity = cartItem.getQuantity() + quantity;//カートに追加済みの個数と調整
		if (newQuantity > stock) {
			newQuantity = stock;//在庫を超えた個数がカートに登録されないように制御
		}
		cartItem.setQuantity(newQuantity);
		cartItemRepository.save(cartItem);

		// 在庫数を減らす
		product.setStock(stock - quantity);
		productRepository.save(product);
	}

	//カートから商品を削除
	public void deleteCartItem(Long cartItemId, Long userId) {
		// ① CartItemを取得（CartItemIdはProductIdではないので注意）
		CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new RuntimeException("CartItem not found"));

		// ② ユーザー確認（不正アクセス防止
		if (!cartItem.getUserId().equals(userId)) {
			throw new RuntimeException("ユーザーが一致しません");
		}

		// ③ 商品の在庫を元に戻す
		Product product = cartItem.getProduct();
		product.setStock(product.getStock() + cartItem.getQuantity());
		productRepository.save(product);

		// ④ カートから削除
		cartItemRepository.deleteById(cartItemId);
	}

	//カートに追加した商品個数を変更
	public void updateQuantity(Long cartItemId, int newQuantity) {
		CartItem item = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new IllegalArgumentException("CartItemが見つかりません"));

		// 在庫を超えないようにする
		int stock = item.getProduct().getStock();
		if (newQuantity > stock) {
			newQuantity = stock;
		}

		item.setQuantity(newQuantity);
		cartItemRepository.save(item);
	}

	//カート情報表示用機能
	public List<CartItem> getCartItemsByUserId(Long userId) {
		return cartItemRepository.findByUserId(userId);
	}

	public int calculateTotal(List<CartItem> items) {
		return items.stream()
				.mapToInt(item -> {
					int price = item.getProduct().isOnSale()
							? item.getProduct().getSalePrice().intValue()
							: item.getProduct().getPrice().intValue();
					return price * item.getQuantity();
				})
				.sum();
	}

}