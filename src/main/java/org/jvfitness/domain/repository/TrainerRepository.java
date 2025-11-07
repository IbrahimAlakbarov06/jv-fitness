package org.jvfitness.domain.repository;

import org.jvfitness.domain.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    List<Trainer> findByBranchId(Long branchId);

    List<Trainer> findByBranchIdAndIsActiveTrue(Long branchId);

    List<Trainer> findByIsActiveTrue();

    Optional<Trainer> findByIdAndIsActiveTrue(Long id);

    Optional<Trainer> findByEmail(String email);

    Optional<Trainer> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select t from Trainer t where t.branch.id= :branchId and t.experienceYears >= :minyears and t.isActive = true")
    List<Trainer> findExperiencedTrainersByBranch(@Param("branchId") Long branchId, @Param("minYears") Integer minYears);

    @Query("select t from Trainer t where lower(t.firstName) like lower(concat('%', :search, '%') ) " +
            "or lower(t.lastName) like lower(concat('%', :search, '%') )" +
            "or lower(t.bio) like lower(concat('%', :search, '%') )")
    List<Trainer> searchTrainers(@Param("search") String search);

    @Query("select count(u) from User u where u.preferredTrainer= :trainerId")
    Long countClients(@Param("trainerId") Long trainerId);
}
