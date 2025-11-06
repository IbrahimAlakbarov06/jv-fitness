package org.jvfitness.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jvfitness.model.enums.SubscriptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "subscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package packageEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_pricing_id", nullable = false)
    private BranchPricing branchPricing;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_entries", nullable = false)
    private Integer totalEntries;

    @Column(name = "remaining_entries", nullable = false)
    private Integer remainingEntries;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<CheckIn> checkIns = new ArrayList<>();

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE &&
                LocalDate.now().isBefore(endDate.plusDays(1)) &&
                remainingEntries > 0;
    }

    public void decrementEntry() {
        if (remainingEntries > 0) {
            this.remainingEntries--;
        }
        if (remainingEntries == 0) {
            this.status = SubscriptionStatus.EXHAUSTED;
        }
    }
}
