package org.jvfitness.domain.repository;

import org.jvfitness.model.enums.PackageDuration;
import org.jvfitness.model.enums.PackageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findByIsActiveTrue();

    Optional<Package> findByIdAndIsActiveTrue(Long id);

    List<Package> findByType(PackageType type);

    List<Package> findByDuration(PackageDuration duration);

    List<Package> findByTypeAndDuration(PackageType type, PackageDuration duration);

    Optional<Package> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Package> findByTypeAndIsActiveTrue(PackageType type);}
