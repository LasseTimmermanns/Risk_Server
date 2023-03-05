package de.lasse.risk_server.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

public class FileUtils {

    public static String getFileAsString(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
