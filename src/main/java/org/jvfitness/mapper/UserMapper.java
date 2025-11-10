package org.jvfitness.mapper;

import org.jvfitness.domain.entity.User;
import org.jvfitness.model.dto.request.RegisterRequest;
import org.jvfitness.model.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .preferredTrainerId(user.getPreferredTrainer() != null ? user.getPreferredTrainer().getId() : null)
                .preferredTrainerName(user.getPreferredTrainer() != null ?
                        user.getPreferredTrainer().getFirstName() + " " + user.getPreferredTrainer().getLastName() : null)
                .assignedBranchId(user.getAssignedBranch() != null ? user.getAssignedBranch().getId() : null)
                .assignedBranchName(user.getAssignedBranch() != null ? user.getAssignedBranch().getName() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
}