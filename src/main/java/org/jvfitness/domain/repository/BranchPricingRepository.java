package org.jvfitness.domain.repository;

import org.jvfitness.domain.entity.BranchPricing;
import org.jvfitness.model.enums.PackageDuration;
import org.jvfitness.model.enums.PackageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BranchPricingRepository extends JpaRepository<BranchPricing, Long> {

    List<BranchPricing> findByBranchId(Long branchId);

    List<BranchPricing> findByPackageEntityId(Long packageId);

    Optional<BranchPricing> findByBranchIdAndPackageEntityId(Long branchId, Long packageId);

    @Query("select br from BranchPricing br where br.branch.id = :branchId and br.packageType = :type and br.duration= :duration")
    Optional<BranchPricing> findByBranchIdAndTypeAndDuration(@Param("branchId") Long branchId,
                                                             @Param("type") PackageType type,
                                                             @Param("duration") PackageDuration duration);

    @Query("select br from BranchPricing br where br.branch.id = :branchId and br.packageType= :type and br.isActive = true")
    List<BranchPricing> findByBranchAndPackageType(@Param("branchId") Long branchId, @Param("type") PackageType type);

    boolean existsByBranchIdAndPackageEntityId(Long branchId, Long packageId);
}
