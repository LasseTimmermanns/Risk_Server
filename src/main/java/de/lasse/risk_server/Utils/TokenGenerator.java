package de.lasse.risk_server.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class TokenGenerator {

    public static String generateToken() {
        byte[] array = new byte[16];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.US_ASCII);
    }

}
