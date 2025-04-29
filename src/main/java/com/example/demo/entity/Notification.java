package com.example.demo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "notifications")
public class Notification {

	//通知ID（主キー）
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//通知を受け取るユーザー
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	//通知メッセージ
	@Column(name = "message", nullable = false, columnDefinition = "TEXT")
	private String message;

	//既読フラグ
	@Column(name = "is_read", nullable = false)
	private Boolean isRead = false;

	//作成日時
	@Column(name = "created_at", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	// Getters and Setters

	public Notification() {
	}

	public Notification(User user, String message) {
		this.user = user;
		this.message = message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	// Getters and Setters

}
