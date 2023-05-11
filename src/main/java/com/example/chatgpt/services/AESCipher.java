package com.example.chatgpt.services;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESCipher {

    @Value("${api.secretKey}")
    private  String SECRET_KEY;

    String encodeMessage( String message) throws Exception  {
        System.out.println(SECRET_KEY);
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] iv = cipher.getIV();
        byte[] ciphertext = cipher.doFinal(message.getBytes());

        return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(ciphertext);
    }
}