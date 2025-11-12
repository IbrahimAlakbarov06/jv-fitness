package org.jvfitness.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jvfitness.model.enums.PackageDuration;
import org.jvfitness.model.enums.PackageType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchPricingRequest {

    @NotNull(message = "Branch ID cannot be empty")
    private Long branchId;

    @NotNull(message = "Package ID cannot be empty")
    private Long packageId;

    @NotNull(message = "Package type cannot be empty")
    private PackageType type;

    @NotNull(message = "Duration cannot be empty")
    private PackageDuration duration;

    @NotNull(message = "Price cannot be empty")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    private Boolean isActive = true;
}
