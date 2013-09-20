package com.launchkey.sdk;

import org.apache.commons.codec.binary.Base64;

public class Util {
    private static Base64 base64;
    static {
        base64 = new Base64(0);
    }

    public static byte[] base64Decode(byte[] pArray) {
        return base64.decode(pArray);
    }

    public static byte[] base64Encode(byte[] pArray) {
        return base64.encode(pArray);
    }

}
