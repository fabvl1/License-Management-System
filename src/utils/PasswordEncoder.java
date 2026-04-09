package utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordEncoder {

    // Codifica (hashea) la contraseña
    public static String encode(String plainPassword) {
        // El parámetro 12 es el "work factor" (más alto = más seguro pero más lento)
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    // Verifica si la contraseña ingresada corresponde al hash almacenado
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new IllegalArgumentException("Hash inválido para BCrypt");
        }
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}