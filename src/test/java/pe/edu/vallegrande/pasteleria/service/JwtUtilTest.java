package pe.edu.vallegrande.pasteleria.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testGenerateAndValidateToken() {
        String username = "usuario1";
        String rol = "ADMIN";
        String token = jwtUtil.generateToken(username, rol);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token, username));
        assertEquals(username, jwtUtil.extractUsername(token));
    }
}
