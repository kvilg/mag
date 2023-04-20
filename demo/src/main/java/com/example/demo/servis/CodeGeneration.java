package com.example.demo.servis;

import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CodeGeneration {

    private static final String CHARACTERS = "0123456789";

    public String generate(int length){
        if (length < 1) {
            throw new IllegalArgumentException("Length must be at least 1");
        }
        StringBuilder code = new StringBuilder();
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(CHARACTERS.length());
                code.append(CHARACTERS.charAt(index));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secure random", e);
        }
        return code.toString();
    }

}
