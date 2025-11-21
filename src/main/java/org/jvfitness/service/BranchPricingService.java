package org.jvfitness.service;

import lombok.RequiredArgsConstructor;
import org.jvfitness.domain.entity.Branch;
import org.jvfitness.domain.entity.BranchPricing;
import org.jvfitness.domain.entity.Package;
import org.jvfitness.domain.repository.BranchPricingRepository;
import org.jvfitness.domain.repository.BranchRepository;
import org.jvfitness.domain.repository.PackageRepository;
import org.jvfitness.exception.AlreadyExistsException;
import org.jvfitness.exception.NotFoundException;
import org.jvfitness.mapper.BranchPricingMapper;
import org.jvfitness.model.dto.request.BranchPricingRequest;
import org.jvfitness.model.dto.response.BranchPricingResponse;
import org.jvfitness.model.dto.response.MessageResponse;
import org.jvfitness.model.enums.PackageDuration;
import org.jvfitness.model.enums.PackageType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchPricingService {

    private final BranchPricingRepository branchPricingRepository;
    private final BranchRepository branchRepository;
    private final PackageRepository packageRepository;
    private final BranchPricingMapper branchPricingMapper;

    @Transactional(readOnly = true)
    public List<BranchPricingResponse> getAllBranchPricings() {
        List<BranchPricing> branchPricings = branchPricingRepository.findAll();
        return branchPricingMapper.toListResponse(branchPricings);
    }

    @Transactional(readOnly = true)
    public BranchPricingResponse getBranchPricingById(Long id) {
        BranchPricing branchPricing = findBranchPricingById(id);
        return branchPricingMapper.toResponse(branchPricing);
    }

    @Transactional(readOnly = true)
    public List<BranchPricingResponse> getBranchPricingsByBranchId(Long branchId) {
        List<BranchPricing> branchPricings = branchPricingRepository.findByBranchId(branchId);
        return branchPricingMapper.toListResponse(branchPricings);
    }

    @Transactional(readOnly = true)
    public List<BranchPricingResponse> getBranchPricingsByPackageId(Long packageId) {
        List<BranchPricing> branchPricings = branchPricingRepository.findByPackageEntityId(packageId);
        return branchPricingMapper.toListResponse(branchPricings);
    }

    @Transactional(readOnly = true)
    public BranchPricingResponse getBranchPricingByBranchAndPackage(Long branchId, Long packageId) {
        BranchPricing branchPricing = branchPricingRepository.findByBranchIdAndPackageEntityId(branchId, packageId)
                .orElseThrow(() -> new NotFoundException("Branch pricing not found for branch id: " + branchId + " and package id: " + packageId));
        return branchPricingMapper.toResponse(branchPricing);
    }

    @Transactional(readOnly = true)
    public BranchPricingResponse getBranchPricingByBranchAndTypeAndDuration(Long branchId, PackageType type, PackageDuration duration) {
        BranchPricing branchPricing = branchPricingRepository.findByBranchIdAndTypeAndDuration(branchId, type, duration)
                .orElseThrow(() -> new NotFoundException("Branch pricing not found for branch id: " + branchId + ", type: " + type + " and duration: " + duration));
        return branchPricingMapper.toResponse(branchPricing);
    }

    @Transactional(readOnly = true)
    public List<BranchPricingResponse> getBranchPricingsByBranchAndPackageType(Long branchId, PackageType type) {
        List<BranchPricing> branchPricings = branchPricingRepository.findByBranchAndPackageType(branchId, type);
        return branchPricingMapper.toListResponse(branchPricings);
    }

    @Transactional
    public BranchPricingResponse createBranchPricing(BranchPricingRequest request) {
        Branch branch = findBranchById(request.getBranchId());
        Package packageEntity = findPackageById(request.getPackageId());

        if (branchPricingRepository.existsByBranchIdAndPackageEntityId(request.getBranchId(), request.getPackageId())) {
            throw new AlreadyExistsException("Branch pricing already exists for branch id: " + request.getBranchId() + " and package id: " + request.getPackageId());
        }

        BranchPricing branchPricing = branchPricingMapper.toEntity(request, branch, packageEntity);
        BranchPricing savedBranchPricing = branchPricingRepository.save(branchPricing);

        return branchPricingMapper.toResponse(savedBranchPricing);
    }

    @Transactional
    public BranchPricingResponse updateBranchPricing(Long id, BranchPricingRequest request) {
        BranchPricing branchPricing = findBranchPricingById(id);
        Branch branch = findBranchById(request.getBranchId());
        Package packageEntity = findPackageById(request.getPackageId());

        boolean isDifferentBranchOrPackage = !branchPricing.getBranch().getId().equals(request.getBranchId()) ||
                !branchPricing.getPackageEntity().getId().equals(request.getPackageId());

        if (isDifferentBranchOrPackage && branchPricingRepository.existsByBranchIdAndPackageEntityId(request.getBranchId(), request.getPackageId())) {
            throw new AlreadyExistsException("Branch pricing already exists for branch id: " + request.getBranchId() + " and package id: " + request.getPackageId());
        }

        branchPricingMapper.updateEntityFromRequest(branchPricing, request, branch, packageEntity);
        BranchPricing updatedBranchPricing = branchPricingRepository.save(branchPricing);

        return branchPricingMapper.toResponse(updatedBranchPricing);
    }

    @Transactional
    public MessageResponse deleteBranchPricing(Long id) {
        BranchPricing branchPricing = findBranchPricingById(id);
        branchPricingRepository.delete(branchPricing);
        return new MessageResponse("Branch pricing deleted successfully");
    }

    @Transactional
    public BranchPricingResponse activateBranchPricing(Long id) {
        BranchPricing branchPricing = findBranchPricingById(id);
        branchPricing.setIsActive(true);
        BranchPricing updatedBranchPricing = branchPricingRepository.save(branchPricing);
        return branchPricingMapper.toResponse(updatedBranchPricing);
    }

    @Transactional
    public BranchPricingResponse deactivateBranchPricing(Long id) {
        BranchPricing branchPricing = findBranchPricingById(id);
        branchPricing.setIsActive(false);
        BranchPricing updatedBranchPricing = branchPricingRepository.save(branchPricing);
        return branchPricingMapper.toResponse(updatedBranchPricing);
    }

    private BranchPricing findBranchPricingById(Long id) {
        return branchPricingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Branch pricing not found with id: " + id));
    }

    private Branch findBranchById(Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + branchId));
    }

    private Package findPackageById(Long packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(() -> new NotFoundException("Package not found with id: " + packageId));
    }
}