package com.control_activos.sks.control_activos.config;

import com.control_activos.sks.control_activos.Jwt.JwtUtil;
import com.control_activos.sks.control_activos.models.entity.UserEntity;
import com.control_activos.sks.control_activos.services.UserEntityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor()
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserEntityService userEntityService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Extract Authorization header
        String tokenHeader = request.getHeader("Authorization");

        // Check if header is present and starts with "Bearer "
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token from header
        String token = tokenHeader.substring(7);

        // Validate token
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract payload from token
        String subject = jwtUtil.getSubject(token);
        UserEntity userEntity = userEntityService.findByUserEntityByUsername(subject);
        userEntityService.isUserEnabled(userEntity);

        // Extract role from database
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name()));

        // Create an Authentication object and set it in the SecurityContext
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(subject, null, authorities);

        // Set details for the authentication object
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // save the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue with the filter chain
        filterChain.doFilter(request, response);

    }
}
