package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PaymentService {
	private final PaymentRepository paymentRepository;

	//PaymentRepository依存性の注入-DB操作を行うため
	@Autowired
	public PaymentService(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	//新規レコードを挿入
	public Payment createPayment(Payment payment) {
		return paymentRepository.save(payment);
	}

	//DBからレコードを取得
	public List<Payment> getAllPayments() {
		return paymentRepository.findAll();
	}

	//更新処理
	public Payment updatePayment(Long id, Payment paymentDetails) {

		//該当IDが見つからない場合はエラー
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Payment not found with id:" + id));

		payment.setPaymentMethod(paymentDetails.getPaymentMethod());
		payment.setPaymentStatus(paymentDetails.getPaymentStatus());
		payment.setTransactionId(paymentDetails.getTransactionId());

		return paymentRepository.save(payment);
	}
	
	//削除処理
	public void deletePayment(Long id) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Payment not found with id:" + id));
		
		paymentRepository.delete(payment);
	}
}
