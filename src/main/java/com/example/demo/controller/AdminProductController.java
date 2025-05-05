package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Product;
import com.example.demo.service.AdminProductService;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
	@Autowired
	private AdminProductService adminProductService;

	//商品一覧表示
	@GetMapping
	public String listProducts(Model model) {
		List<Product> products = adminProductService.findAllProducts();
		if (products == null || products.isEmpty()) {
			model.addAttribute("products", new ArrayList<>()); // 空リストを渡す
		} else {
			model.addAttribute("products", products);
		}
		return "admin/admin_dashboard"; // 商品管理画面に移動
	}

	//商品追加
	@PostMapping("/add")
	public String addProduct(@ModelAttribute Product product) {
		adminProductService.saveProduct(product);
		return "redirect:/admin/products"; // ← リダイレクトして一覧表示
	}

	// 商品削除
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		adminProductService.deleteProductById(id);
		return "redirect:/admin/products";
	}

	// 商品更新
	@PostMapping("/update")
	public String updateProduct(@ModelAttribute Product product) {
		adminProductService.updateProduct(product);
		return "redirect:/admin/products";
	}

	// セール情報を更新するエンドポイント
	@PostMapping("/{id}/sale")
	public String updateSaleInfo(
			@PathVariable("id") Long productId,
			@RequestParam("salePrice") Integer salePrice,
			@RequestParam("saleStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate saleStartDate,
			@RequestParam("saleEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate saleEndDate) {
		adminProductService.updateSaleInfo(productId, salePrice, saleStartDate, saleEndDate);
		return "redirect:/admin/products"; // 更新後は管理画面にリダイレクト
	}

	// 在庫数更新
	@PostMapping("/{id}/stock")
	public String updateStock(
			@PathVariable("id") Long productId,
			@RequestParam("stock") int newStock) {
		adminProductService.updateStock(productId, newStock);
		return "redirect:/admin/products";
	}

	//	//商品名、価格更新
	//	@PostMapping("/{id}/edit")
	//	public String updateEdit(
	//			@PathVariable("id") Long productId,
	//			@RequestParam("name") String newName,
	//			@RequestParam("price") int newPrice) {
	//		adminProductService.updateEdit(productId, newName, newPrice);
	//		return "redirect:/admin/products";
	//	}

	@PostMapping("/{id}/edit")
	public String updateProductInfo(@PathVariable Long id,
			@RequestParam(required = false) String imageUrl) {
		adminProductService.updateImagepath(id,imageUrl);
		return "redirect:/admin/products";
	}

}
