package org.jvfitness.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchRequest {

    @NotBlank(message = "Branch name cannot be empty")
    @Size(min = 2, max = 100, message = "Branch name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @NotBlank(message = "Phone number cannot be empty")
    @Size(min = 7, max = 20, message = "Phone number must be between 7 and 20 characters")
    private String phone;

    @NotNull(message = "Latitude cannot be empty")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    @NotNull(message = "Longitude cannot be empty")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    @NotNull(message = "Opening time cannot be empty")
    private LocalTime openingTime;

    @NotNull(message = "Closing time cannot be empty")
    private LocalTime closingTime;

    private List<String> imageUrls;

    private Boolean isActive = true;
}
