package org.jvfitness.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jvfitness.model.enums.PackageDuration;
import org.jvfitness.model.enums.PackageType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "branch_pricing")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package packageEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageType packageType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageDuration duration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
