package com.example.demo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ContactForm {
	@NotBlank(message = "お名前を入力してください。")
	private String name;

	@NotBlank(message = "メールアドレスを入力してください。")
	@Email(message = "正しいメールアドレス形式で入力してください。")
	private String email;

	private String category;

	@NotBlank(message = "お問い合わせ内容を入力してください。")
	@Size(max = 1000, message = "1000文字以内で入力してください。")
	private String message;
	
}
