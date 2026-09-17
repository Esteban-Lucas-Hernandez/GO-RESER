package com.example.back.repo.payment;

import com.example.back.models.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByReferenciaPago(String referenciaPago);
    List<Payment> findByReservaIdReserva(Integer reservaId);
}
