package com.app.year2022.pack02;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RsaHelper {
    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";


    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCn/E/tB3eei8v6M0ESXvMEO/sH33CwaH3vFzjY9Ff53NHDZYmS2MC9nq5++jDzvkGiWGi5Enm1R6MRP2EJanMD4l6ZyT4zOm42SRo7PXfiyg8sGyEQWD+e/r2svhPIVrDx5ynosqV8tI8GbgW2x/XXt1X03FnKgEMTAM8gf2CzNQIDAQAB";
    private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJlZqvfCMnC5lGpgmcF6xGkvNlySp23Dcxdd7mKEMjMnaMoBgcZ8SBNH2Hnplc1pcKkmXWFRGch8C8VfaG1YgHcyLpX1bYZck8lX7PLT9nQSttj/Z1TKGCjS5mffRo30aZs+xqaVDSC3yWuM5gxgyNKp2zBqs152RyEG0VDYU47LAgMBAAECgYB9pCDWs45eGz6FVbZD9lD8GQmCGUoCPXOLfpM0tjjm/m/1qfxo4iSvX1r2r7HjxXLfs6YoeKkWQMPoJz5I8Iz3V0K92Giee6Gq+QmIHxemjNvjEvqVfwQIZs3htbUWRNluQChIJOXH9D439DpZpy6eC/2SfcQ2I5rDgbxLCznpsQJBAMZznS0sI0tXl2l2tlIjW18omSlexSjSOnKxq+xAFeJibU/TYQRaRhcr7TRCv71vcCwBkFPqZKictNlhWckn5JMCQQDF0eFEc3PqQjuY0pShN0zgV0/T1Ri4sH8A+uBFeToYheYZ1AI5zuheIk45WdqfGsn2IDtzkLj2xeSyb+2QeYfpAkBctG+NRZwrOvzRWJJtJnUtq9J4v/vMSQDiyVrt3zYZ7i7wZmdg2Cb79ho2GqRlgyRcelKI0Os61RGqps6BqLAfAkBQ9S0Q+4Lz2zlSuNO1CtG+el0kH3DnnOvNNd7078Dz6lCaP2bNy7zTUYzb8ccEGE2RGe3axN69t4157U63MtWJAkEAxhpgfNPIVt8/gb5bPJHwjLnUwy5eakqr5/HxBR1CB268MdeMtbDsDqBGjeERcq74rbGNbROOS6QSq/0OEtH2eg==";

    public static String encrypt(String str){
        byte[] decoded = Base64.decodeBase64(publicKey);
        String outStr = "";
        //RSA加密
        Cipher cipher;
        try {
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return outStr;
    }

    public static String decrypt(String str){
        String outStr = "";
        //64位解码加密后的字符串
        byte[] inputByte;
        try {
            inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outStr;
    }


    public static Map<String, String> createKeys(int keySize){
        //为RSA算法创建一个KeyPairGenerator对象（KeyPairGenerator，密钥对生成器，用于生成公钥和私钥对）
        KeyPairGenerator kpg;
        try{
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        }catch(NoSuchAlgorithmException e){
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded()); //返回一个publicKey经过二次加密后的字符串
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded()); //返回一个privateKey经过二次加密后的字符串

        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        System.out.println(publicKeyStr);
        System.out.println(privateKeyStr);
        System.out.println(new String(publicKey.getEncoded()));
        System.out.println(new String(privateKey.getEncoded()));
        return keyPairMap;
    }

}
