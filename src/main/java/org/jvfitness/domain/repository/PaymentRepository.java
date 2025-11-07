package org.jvfitness.domain.repository;

import org.jvfitness.domain.entity.Payment;
import org.jvfitness.model.enums.PaymentMethod;
import org.jvfitness.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);

    List<Payment> findBySubscriptionId(Long subscriptionId);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByMethod(PaymentMethod method);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByUserIdAndStatus(Long userId, PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsBetween(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Payment p WHERE p.subscription.branch.id = :branchId " +
            "AND p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsByBranchBetween(@Param("branchId") Long branchId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.subscription.branch.id = :branchId " +
            "AND p.status = 'COMPLETED' AND p.completedAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateBranchRevenue(@Param("branchId") Long branchId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED' " +
            "AND p.completedAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalRevenue(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status " +
            "AND p.createdAt BETWEEN :startDate AND :endDate")
    Long countPaymentsByStatusBetween(@Param("status") PaymentStatus status,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}