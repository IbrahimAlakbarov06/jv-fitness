package org.jvfitness.domain.repository;

import org.jvfitness.domain.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    List<CheckIn> findByUserId(Long userId);

    List<CheckIn> findByBranchId(Long branchId);

    List<CheckIn> findByReceptionistId(Long receptionistId);

    List<CheckIn> findBySubscriptionId(Long subscriptionId);

    @Query("SELECT c FROM CheckIn c WHERE c.user.id = :userId AND c.checkOutTime IS NULL")
    Optional<CheckIn> findActiveCheckIn(@Param("userId") Long userId);

    @Query("SELECT c FROM CheckIn c WHERE c.branch.id = :branchId AND c.checkOutTime IS NULL")
    List<CheckIn> findActiveCheckInsByBranch(@Param("branchId") Long branchId);

    @Query("SELECT c FROM CheckIn c WHERE c.checkInTime BETWEEN :startTime AND :endTime")
    List<CheckIn> findCheckInsBetween(@Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    @Query("SELECT c FROM CheckIn c WHERE c.branch.id = :branchId " +
            "AND c.checkInTime BETWEEN :startTime AND :endTime")
    List<CheckIn> findCheckInsByBranchBetween(@Param("branchId") Long branchId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(c) FROM CheckIn c WHERE c.branch.id = :branchId " +
            "AND c.checkInTime BETWEEN :startTime AND :endTime")
    Long countCheckInsByBranchBetween(@Param("branchId") Long branchId,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(DISTINCT c.user.id) FROM CheckIn c WHERE c.branch.id = :branchId " +
            "AND c.checkInTime BETWEEN :startTime AND :endTime")
    Long countUniqueVisitorsByBranchBetween(@Param("branchId") Long branchId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    boolean existsByUserIdAndCheckOutTimeIsNull(Long userId);
}