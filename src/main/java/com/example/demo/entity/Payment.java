package com.example.demo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 支払いID（主キー）

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order; // 注文との関連

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user; // 支払ったユーザーとの関連

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod; // 支払い方法（Enum）

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status", nullable = false)
	private PaymentStatus paymentStatus; // 支払いステータス（Enum）

	@Column(name = "amount", nullable = false)
	private int amount; // 支払金額

	@Column(name = "transaction_id", nullable = false, unique = true)
	private String transactionId; // トランザクションID

	// 作成日時
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	//PaymentMethodの入力を制御
	public enum PaymentMethod {
		STRIPE, PAYPAL, CREDIT_CARD; // 追加した支払い方法
	}

	//PaymentStatusの入力を制御
	public enum PaymentStatus {
		PENDING, // 支払い保留中
		COMPLETED, // 支払い完了
		FAILED; // 支払い失敗
	}

	// コンストラクタ
	public Payment(Order order, User user, PaymentMethod paymentMethod, int amount, String transactionId) {
		this.order = order;
		this.user = user;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.paymentStatus = PaymentStatus.PENDING; // デフォルトは「保留中」
		this.transactionId = transactionId;
	}

	public Payment() {
	} // デフォルトコンストラクタ

	// Getter & Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
