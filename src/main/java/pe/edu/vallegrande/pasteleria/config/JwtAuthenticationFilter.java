package pe.edu.vallegrande.pasteleria.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.edu.vallegrande.pasteleria.service.JwtUtil;
import pe.edu.vallegrande.pasteleria.repository.UsuarioLoginRepository;
import pe.edu.vallegrande.pasteleria.model.UsuarioLogin;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UsuarioLoginRepository usuarioLoginRepository;

    private final ConcurrentHashMap<String, CachedUser> userCache = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRATION = TimeUnit.MINUTES.toMillis(5);

    private final ConcurrentHashMap<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 60;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UsuarioLoginRepository usuarioLoginRepository) {
        this.jwtUtil = jwtUtil;
        this.usuarioLoginRepository = usuarioLoginRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/v1/api/auth") ||
                path.startsWith("/actuator/health") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIP = getClientIP(request);
        if (!isRateLimitAllowed(clientIP)) {
            logger.warn("Rate limit excedido para IP: {}", clientIP);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Demasiadas peticiones\",\"message\":\"Rate limit excedido\"}");
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            logger.warn("Token inválido desde IP: {} - Error: {}", clientIP, e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token inválido\",\"details\":\"Token malformado o expirado\"}");
            return;
        }

        CachedUser cachedUser = userCache.get(username);
        UsuarioLogin user = null;

        if (cachedUser != null && (System.currentTimeMillis() - cachedUser.timestamp) < CACHE_EXPIRATION) {
            user = cachedUser.user;
        } else {
            Optional<UsuarioLogin> userOpt = usuarioLoginRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                user = userOpt.get();
                userCache.put(username, new CachedUser(user, System.currentTimeMillis()));
            }
        }

        if (user != null && jwtUtil.validateToken(jwt, username)) {
            if (!"A".equals(user.getEstado())) {
                logger.warn("Usuario inactivo intentó acceder: {} desde IP: {}", username, clientIP);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Usuario inactivo\",\"username\":\"" + username + "\"}");
                return;
            }

            String rol = user.getRol();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase());

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                            .password(user.getPasswordHash())
                            .authorities(Collections.singleton(authority))
                            .build(),
                    null,
                    Collections.singleton(authority));

            authToken.setDetails(user);
            SecurityContextHolder.getContext().setAuthentication(authToken);

            logger.debug("Usuario autenticado: {} con rol: {} desde IP: {}", username, rol, clientIP);
        } else {
            logger.warn("Token inválido o usuario no encontrado: {} desde IP: {}", username, clientIP);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Usuario no encontrado o token inválido\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }

    private boolean isRateLimitAllowed(String clientIP) {
        long now = System.currentTimeMillis();
        RateLimitInfo info = rateLimitMap.computeIfAbsent(clientIP, k -> new RateLimitInfo());

        if (now - info.windowStart > TimeUnit.MINUTES.toMillis(1)) {
            info.requestCount = 0;
            info.windowStart = now;
        }

        info.requestCount++;
        return info.requestCount <= MAX_REQUESTS_PER_MINUTE;
    }

    private static class CachedUser {
        final UsuarioLogin user;
        final long timestamp;

        CachedUser(UsuarioLogin user, long timestamp) {
            this.user = user;
            this.timestamp = timestamp;
        }
    }

    private static class RateLimitInfo {
        int requestCount = 0;
        long windowStart = System.currentTimeMillis();
    }
}