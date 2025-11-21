package org.jvfitness.mapper;

import org.jvfitness.domain.entity.Branch;
import org.jvfitness.domain.entity.BranchPricing;
import org.jvfitness.domain.entity.Package;
import org.jvfitness.model.dto.request.BranchPricingRequest;
import org.jvfitness.model.dto.response.BranchPricingResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchPricingMapper {

    public BranchPricing toEntity(BranchPricingRequest request, Branch branch, Package packageEntity) {
        if (request == null) {
            return null;
        }

        return BranchPricing.builder()
                .branch(branch)
                .packageEntity(packageEntity)
                .packageType(request.getType())
                .duration(request.getDuration())
                .price(request.getPrice())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }




    public BranchPricingResponse toResponse(BranchPricing branchPricing) {
        if (branchPricing == null) {
            return null;
        }

        return BranchPricingResponse.builder()
                .id(branchPricing.getId())
                .branchId(branchPricing.getBranch().getId())
                .branchName(branchPricing.getBranch().getName())
                .packageId(branchPricing.getPackageEntity().getId())
                .packageName(branchPricing.getPackageEntity().getName())
                .packageType(branchPricing.getPackageType())
                .duration(branchPricing.getDuration())
                .price(branchPricing.getPrice())
                .isActive(branchPricing.getIsActive())
                .createdAt(branchPricing.getCreatedAt())
                .updatedAt(branchPricing.getUpdatedAt())
                .build();
    }

    public void updateEntityFromRequest(BranchPricing branchPricing, BranchPricingRequest request,
                                        Branch branch, Package packageEntity) {
        branchPricing.setBranch(branch);
        branchPricing.setPackageEntity(packageEntity);
        branchPricing.setPackageType(request.getType());
        branchPricing.setDuration(request.getDuration());
        branchPricing.setPrice(request.getPrice());
        branchPricing.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
    }

    public List<BranchPricingResponse> toListResponse(List<BranchPricing> branchPricings) {
        return branchPricings.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
