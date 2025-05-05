package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.DTO.ContactForm;
import com.example.demo.service.InquiryService;

import jakarta.validation.Valid;

@Controller
public class ContactController {

	@Autowired
	private InquiryService inquiryService;

	@GetMapping("/contact")
	public String showContactForm(Model model) {
		model.addAttribute("contactForm", new ContactForm());
		return "contact";
	}

	@PostMapping("/contact/submit")
	public String submitContactForm(@ModelAttribute("contactForm") @Valid ContactForm form, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "contact";
		}

		inquiryService.saveInquiry(form);
		model.addAttribute("message", "お問い合わせありがとうございました。");
		return "contact_success"; // 成功画面を別途作る想定
	}
}
