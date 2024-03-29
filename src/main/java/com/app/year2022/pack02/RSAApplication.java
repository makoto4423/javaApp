package com.app.year2022.pack02;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Date;

public class RSAApplication {

    static byte[] publicKey;
    static byte[] privateKey;
    static String PUBLIC_KEY_PATH = "";
    static String PRIVATE_KEY_PATH = "";

    public static void main(String[] args){
        String s = RsaHelper.encrypt("{\"id\":\"page-020iPCBi\"}");
        System.out.println(s);
        System.out.println(RsaHelper.decrypt(s));
        System.out.println(RsaHelper.decrypt("JFQIXgnck1XoIdKvy3u4t5PBd2hS6drBbR3R87k2osRE/ZV0Rhw4i3/AtJRuB7pHsq4RE0w/NEdhtfbKCFNbT+W8o/xHF+tEY6PjKJqCeSks71vel4P5TqmbZdV/w8JLDHIsCC2LwfrssOegUFTYk0A3kPFhhjSrXNo9zRo6PmA="));
    }

    static String pubKey;
    static String priKey;

    public static void main2(String[] args){
        // geration();

        String input = "!!!hello world!!!";
        RSAPublicKey pubKey;
        RSAPrivateKey privKey;
        byte[] cipherText;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            pubKey = (RSAPublicKey) getPublicKey(PUBLIC_KEY_PATH);
            privKey = (RSAPrivateKey) getPrivateKey(PRIVATE_KEY_PATH);

            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            cipherText = cipher.doFinal(input.getBytes());
            //加密后的东西
            System.out.println("cipher: " + new String(cipherText));
            //开始解密
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainText = cipher.doFinal(cipherText);
            // System.out.println("publickey: " + Base64.getEncoder().encode(cipherText));
            System.out.println("plain : " + new String(plainText));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


    public static void geration(){
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_PATH);
            fos.write(publicKeyBytes);
            fos.close();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            fos = new FileOutputStream(PRIVATE_KEY_PATH);
            fos.write(privateKeyBytes);
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static PublicKey getPublicKey(String filename) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static PrivateKey getPrivateKey(String filename)throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec =new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


}
