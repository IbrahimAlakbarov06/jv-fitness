package org.jvfitness.domain.repository;

import org.jvfitness.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserId(Long userId);

    List<Review> findByBranchIdAndIsApprovedTrue(Long branchId);

    List<Review> findByTrainerIdAndIsApprovedTrue(Long trainerId);

    List<Review> findByIsApprovedFalse();

    Optional<Review> findByUserIdAndBranchId(Long userId, Long branchId);

    Optional<Review> findByUserIdAndTrainerId(Long userId, Long trainerId);

    List<Review> findByTrainerId(Long trainerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.branch.id = :branchId AND r.isApproved = true")
    Double calculateAverageRatingForBranch(@Param("branchId") Long branchId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.trainer.id = :trainerId AND r.isApproved = true")
    Double calculateAverageRatingForTrainer(@Param("trainerId") Long trainerId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.branch.id = :branchId AND r.isApproved = true")
    Long countApprovedReviewsForBranch(@Param("branchId") Long branchId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.trainer.id = :trainerId AND r.isApproved = true")
    Long countApprovedReviewsForTrainer(@Param("trainerId") Long trainerId);

    boolean existsByUserIdAndBranchId(Long userId, Long branchId);

    boolean existsByUserIdAndTrainerId(Long userId, Long trainerId);
}
