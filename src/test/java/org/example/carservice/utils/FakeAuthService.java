package org.example.carservice.utils;

import lombok.RequiredArgsConstructor;
import org.example.authservice.entity.Role;
import org.example.authservice.entity.User;
import org.example.authservice.filter.JwtService;
import org.example.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FakeAuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public String createUser(String email){
        User user = User.builder()
                .email(email)
                .password("test-password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return jwtService.generateToken(user);
    }
}
