package org.jvfitness.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
