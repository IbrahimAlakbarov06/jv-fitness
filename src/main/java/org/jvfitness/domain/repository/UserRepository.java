package org.jvfitness.domain.repository;

import org.jvfitness.domain.entity.User;
import org.jvfitness.model.enums.Role;
import org.jvfitness.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    List<User> findByRole(Role role);

    List<User> findByStatus(UserStatus status);

    @Query("select u from User u where u.assignedBranch = :branchId and u.role = 'ROLE_RECEPTIONIST'")
    List<User> findReceptionistsByBranch(@Param("branchId") Long branchId);

    @Query("select u from User u where u.preferredTrainer = :trainerId")
    List<User> findPreferredTrainers(@Param("trainerId") Long trainerId);
}
