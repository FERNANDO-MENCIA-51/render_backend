package pe.edu.vallegrande.pasteleria.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        String plain = "luis12";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String hash = encoder.encode(plain);
        System.out.println("Hash generado para '" + plain + "': " + hash);
        // Prueba de coincidencia
        boolean match = encoder.matches(plain, hash);
        System.out.println("¿Coincide el hash con el password? " + match);
    }
}
