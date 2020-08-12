package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class DirectoryV3TotpPostResponseTest {

    private DirectoryV3TotpPostResponse transport;

    @Before
    public void setUp() throws Exception {
        transport = new DirectoryV3TotpPostResponse("secret", "algorithm", 1234, 4321);
    }

    @Test
    public void getSecret() {
        assertEquals("secret", transport.getSecret());
    }

    @Test
    public void getAlgorithm() {
        assertEquals("algorithm", transport.getAlgorithm());
    }

    @Test
    public void getPeriod() {
        assertEquals(1234, transport.getPeriod());
    }

    @Test
    public void getDigits() {
        assertEquals(4321, transport.getDigits());
    }

    @Test
    public void equalObjectsAreEqual() {
        assertEquals(new DirectoryV3TotpPostResponse("secret", "algorithm", 1234, 4321), transport);
    }

    @Test
    public void differentSecretIsNotEqual() {
        assertNotEquals(new DirectoryV3TotpPostResponse("secret1", "algorithm", 1234, 4321), transport);
    }

    @Test
    public void differentAlgorithmIsNotEqual() {
        assertNotEquals(new DirectoryV3TotpPostResponse("secret", "algorithm1", 1234, 4321), transport);
    }

    @Test
    public void differentDigitsIsNotEqual() {
        assertNotEquals(new DirectoryV3TotpPostResponse("secret", "algorithm", 12345, 4321), transport);
    }

    @Test
    public void differentPeriodIsNotEqual() {
        assertNotEquals(new DirectoryV3TotpPostResponse("secret", "algorithm", 1234, 43210), transport);
    }

    @Test
    public void hashEqualIfObjectsAreEqual() {
        assertEquals(new DirectoryV3TotpPostResponse("secret", "algorithm", 1234, 4321).hashCode(), transport.hashCode());
    }

    @Test
    public void hashNotEqualWithDifferentSecret() {
        assertNotEquals(new DirectoryV3TotpPostResponse("secret1", "algorithm", 1234, 4321).hashCode(), transport.hashCode());
    }

    @Test
    public void hashNotEqualWithDifferentAlgorithm() {
        assertNotEquals(new DirectoryV3TotpPostResponse("secret", "algorithm1", 1234, 4321).hashCode(), transport.hashCode());
    }

    @Test
    public void hashNotEqualWithDifferentDigits() {
        assertNotEquals(new DirectoryV3TotpPostResponse("secret", "algorithm", 12345, 4321).hashCode(), transport.hashCode());
    }

    @Test
    public void hashNotEqualWithDifferentPeriod() {
        assertNotEquals(new DirectoryV3TotpPostResponse("secret", "algorithm", 1234, 43210).hashCode(), transport.hashCode());
    }

    @Test
    public void testToStringHasClassName() {
        assertTrue(transport.toString().contains(transport.getClass().getSimpleName()));
    }

    @Test
    public void fromJSONSetsExpectedData() throws Exception {
        DirectoryV3TotpPostResponse actual = new ObjectMapper().readValue(
                "{" +
                        "        \"algorithm\": \"algorithm\"," +
                        "        \"digits\": 4321," +
                        "        \"period\": 1234," +
                        "        \"secret\": \"secret\"" +
                        "    }",
                DirectoryV3TotpPostResponse.class);
        assertEquals(transport, actual);
    }

    @Test
    public void fromJSONDoesNotErrorWithNewData() throws Exception {
        new ObjectMapper().readValue(
                "{" +
                        "        \"algorithm\": \"algorithm\"," +
                        "        \"digits\": 4321," +
                        "        \"period\": 1234," +
                        "        \"secret\": \"secret\"," +
                        "        \"other\": null" +
                        "    }",
                DirectoryV3TotpPostResponse.class);
    }
}