package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ContactForm;
import com.example.demo.entity.Inquiry;
import com.example.demo.repository.InquiryRepository;

@Service
public class InquiryService {

	@Autowired
	private InquiryRepository inquiryRepository;

	public void saveInquiry(ContactForm form) {
		Inquiry inquiry = new Inquiry();

		inquiry.setName(form.getName());
		inquiry.setEmail(form.getEmail());
		inquiry.setCategory(form.getCategory());
		inquiry.setMessage(form.getMessage());

		inquiryRepository.save(inquiry);

	}
}
