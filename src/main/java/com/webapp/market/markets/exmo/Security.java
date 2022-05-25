package com.webapp.market.markets.exmo;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Security {

    private static String PUBLIC_KEY = "";
    private static String SECRET_KEY = "";

    public static String getPublicKey() {
        return PUBLIC_KEY;
    }

    public static void setPublicKey(String publicKey) {
        if (publicKey.equals(""))
            publicKey = "K-";
        PUBLIC_KEY = publicKey;
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public static void setSecretKey(String secretKey) {
        if (secretKey.equals(""))
            secretKey = "S-";
        SECRET_KEY = secretKey;
    }

    private static final byte[] sign(String postData) {

        // Create a new secret key
        SecretKeySpec secretKeySpec;
        secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

        // Create a new mac
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("No such algorithm exception: " + nsae.toString());
            return null;
        }

        // Init mac with key.
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException ike) {
            System.err.println("Invalid key exception: " + ike.toString());
            return null;
        }

        // Encode the post data by the secret and encode the result as base64.
        try {
            return mac.doFinal(postData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            System.err.println("Unsupported encoding exception: " + uee.toString());
            return null;
        }
    }

    public static final String signWS(String postData) {
        return Base64.getEncoder().encodeToString(sign(postData));
    }

    public static final String signRest(String postData) {
        return Hex.encodeHexString(sign(postData));
    }
}

