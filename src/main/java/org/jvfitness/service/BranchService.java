package org.jvfitness.service;

import lombok.RequiredArgsConstructor;
import org.jvfitness.domain.entity.Branch;
import org.jvfitness.domain.repository.BranchRepository;
import org.jvfitness.domain.repository.ReviewRepository;
import org.jvfitness.domain.repository.SubscriptionRepository;
import org.jvfitness.exception.AlreadyExistsException;
import org.jvfitness.exception.BusinessException;
import org.jvfitness.exception.NotFoundException;
import org.jvfitness.mapper.BranchMapper;
import org.jvfitness.model.dto.request.BranchRequest;
import org.jvfitness.model.dto.response.BranchResponse;
import org.jvfitness.model.dto.response.MessageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private SubscriptionRepository subscriptionRepository;
    private ReviewRepository reviewRepository;


    @Transactional(readOnly = true)
    public List<BranchResponse> getAllBranches() {
        List<Branch> branches = branchRepository.findAll();
        return branchMapper.toListResponse(branches);
    }

    @Transactional(readOnly = true)
    public List<BranchResponse> getAllActiveBranches() {
        List<Branch> branches = branchRepository.findByIsActiveTrue();
        return branchMapper.toListResponse(branches);
    }

    @Transactional(readOnly = true)
    public BranchResponse getBranchById(Long id) {
        Branch branch = findBranchById(id);
        return branchMapper.toResponse(branch);
    }

    @Transactional(readOnly = true)
    public BranchResponse getBranchByIdWithStats(Long id) {
        Branch branch = findBranchById(id);

        Long activeSubscriptions = subscriptionRepository.countActiveSubscriptionsByBranch(id, LocalDate.now());

        Double averageRating = reviewRepository.calculateAverageRatingForBranch(id);
        Long totalReviews =reviewRepository.countApprovedReviewsForBranch(id);

        return branchMapper.toResponseWithStats(
                branch,
                activeSubscriptions.intValue(),
                averageRating != null ? averageRating : 0.0,
                totalReviews
        );    }

    @Transactional
    public BranchResponse createBranch(BranchRequest request) {
       validateBranchRequest(request);

        if (branchRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AlreadyExistsException("Branch already exists with name " + request.getName());
        }

        Branch branch = branchMapper.toEntity(request);
        Branch savedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(savedBranch);
    }

    @Transactional
    public BranchResponse updateBranch(Long id, BranchRequest request) {
        validateBranchRequest(request);

        Branch branch = findBranchById(id);

        if (branchRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AlreadyExistsException("Branch already exists with name " + request.getName());
        }

        branchMapper.updateEntityFromRequest(branch, request);
        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(updatedBranch);
    }

    @Transactional
    public MessageResponse deleteBranch(Long id) {
        Branch branch = findBranchById(id);

        branchRepository.delete(branch);

        return new MessageResponse("Branch deleted successfully");
    }

    @Transactional
    public BranchResponse activateBranch(Long id) {
        Branch branch = findBranchById(id);

        branch.setIsActive(true);
        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(updatedBranch);
    }

    @Transactional
    public BranchResponse deactivateBranch(Long id) {
        Branch branch = findBranchById(id);

        branch.setIsActive(false);
        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(updatedBranch);
    }

    @Transactional
    public BranchResponse addImage(Long id, String imageUrl) {
        Branch branch = findBranchById(id);

        branch.getImageUrls().add(imageUrl);
        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(updatedBranch);
    }

    @Transactional
    public BranchResponse removeImage(Long id, String imageUrl) {
        Branch branch = findBranchById(id);

        if (!branch.getImageUrls().contains(imageUrl)) {
            throw new NotFoundException("Image URL not found in branch");
        }

        branch.getImageUrls().remove(imageUrl);
        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(updatedBranch);
    }

    private Branch findBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + id));
    }

    private void validateBranchRequest(BranchRequest request) {
        if (request.getOpeningTime().isAfter(request.getClosingTime())) {
            throw new BusinessException("Opening time cannot be after closing time");
        }
    }
}
