package com.app.year2021.month11;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESApplication {

    private static final String algorithm = "AES/GCM/PKCS5Padding";

    public static void main(String[] args) {
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            SecretKey secretKey = generator.generateKey();
            String gcmSecretKey = Base64.encodeBase64String(secretKey.getEncoded());
            System.out.println(" generator key is " + gcmSecretKey);
            String in = "java app";
            System.out.println(" source string is " + in);
            String encrypt = encrypt(in, gcmSecretKey);
            System.out.println(" encrypt result is " + encrypt);
            System.out.println(" decrypt result is " + decrypt(encrypt, gcmSecretKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String content, String keyStr) {
        try {
            SecretKey secretKey = new SecretKeySpec(Base64.decodeBase64(keyStr), "AES");
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getIV();
            byte[] encryptData = cipher.doFinal(content.getBytes());
            byte[] message = new byte[12 + content.getBytes().length + 16];
            System.arraycopy(iv, 0, message, 0, 12);
            System.arraycopy(encryptData, 0, message, 12, encryptData.length);
            return Base64.encodeBase64String(message);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String content, String keyStr){
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            SecretKey key = new SecretKeySpec(Base64.decodeBase64(keyStr), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] message = Base64.decodeBase64(content);
            GCMParameterSpec params = new GCMParameterSpec(128, message, 0 ,12);
            cipher.init(Cipher.DECRYPT_MODE, key, params);
            byte[] decryptData = cipher.doFinal(message, 12, message.length-12);
            return new String(decryptData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

}
