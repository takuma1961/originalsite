package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

@Controller
public class ProductController {
	public final ProductService productService;

	//productServiceの機能を本クラスで使用可能にするコンストラクタ
	public ProductController(ProductService productService) {
		this.productService = productService;

	}

	//商品情報を全件取得するメソッド
	@GetMapping("/products")
	public String showProductList(Model model) {
		List<Product> productList = productService.findAllproduct();
		model.addAttribute("products", productList);
		return "shop_page";//shop_page.htmlへ
	}

}
