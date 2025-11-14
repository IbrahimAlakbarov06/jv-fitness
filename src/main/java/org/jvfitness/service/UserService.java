package org.jvfitness.service;

import lombok.RequiredArgsConstructor;
import org.jvfitness.domain.entity.Branch;
import org.jvfitness.domain.entity.Trainer;
import org.jvfitness.domain.entity.User;
import org.jvfitness.domain.repository.BranchRepository;
import org.jvfitness.domain.repository.TrainerRepository;
import org.jvfitness.domain.repository.UserRepository;
import org.jvfitness.exception.BusinessException;
import org.jvfitness.exception.NotFoundException;
import org.jvfitness.mapper.UserMapper;
import org.jvfitness.model.dto.response.MessageResponse;
import org.jvfitness.model.dto.response.UserResponse;
import org.jvfitness.model.enums.Role;
import org.jvfitness.model.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TrainerRepository trainerRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toListResponse(users);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        return userMapper.toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("User not found with phone number: " + phoneNumber));

        return userMapper.toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);

        return userMapper.toListResponse(users);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByStatus(UserStatus status) {
        List<User> users = userRepository.findByStatus(status);

        return userMapper.toListResponse(users);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getReceptionistByBranch(Long branchId) {
        List<User> users = userRepository.findReceptionistsByBranch(branchId);

        return userMapper.toListResponse(users);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getPreferredTrainers(Long trainerId) {
        List<User> users = userRepository.findPreferredTrainers(trainerId);

        return userMapper.toListResponse(users);
    }

    @Transactional
    public MessageResponse deleteUserById(Long id) {
        User user = findUserById(id);

        userRepository.delete(user);

        return new MessageResponse("User deleted successfully");
    }

    @Transactional
    public MessageResponse updatePassword(Long id, String oldPassword, String newPassword) {
        User user = findUserById(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return new MessageResponse("Password updated successfully");
    }

    @Transactional
    public UserResponse updateUserRole(Long userId, Role role) {
        User user = findUserById(userId);

        user.setRole(role);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse updateUserStatus(Long userId, UserStatus status) {
        User user = findUserById(userId);

        user.setStatus(status);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse assignPreferredTrainer(Long userId, Long trainerId) {
        User user = findUserById(userId);

        Trainer trainer = findTrainerById(trainerId);

        user.setPreferredTrainer(trainer);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse removePreferredTrainer(Long userId, Long trainerId) {
        User user = findUserById(userId);

        Trainer trainer = findTrainerById(trainerId);

        user.setPreferredTrainer(null);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse assignBranchToReceptionist(Long userId, Long branchId) {
        User user = findUserById(userId);

        if (user.getRole() != Role.ROLE_RECEPTIONIST) {
            throw new NotFoundException("User must be a receptionist to be assigned to a branch");
        }

        Branch branch = findBranchById(branchId);

        user.setAssignedBranch(branch);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse removeBranchToReceptionist(Long userId) {
        User user = findUserById(userId);

        user.setAssignedBranch(null);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    private User findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return user;
    }

    private Trainer findTrainerById(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + trainerId));

        return trainer;
    }

    private Branch findBranchById(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + branchId));

        return branch;
    }


}
