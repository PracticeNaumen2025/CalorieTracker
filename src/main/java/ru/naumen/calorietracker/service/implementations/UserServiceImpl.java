package ru.naumen.calorietracker.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.naumen.calorietracker.dto.UserRegisterRequest;
import ru.naumen.calorietracker.dto.UserResponse;
import ru.naumen.calorietracker.dto.UserUpdateRequest;
import ru.naumen.calorietracker.exception.UserAlreadyExistsException;
import ru.naumen.calorietracker.mapper.UserMapper;
import ru.naumen.calorietracker.model.Role;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.UserRepository;
import ru.naumen.calorietracker.service.MinioService;
import ru.naumen.calorietracker.service.RoleService;
import ru.naumen.calorietracker.service.UserService;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final MinioService minioService;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            RoleService roleService,
            MinioService minioService
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.minioService = minioService;
    }

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest request) {
        validateUserDoesNotExist(request.username());

        User user = createUserFromRequest(request);
        assignDefaultRole(user);
        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Cacheable(value = "userProfile", key = "#username")
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь с юзернеймом \"%s\" не найден".formatted(username)));
        return userMapper.toUserResponse(user);
    }

    @Override
    @CacheEvict(value = "userProfile", key = "#username")
    @Transactional
    public UserResponse updateUserProfile(String username, UserUpdateRequest updateRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        if (updateRequest.getDateOfBirth() != null) {
            user.setDateOfBirth(updateRequest.getDateOfBirth());
        }
        if (updateRequest.getGender() != null) {
            user.setGender(updateRequest.getGender());
        }
        if (updateRequest.getHeightCm() != null) {
            user.setHeightCm(updateRequest.getHeightCm());
        }
        if (updateRequest.getWeightKg() != null) {
            user.setWeightKg(updateRequest.getWeightKg());
        }
        if (updateRequest.getActivityLevel() != null) {
            user.setActivityLevel(updateRequest.getActivityLevel());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    @CacheEvict(value = "userProfile", key = "#username")
    @Transactional
    public UserResponse updateUserPhoto(String username, MultipartFile file) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        if (user.getPhotoUrl() != null && !user.getPhotoUrl().isBlank()) {
            minioService.deleteIconFile(user.getPhotoUrl());
        }

        String url = minioService.storeIconFile(file);
        user.setPhotoUrl(url);

        User updated = userRepository.save(user);
        return userMapper.toUserResponse(updated);
    }

    private void assignDefaultRole(User user) {
        Role defaultRole = roleService.getDefaultUserRole();
        user.getRoles().add(defaultRole);
    }

    private void validateUserDoesNotExist(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(
                    "Пользователь с юзернеймом \"%s\" уже существует!"
                            .formatted(username));
        }
    }

    private User createUserFromRequest(UserRegisterRequest request) {
        User user = userMapper.toUser(request);
        String encodedPassword = passwordEncoder.encode(request.password());
        user.setPasswordHash(encodedPassword);
        return user;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Пользователь с юзернеймом: \"%s\" не найден"
                                .formatted(username)));
    }
}
