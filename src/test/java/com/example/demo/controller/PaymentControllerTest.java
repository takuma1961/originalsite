package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.Payment;
import com.example.demo.entity.Payment.PaymentMethod;
import com.example.demo.service.PaymentService;

@WebMvcTest(PaymentController.class) // Controller層のテスト
class PaymentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private AdminProductController adminProductController;

	@MockBean
	private PaymentService paymentService; // Service層をモック化

	// 新規支払い作成のテスト
	@Test
	void testCreatePayment_Success() throws Exception {
		Payment payment = new Payment(null, null, PaymentMethod.STRIPE, 1000, "dummy-transaction-id");
		Payment savedPayment = new Payment(null, null, PaymentMethod.STRIPE, 1000, "dummy-transaction-id");
		savedPayment.setId(1L);

		when(paymentService.createPayment(any(Payment.class))).thenReturn(savedPayment);

		String requestBody = """
				{
				    "paymentMethod": "STRIPE",
				    "amount": 1000,
				    "transactionId": "dummy-transaction-id"
				}
				""";

		mockMvc.perform(post("/payments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.paymentMethod").value("STRIPE"))
				.andExpect(jsonPath("$.amount").value(1000));
	}

	// すべての支払い情報を取得するテスト
	@Test
	void testGetAllPayments_Success() throws Exception {
		Payment payment1 = new Payment(null, null, PaymentMethod.STRIPE, 1000, "dummy-transaction-id");
		payment1.setId(1L);
		Payment payment2 = new Payment(null, null, PaymentMethod.PAYPAL, 2000, "dummy-transaction-id-2");
		payment2.setId(2L);

		when(paymentService.getAllPayments()).thenReturn(List.of(payment1, payment2));

		mockMvc.perform(get("/payments"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].paymentMethod").value("STRIPE"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].paymentMethod").value("PAYPAL"));
	}

	// 支払い情報の更新テスト
	@Test
	void testUpdatePayment_Success() throws Exception {
		Payment payment = new Payment(null, null, PaymentMethod.STRIPE, 1000, "dummy-transaction-id");
		payment.setId(1L);
		Payment updatedPayment = new Payment(null, null, PaymentMethod.PAYPAL, 2000, "new-dummy-transaction-id");
		updatedPayment.setId(1L);

		when(paymentService.updatePayment(eq(1L), any(Payment.class))).thenReturn(updatedPayment);

		String requestBody = """
				{
				    "paymentMethod": "PAYPAL",
				    "amount": 2000,
				    "transactionId": "new-dummy-transaction-id"
				}
				""";

		mockMvc.perform(put("/payments/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.paymentMethod").value("PAYPAL"))
				.andExpect(jsonPath("$.amount").value(2000));
	}

	// 支払い情報の削除テスト
	@Test
	void testDeletePayment_Success() throws Exception {
		doNothing().when(paymentService).deletePayment(1L);

		mockMvc.perform(delete("/payments/1"))
				.andExpect(status().isNoContent());

		verify(paymentService, times(1)).deletePayment(1L); // deletePaymentが1回呼ばれたことを検証
	}
}
