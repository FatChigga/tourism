package com.iptv.core.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class EncryptUtil {
    private static final String PASSWORD_CRYPT_KEY = "kEHrDooxWHCWtfeSxvDvgqZq";
    private static final String ALGORITHM = "DES";


    public static void main(String[] args)
            throws Exception {

        String hashAlgorithmName = "SHA-256";
        String credentials = "111111";
        ByteSource credentialsSalt = ByteSource.Util.bytes("admin");
        Object obj = new SimpleHash(hashAlgorithmName, credentials, credentialsSalt);

        System.out.println(obj.toString());
    }


    public static final String decrypt(String data)
            throws Exception {

        return new String(decrypt(hex2byte(data.getBytes()), "kEHrDooxWHCWtfeSxvDvgqZq".getBytes()));

    }

    public static final String encrypt(String data)
            throws Exception {

        return byte2hex(encrypt(data.getBytes(), "kEHrDooxWHCWtfeSxvDvgqZq".getBytes()));

    }

    private static byte[] encrypt(byte[] data, byte[] key)
            throws Exception {

        SecureRandom sr = new SecureRandom();


        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        SecretKey securekey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance("DES");

        cipher.init(1, securekey, sr);

        return cipher.doFinal(data);

    }

    private static byte[] decrypt(byte[] data, byte[] key)
            throws Exception {

        SecureRandom sr = new SecureRandom();


        DESKeySpec dks = new DESKeySpec(key);


        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        SecretKey securekey = keyFactory.generateSecret(dks);


        Cipher cipher = Cipher.getInstance("DES");

        cipher.init(2, securekey, sr);

        return cipher.doFinal(data);

    }

    public static byte[] hex2byte(byte[] b) {

        if (b.length % 2 != 0)
            throw new IllegalArgumentException("长度不是偶数");

        byte[] b2 = new byte[b.length / 2];

        for (int n = 0; n < b.length; n += 2) {

            String item = new String(b, n, 2);

            b2[(n / 2)] = ((byte) Integer.parseInt(item, 16));

        }

        return b2;

    }


    public static String byte2hex(byte[] b) {

        String hs = "";

        String stmp = "";

        for (int n = 0; n < b.length; n++) {

            stmp = Integer.toHexString(b[n] & 0xFF);

            if (stmp.length() == 1) {

                hs = hs + "0" + stmp;

            } else
                hs = hs + stmp;

        }

        return hs.toUpperCase();

    }

    public static String encryptBySHA256(String pwd,String account,Integer i) throws Exception{
        String hashAlgorithmName = "SHA-256";
        String credentials = pwd;
        ByteSource credentialsSalt = ByteSource.Util.bytes(account);
        Object obj = new SimpleHash(hashAlgorithmName, credentials, credentialsSalt,i);
        return obj.toString();
    }

    public static String encryptBySHA256(String pwd,String account) throws Exception{
        String hashAlgorithmName = "SHA-256";
        String credentials = pwd;
        ByteSource credentialsSalt = ByteSource.Util.bytes(account);
        Object obj = new SimpleHash(hashAlgorithmName, credentials, credentialsSalt);
        return obj.toString();
    }
}