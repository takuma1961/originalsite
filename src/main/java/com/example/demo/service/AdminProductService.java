package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class AdminProductService {
	@Autowired
	private ProductRepository productRepository;

	// 商品一覧取得
	public List<Product> findAllProducts() {
		return productRepository.findAll();
	}

	// 商品登録
	public void saveProduct(Product product) {
		productRepository.save(product);
	}

	// 商品削除
	public void deleteProductById(Long id) {
		productRepository.deleteById(id);
	}

	// 商品更新
	public void updateProduct(Product product) {
		productRepository.save(product);
	}

	//セール価格、期間を設定、更新
	public void updateSaleInfo(Long productId, Integer salePrice, LocalDate saleStartDate, LocalDate saleEndDate) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			product.setSalePrice(BigDecimal.valueOf(salePrice));
			product.setSaleStartDate(saleStartDate);
			product.setSaleEndDate(saleEndDate);
			productRepository.save(product);
		}
	}

	public void updateStock(Long productId, int newStock) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		product.setStock(newStock);
		productRepository.save(product);
	}

	//	public void updateEdit(Long productId, String newName, int newPrice) {
	//		Product product = productRepository.findById(productId)
	//				.orElseThrow(() -> new RuntimeException("Product not found"));
	//		product.setName(newName);
	//		product.setPrice(BigDecimal.valueOf(newPrice));
	//		productRepository.save(product);
	//	}

	public void updateImagepath(Long id, String imagePath) {
		Optional<Product> optional = productRepository.findById(id);
		if (optional.isPresent()) {
			Product product = optional.get();
			product.setImageUrl(imagePath); // nullでもOK
			productRepository.save(product);
		}
	}

}
