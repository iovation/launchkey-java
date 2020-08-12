package com.iovation.launchkey.sdk.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectoryUserTotpTest {

    private DirectoryUserTotp model;

    @Before
    public void setUp() throws Exception {
        model = new DirectoryUserTotp("secret", "algorithm", 1234, 4321);
    }

    @Test
    public void testGetSecret() {
        assertEquals("secret", model.getSecret());
    }

    @Test
    public void testGetAlgorithm() {
        assertEquals("algorithm", model.getAlgorithm());
    }

    @Test
    public void testGetPeriod() {
        assertEquals(1234, model.getPeriod());
    }

    @Test
    public void testGetDigits() {
        assertEquals(4321, model.getDigits());
    }

    @Test
    public void equalObjectsAreEqual() {
        assertEquals(new DirectoryUserTotp("secret", "algorithm", 1234, 4321), model);
    }

    @Test
    public void differentSecretIsNotEqual() {
        assertNotEquals(new DirectoryUserTotp("secret1", "algorithm", 1234, 4321), model);
    }

    @Test
    public void differentAlgorithmIsNotEqual() {
        assertNotEquals(new DirectoryUserTotp("secret", "algorithm1", 1234, 4321), model);
    }

    @Test
    public void differentDigitsIsNotEqual() {
        assertNotEquals(new DirectoryUserTotp("secret", "algorithm", 12345, 4321), model);
    }

    @Test
    public void differentPeriodIsNotEqual() {
        assertNotEquals(new DirectoryUserTotp("secret", "algorithm", 1234, 43210), model);
    }

    @Test
    public void hashEqualIfObjectsAreEqual() {
        assertEquals(new DirectoryUserTotp("secret", "algorithm", 1234, 4321).hashCode(), model.hashCode());
    }

    @Test
    public void hashNotEqualWithDifferentSecret() {
        assertNotEquals(new DirectoryUserTotp("secret1", "algorithm", 1234, 4321).hashCode(), model.hashCode());
    }

    @Test
    public void hashNotEqualWithDifferentAlgorithm() {
        assertNotEquals(new DirectoryUserTotp("secret", "algorithm1", 1234, 4321).hashCode(), model.hashCode());
    }

    @Test
    public void hashNotEqualWithDifferentDigits() {
        assertNotEquals(new DirectoryUserTotp("secret", "algorithm", 12345, 4321).hashCode(), model.hashCode());
    }

    @Test
    public void hashNotEqualWithDifferentPeriod() {
        assertNotEquals(new DirectoryUserTotp("secret", "algorithm", 1234, 43210).hashCode(), model.hashCode());
    }

    @Test
    public void testToStringHasClassName() {
        assertTrue(model.toString().contains(model.getClass().getSimpleName()));
    }
}