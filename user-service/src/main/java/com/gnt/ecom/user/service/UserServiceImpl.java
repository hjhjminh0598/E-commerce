package com.gnt.ecom.user.service;

import com.gnt.ecom.base.BaseServiceImpl;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.user.dto.CreateUserRequest;
import com.gnt.ecom.user.dto.UpdateUserRequest;
import com.gnt.ecom.user.dto.UserDTO;
import com.gnt.ecom.user.entity.User;
import com.gnt.ecom.user.event.UserCreatedEvent;
import com.gnt.ecom.user.producer.UserProducer;
import com.gnt.ecom.user.repository.UserRepository;
import com.gnt.ecom.user_authentication.config.JwtProvider;
import com.gnt.ecom.user_authentication.dto.OAuth2Response;
import com.gnt.ecom.user_authentication.entity.MyUserDetails;
import com.gnt.ecom.utils.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UUID> implements UserService {

    private final static int MIN_USERNAME_LENGTH = 4;

    private final static int MIN_PASSWORD_LENGTH = 6;

    private final UserRepository userRepository;

    private final UserProducer userProducer;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository, UserProducer userProducer, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        super(userRepository);
        this.userRepository = userRepository;
        this.userProducer = userProducer;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public MyUserDetails loadUserByUsername(String username) {
        User user = getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return MyUserDetails.builder()
                .user(user)
                .build();
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public PageResponse<UserDTO> getAllUsers(Pageable pageable) {
        return PageResponse.from(super.findAll(pageable).map(UserDTO::of));
    }

    @Override
    public UserDTO getById(UUID id) {
        return super.findById(id).map(UserDTO::of).orElse(null);
    }

    public UserDTO create(CreateUserRequest request) {
        validateCreateRequest(request);
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = super.save(user);
        userProducer.publishUserCreatedEvent(UserCreatedEvent.of(user));

        return UserDTO.of(user);
    }

    private void validateCreateRequest(CreateUserRequest request) {
        if (StringUtils.isBlank(request.getUsername()) || request.getUsername().length() < MIN_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username must be at least 4 characters long");
        }

        if (StringUtils.isBlank(request.getEmail())) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (StringUtils.isBlank(request.getPassword()) || request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        request.setUsername(request.getUsername().toLowerCase().trim());
        request.setEmail(request.getEmail().toLowerCase().trim());
        request.setPassword(request.getPassword().trim());
    }

    public UserDTO update(UUID id, UpdateUserRequest request) {
        return UserDTO.of(super.findById(id)
                .map(user -> {
                    user.setPhoneNumber(request.getPhoneNumber());
                    if (StringUtils.isNotBlank(request.getPassword()) && request.getPassword().length() >= MIN_PASSWORD_LENGTH) {
                        user.setPassword(passwordEncoder.encode(request.getPassword()));
                    }
                    return save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public OAuth2Response loginWithGoogle(OAuth2AuthenticationToken authentication) {
        OAuth2User oauth2User = authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String provider = authentication.getAuthorizedClientRegistrationId();

        String token = jwtProvider.generateToken(email, provider);
        return OAuth2Response.builder()
                .email(email)
                .token(token)
                .provider(provider)
                .build();
    }
}
