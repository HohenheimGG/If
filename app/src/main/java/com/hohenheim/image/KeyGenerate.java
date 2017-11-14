package com.hohenheim.image;

import android.support.v4.util.LruCache;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by com.hohenheim on 11/07/2017.
 */

public class KeyGenerate {

    private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    // 32 bytes from sha-256 -> 64 hex chars.
    private static final char[] SHA_256_CHARS = new char[64];

    private final LruCache<String, String> cacheHash = new LruCache<>(1000);

    public String getKey(String key) {
        String safeKey;
        synchronized (cacheHash) {
            safeKey = cacheHash.get(key);
        }
        if(safeKey == null) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(key.getBytes());
                safeKey = sha256BytesToHex(digest.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        synchronized (cacheHash) {
            cacheHash.put(key, safeKey);
        }
        return safeKey;
    }

    /**
     * Returns the hex string of the given byte array representing a SHA256 hash.
     */
    private String sha256BytesToHex(byte[] bytes) {
        synchronized (SHA_256_CHARS) {
            return bytesToHex(bytes, SHA_256_CHARS);
        }
    }

    // Taken from:
    // http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java/9655275#9655275
    private String bytesToHex(byte[] bytes, char[] hexChars) {
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHAR_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHAR_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}
