package org.jvfitness.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerRequest {

    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Branch ID cannot be empty")
    private Long branchId;

    @NotNull(message = "Experience years cannot be empty")
    @Min(value = 1, message = "Experience years cannot be less than 1")
    private Integer experienceYears;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    private String imageUrl;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @Email(message = "Please enter a valid email")
    private String email;

    private Boolean isActive = true;
}
