package com.control_activos.sks.control_activos.config;

import com.control_activos.sks.control_activos.Jwt.JwtUtil;
import com.control_activos.sks.control_activos.enums.AuthenticationExceptionEnum;
import com.control_activos.sks.control_activos.exception.AuthenticationException;
import com.control_activos.sks.control_activos.models.dto.LoginResponse;
import com.control_activos.sks.control_activos.models.entity.UserEntity;
import com.control_activos.sks.control_activos.repository.UserEntityRepository;
import com.control_activos.sks.control_activos.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final JwtUtil jwtUtil;
    private final PasswordConfig passwordConfig;
    private final UserEntityService userEntityService;
    private final UserEntityRepository userEntityRepository;


    /**
     * 1. find user by username.
     * 2. Compare newPassword with the one in the database.
     * 3. Check if user is active
     * 4. Generate JWT token if the credentials are valid.
     */

    public LoginResponse login(String username, String password) {

        // 1. find user by username.
        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(()-> new AuthenticationException(HttpStatus.UNAUTHORIZED, AuthenticationExceptionEnum.INVALID_CREDENTIALS.getMessage()));

        // 2. Compare newPassword with the one in the database.
        if (!passwordConfig.passwordEncoder().matches(password, userEntity.getPassword())) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, AuthenticationExceptionEnum.INVALID_CREDENTIALS.getMessage());
        }

        // 3. Check if user is active
        userEntityService.isUserEnabled(userEntity);

        // 4. Generate JWT token if the credentials are valid.
        return new LoginResponse(
                userEntity.getUsername(),
                userEntity.getFullName(),
                userEntity.getRole().getValue(),
                jwtUtil.generateToken(userEntity.getUsername(),userEntity.getFullName(),userEntity.getRole().name()));
    }
}
