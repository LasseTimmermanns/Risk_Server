package de.lasse.risk_server.Utils;

import java.util.UUID;

public class TokenGenerator {

    public static String generateToken() {
        String token = UUID.randomUUID().toString();
        token = token.replaceAll("-", "");
        return token;
    }

}
