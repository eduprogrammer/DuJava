/*
 * Copyright 2021. Eduardo Programador
 * All rights reserved.
 * www.eduardoprogramador.com
 * consultoria@eduardoprogramador.com
 *
 * */

package com.eduardoprogramador.dujava.DuCrypto;

import java.security.MessageDigest;

public class DuCrypto {

    private DuCrypto() {

    }

    public static DuCrypto getInstance() {
        return new DuCrypto();
    }

    public String md5(byte[] input) {
        return hashFingerPrint(input, "MD5");
    }

    public String sha1(byte[] input){
        return hashFingerPrint(input, "SHA-1");
    }

    public String sha256(byte[] input) {
        return hashFingerPrint(input,"SHA-256");
    }

    public String sha512(byte[] input){
        return hashFingerPrint(input,"SHA-512");
    }

    public String hashFingerPrint(byte[] input, String algorithm) {
        String res = null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(input);
            byte[] digest = messageDigest.digest();

            for (int i = 0; i < digest.length; i++) {
                int b = digest[i] & 0xff;
                String hex = Integer.toHexString(b);
                if(i == 0) {
                    res = hex;
                } else {
                    res += hex;
                }
            }

            return res;

        } catch (Exception ex) {
            return null;
        }
    }


}
