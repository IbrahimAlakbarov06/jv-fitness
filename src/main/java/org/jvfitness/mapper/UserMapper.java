package org.jvfitness.mapper;

import org.jvfitness.domain.entity.User;
import org.jvfitness.model.dto.request.RegisterRequest;
import org.jvfitness.model.dto.request.UpdateUserRequest;
import org.jvfitness.model.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public void updateUserFromRequest(User user, UpdateUserRequest request) {
        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
    }

    public List<UserResponse> toListResponse(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}