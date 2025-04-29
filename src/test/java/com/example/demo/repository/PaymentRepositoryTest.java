package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.entity.Order;
import com.example.demo.entity.Order.OrderStatus;
import com.example.demo.entity.Payment;
import com.example.demo.entity.Payment.PaymentMethod;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSaveAndFindPayment() {
        // ユーザー作成・保存
        User user = new User("takuma1999@outlook.com", "password1111", Role.ADMIN);
        user = entityManager.persistAndFlush(user);

        // 注文作成・保存
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(BigDecimal.valueOf(1000));
        order.setStatus(OrderStatus.PENDING);
        order = entityManager.persistAndFlush(order);

        // 支払い作成・保存
        Payment payment = new Payment(order, user, PaymentMethod.STRIPE, 1000, "txn-test-001");
        Payment savedPayment = paymentRepository.save(payment);

        assertNotNull(savedPayment.getId());

        Optional<Payment> found = paymentRepository.findById(savedPayment.getId());
        assertTrue(found.isPresent());
        assertEquals(1000, found.get().getAmount());
        assertEquals("txn-test-001", found.get().getTransactionId());
    }

    @Test
    public void testDeletePayment() {
        // ユーザー作成・保存
        User user = new User("testuser@example.com", "password123", Role.USER);
        user = entityManager.persistAndFlush(user);

        // 注文作成・保存
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(BigDecimal.valueOf(2000));
        order.setStatus(OrderStatus.PENDING);
        order = entityManager.persistAndFlush(order);

        // 支払い作成・保存
        Payment payment = new Payment(order, user, PaymentMethod.PAYPAL, 2000, "txn-test-002");
        Payment savedPayment = paymentRepository.save(payment);

        Long paymentId = savedPayment.getId();
        assertNotNull(paymentId);

        // 削除
        paymentRepository.delete(savedPayment);
        entityManager.flush();

        // 検証
        Optional<Payment> found = paymentRepository.findById(paymentId);
        assertFalse(found.isPresent());
    }
}
