package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class AdminProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private AdminProductService adminProductService;

	private Product testProduct;

	@BeforeEach
	public void setUp() {
		testProduct = new Product();
		testProduct.setId(1L);
		testProduct.setName("テスト商品");
		testProduct.setPrice(BigDecimal.valueOf(1000));
		testProduct.setStock(10);
	}

	// 商品一覧取得のテスト
	@Test
	public void testFindAllProducts() {
		when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));

		var products = adminProductService.findAllProducts();

		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals(testProduct.getName(), products.get(0).getName());
		verify(productRepository, times(1)).findAll();
	}

	// 商品登録のテスト
	@Test
	public void testSaveProduct() {
		adminProductService.saveProduct(testProduct);

		verify(productRepository, times(1)).save(testProduct);
	}

	// 商品削除のテスト
	@Test
	public void testDeleteProductById() {
		adminProductService.deleteProductById(1L);

		verify(productRepository, times(1)).deleteById(1L);
	}

	// 商品更新のテスト
	@Test
	public void testUpdateProduct() {
		adminProductService.updateProduct(testProduct);

		verify(productRepository, times(1)).save(testProduct);
	}

	// セール情報更新のテスト
	@Test
	public void testUpdateSaleInfo() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

		adminProductService.updateSaleInfo(1L, 800, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 10));

		assertEquals(BigDecimal.valueOf(800), testProduct.getSalePrice());
		assertEquals(LocalDate.of(2025, 5, 1), testProduct.getSaleStartDate());
		assertEquals(LocalDate.of(2025, 5, 10), testProduct.getSaleEndDate());

		verify(productRepository, times(1)).save(testProduct);
	}

	// 商品が存在しない場合、セール情報は更新されないテスト
	@Test
	public void testUpdateSaleInfo_ProductNotFound() {
		when(productRepository.findById(999L)).thenReturn(Optional.empty());

		adminProductService.updateSaleInfo(999L, 800, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 10));

		verify(productRepository, never()).save(any(Product.class));
	}
}
