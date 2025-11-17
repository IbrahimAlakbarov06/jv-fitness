package org.jvfitness.mapper;

import org.jvfitness.domain.entity.Branch;
import org.jvfitness.model.dto.request.BranchRequest;
import org.jvfitness.model.dto.response.BranchResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchMapper {

    public BranchResponse toResponse(Branch branch) {
        if (branch == null) {
            return null;
        }

        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .latitude(branch.getLatitude())
                .longitude(branch.getLongitude())
                .openingTime(branch.getOpeningTime())
                .closingTime(branch.getClosingTime())
                .isActive(branch.getIsActive())
                .imageUrls(branch.getImageUrls())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }

    public BranchResponse toResponseWithStats(Branch branch, Integer activeSubscriptionsCount,
                                                    Double averageRating, Long totalReviews) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .latitude(branch.getLatitude())
                .longitude(branch.getLongitude())
                .openingTime(branch.getOpeningTime())
                .closingTime(branch.getClosingTime())
                .isActive(branch.getIsActive())
                .imageUrls(branch.getImageUrls())
                .activeSubscriptionsCount(activeSubscriptionsCount)
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }

    public void updateEntityFromRequest(Branch branch, BranchRequest request) {
        branch.setName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setPhone(request.getPhone());
        branch.setLatitude(request.getLatitude());
        branch.setLongitude(request.getLongitude());
        branch.setOpeningTime(request.getOpeningTime());
        branch.setClosingTime(request.getClosingTime());
        branch.setImageUrls(request.getImageUrls());
        branch.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
    }

    public Branch toEntity(BranchRequest request) {
        if (request == null) {
            return null;
        }

        return Branch.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .imageUrls(request.getImageUrls())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }

    public List<BranchResponse> toListResponse(List<Branch> branches) {
        if (branches == null) {
            return null;
        }

        return branches.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}