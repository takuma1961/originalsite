package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Payment;
import com.example.demo.entity.Payment.PaymentMethod;
import com.example.demo.entity.Payment.PaymentStatus;
import com.example.demo.repository.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private PaymentService paymentService;

	private Payment payment;

	@BeforeEach
	void setUp() {
		// テスト用の Payment オブジェクトの初期化
		// ※ Order や User は本来のオブジェクトですが、必要な場合はモックまたはダミーで用意してください
		payment = new Payment(null, null, PaymentMethod.STRIPE, 1000, "txn-001");
		payment.setId(1L);
	}

	@Test
	void testCreatePayment_Success() {
		// paymentRepository.saveが呼ばれたときに payment を返すように設定
		when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

		Payment created = paymentService.createPayment(payment);
		assertNotNull(created);
		assertEquals(1L, created.getId());
		assertEquals(PaymentMethod.STRIPE, created.getPaymentMethod());
		assertEquals(1000, created.getAmount());

		verify(paymentRepository, times(1)).save(payment);
	}

	@Test
	void testGetAllPayments_Success() {
		Payment payment2 = new Payment(null, null, PaymentMethod.PAYPAL, 2000, "txn-002");
		payment2.setId(2L);

		when(paymentRepository.findAll()).thenReturn(List.of(payment, payment2));

		List<Payment> payments = paymentService.getAllPayments();
		assertNotNull(payments);
		assertEquals(2, payments.size());
		assertEquals(1L, payments.get(0).getId());
		assertEquals(2L, payments.get(1).getId());

		verify(paymentRepository, times(1)).findAll();
	}

	@Test
	void testUpdatePayment_Success() {
		Payment updatedDetails = new Payment(null, null, PaymentMethod.PAYPAL, 1500, "txn-updated");
		updatedDetails.setId(1L);

		// 存在する支払い情報を返す
		when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
		// 更新後の Payment を返す
		Payment updatedPayment = new Payment(null, null, PaymentMethod.PAYPAL, 1500, "txn-updated");
		updatedPayment.setId(1L);
		when(paymentRepository.save(any(Payment.class))).thenReturn(updatedPayment);

		Payment result = paymentService.updatePayment(1L, updatedDetails);
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(PaymentMethod.PAYPAL, result.getPaymentMethod());
		assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus()); // デフォルト値は PENDING のまま
		assertEquals("txn-updated", result.getTransactionId());
		assertEquals(1500, result.getAmount());

		verify(paymentRepository, times(1)).findById(1L);
		verify(paymentRepository, times(1)).save(payment);
	}

	@Test
	void testUpdatePayment_NotFound() {
		Payment updatedDetails = new Payment(null, null, PaymentMethod.PAYPAL, 1500, "txn-updated");

		when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

		Exception exception = assertThrows(EntityNotFoundException.class, () -> {
			paymentService.updatePayment(1L, updatedDetails);
		});
		String expectedMessage = "Payment not found with id:";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		verify(paymentRepository, times(1)).findById(1L);
		verify(paymentRepository, never()).save(any(Payment.class));
	}

	@Test
	void testDeletePayment_Success() {
		when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
		doNothing().when(paymentRepository).delete(any(Payment.class));

		assertDoesNotThrow(() -> paymentService.deletePayment(1L));
		verify(paymentRepository, times(1)).findById(1L);
		verify(paymentRepository, times(1)).delete(payment);
	}

	@Test
	void testDeletePayment_NotFound() {
		when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

		Exception exception = assertThrows(EntityNotFoundException.class, () -> {
			paymentService.deletePayment(1L);
		});
		String expectedMessage = "Payment not found with id:";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		verify(paymentRepository, times(1)).findById(1L);
		verify(paymentRepository, never()).delete(any(Payment.class));
	}
}
