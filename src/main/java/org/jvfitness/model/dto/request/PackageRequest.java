package org.jvfitness.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jvfitness.model.enums.PackageDuration;
import org.jvfitness.model.enums.PackageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageRequest {

    @NotBlank(message = "Package name cannot be empty")
    private String name;

    @NotNull(message = "Package type cannot be empty")
    private PackageType type;

    @NotNull(message = "Duration cannot be empty")
    private PackageDuration duration;

    private Boolean isActive = true;
}
