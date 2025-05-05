package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

}
