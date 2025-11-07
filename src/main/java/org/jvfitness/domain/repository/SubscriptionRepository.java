package org.jvfitness.domain.repository;

import org.jvfitness.domain.entity.Subscription;
import org.jvfitness.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserId(Long userId);

    List<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    List<Subscription> findByBranchId(Long branchId);

    List<Subscription> findByStatus(SubscriptionStatus status);

    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId AND s.branch.id = :branchId " +
            "AND s.status = 'ACTIVE' AND s.endDate >= :currentDate AND s.remainingEntries > 0")
    Optional<Subscription> findActiveSubscription(@Param("userId") Long userId,
                                                  @Param("branchId") Long branchId,
                                                  @Param("currentDate") LocalDate currentDate);

    @Query("SELECT s FROM Subscription s WHERE s.endDate BETWEEN :startDate AND :endDate " +
            "AND s.status = 'ACTIVE'")
    List<Subscription> findExpiringSubscriptions(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT s FROM Subscription s WHERE s.remainingEntries <= :threshold " +
            "AND s.remainingEntries > 0 AND s.status = 'ACTIVE'")
    List<Subscription> findLowEntrySubscriptions(@Param("threshold") Integer threshold);

    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.branch.id = :branchId " +
            "AND s.status = 'ACTIVE' AND s.endDate >= :currentDate")
    Long countActiveSubscriptionsByBranch(@Param("branchId") Long branchId,
                                          @Param("currentDate") LocalDate currentDate);
}