package de.lasse.risk_server.Utils;

import java.nio.charset.Charset;
import java.util.Random;

public class TokenGenerator {

    public static String generateToken() {
        byte[] array = new byte[16]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

}
