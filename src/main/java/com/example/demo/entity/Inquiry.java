package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "inquiries")
@Data
public class Inquiry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	private String category;

	@Column(columnDefinition = "TEXT")
	private String message;

	@Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "timestamp default current_timestamp")
	private LocalDateTime createdAt;

	
}
