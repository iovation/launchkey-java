package com.launchkey.sdk;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testBase64Decode() throws Exception {
        String expected = "abcde";
        String actual = new String(Util.base64Decode("YWJjZGU=".getBytes()));
        assertEquals(expected, actual);
    }

    @Test
    public void testBase64Encode() throws Exception {
        String expected = "YWJjZGU=";
        String actual = new String(Util.base64Encode("abcde".getBytes()));
        assertEquals(expected, actual);
    }
}