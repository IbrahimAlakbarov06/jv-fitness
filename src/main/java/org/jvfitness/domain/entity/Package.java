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

import java.time.LocalDateTime;

@Entity
@Table(name = "packages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageDuration duration;

    @Column(nullable = false)
    private Boolean isActive;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
