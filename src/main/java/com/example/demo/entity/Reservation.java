package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {

	//予約ID（主キー）
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//予約したユーザー
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	//予約された商品
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	//予約日
	@Column(name = "reservation_date", nullable = false) //クラス内の変数名がDBと異なる場合にDBのreservation_dateと関連付けている
	private LocalDate reservationDate;

	//予約ステータス（未確定、確定、キャンセル）
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private Status status;

	//作成日時
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	//作成日時取得メソッド
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	//予約ステータスの種類割り当て
	public enum Status {
		PENDING, CONFIRMED, CANCELLED
	}

	// --- コンストラクタ ---
	public Reservation() {
	}

	public Reservation(User user, Product product, LocalDate reservationDate, Status status) {
		this.user = user;
		this.product = product;
		this.reservationDate = reservationDate;
		this.status = status;
		this.createdAt = LocalDateTime.now();
	}

	// --- Getter & Setter ---
	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public LocalDate getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
