package org.jvfitness.mapper;

import org.jvfitness.domain.entity.Package;
import org.jvfitness.model.dto.request.PackageRequest;
import org.jvfitness.model.dto.response.PackageResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PackageMapper {

    public Package toEntity(PackageRequest request) {
        if (request == null) {
            return null;
        }

        return Package.builder()
                .name(request.getName())
                .type(request.getType())
                .duration(request.getDuration())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }

    public PackageResponse toResponse(Package packageEntity) {
        if (packageEntity == null) {
            return null;
        }

        return PackageResponse.builder()
                .id(packageEntity.getId())
                .name(packageEntity.getName())
                .type(packageEntity.getType())
                .duration(packageEntity.getDuration())
                .isActive(packageEntity.getIsActive())
                .createdAt(packageEntity.getCreatedAt())
                .updatedAt(packageEntity.getUpdatedAt())
                .build();

    }

    public void updateEntityFromRequest(Package packageEntity, PackageRequest request) {
        packageEntity.setName(request.getName());
        packageEntity.setType(request.getType());
        packageEntity.setDuration(request.getDuration());
        packageEntity.setIsActive(request.getIsActive());
    }

    public List<PackageResponse> toListResponse(List<Package> packages) {
        return packages.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
