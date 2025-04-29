package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.Product;
import com.example.demo.service.AdminProductService;

@WebMvcTest(AdminProductController.class)
public class AdminProductControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private AdminProductController adminProductController;

	@MockBean
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

	// 商品一覧表示のテスト
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testListProducts() throws Exception {
		when(adminProductService.findAllProducts()).thenReturn(Arrays.asList(testProduct));
		// モックの設定確認
		System.out.println("Mocked products: " + adminProductService.findAllProducts());

		mockMvc.perform(get("/admin/products"))
				.andExpect(status().isOk())
				.andExpect(view().name("/admin/admin_dashboard"))
				.andExpect(model().attributeExists("products"));
	}

	// 商品追加のテスト
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testAddProduct() throws Exception {
		mockMvc.perform(post("/admin/products/add")
				.with(csrf())//csrfトークン追加
				.param("name", "新しい商品")
				.param("price", "5000")
				.param("stock", "20"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/products"));

		verify(adminProductService, times(1)).saveProduct(any(Product.class));
	}

	// 商品削除のテスト
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testDeleteProduct() throws Exception {
		mockMvc.perform(post("/admin/products/delete/1")
				.with(csrf())) // ← ここ！post()の直後に.with(csrf())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/products"));

		verify(adminProductService, times(1)).deleteProductById(1L);
	}

	// 商品更新のテスト
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testUpdateProduct() throws Exception {
		mockMvc.perform(post("/admin/products/update").with(csrf())
				.param("id", "1")
				.param("name", "更新された商品")
				.param("price", "1500")
				.param("stock", "5"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/products"));

		verify(adminProductService, times(1)).updateProduct(any(Product.class));
	}

	// セール情報更新のテスト
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testUpdateSaleInfo() throws Exception {
		mockMvc.perform(post("/admin/products/1/sale").with(csrf())
				.param("salePrice", "800")
				.param("saleStartDate", "2025-05-01")
				.param("saleEndDate", "2025-05-10"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/products"));

		verify(adminProductService, times(1))
				.updateSaleInfo(eq(1L), eq(800), eq(LocalDate.of(2025, 5, 1)), eq(LocalDate.of(2025, 5, 10)));
	}
}
