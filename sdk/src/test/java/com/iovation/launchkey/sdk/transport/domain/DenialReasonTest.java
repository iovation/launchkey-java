package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DenialReasonTest {

    @Test
    public void getId() {
        assertEquals("expected", new DenialReason("expected", null, false).getId());
    }

    @Test
    public void getReason() {
        assertEquals("expected", new DenialReason(null, "expected", false).getReason());
    }

    @Test
    public void isFraud() {
        assertEquals(true, new DenialReason(null, null, true).isFraud());
    }

    @Test
    public void equalToTrueWhenValuesAreSame() {
        assertEquals(new DenialReason("a", "1", true), new DenialReason("a", "1", true));
    }

    @Test
    public void equalToFalseWhenIdIsDifferent() {
        assertNotEquals(new DenialReason("a", "1", true), new DenialReason("b", "1", true));
    }

    @Test
    public void equalToFalseWhenReasonIsDifferent() {
        assertNotEquals(new DenialReason("a", "1", true), new DenialReason("a", "2", true));

    }

    @Test
    public void equalToFalseWhenFraudIsDifferent() {
        assertNotEquals(new DenialReason("a", "1", true), new DenialReason("a", "1", false));

    }

    @Test
    public void hashCodeEqualWhenSame() {
        assertEquals(new DenialReason("a", "1", true).hashCode(), new DenialReason("a", "1", true).hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenIdIsDifferent() {
        assertNotEquals(new DenialReason("a", "1", true).hashCode(), new DenialReason("b", "1", true).hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenReasonIsDifferent() {
        assertNotEquals(new DenialReason("a", "1", true).hashCode(), new DenialReason("a", "2", true).hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenFraudIsDifferent() {
        assertNotEquals(new DenialReason("a", "1", true).hashCode(), new DenialReason("a", "1", false).hashCode());
    }

    @Test
    public void toStringContainsClassName() {
        assertTrue(new DenialReason(null, null, true)
                .toString().contains(DenialReason.class.getSimpleName()));
    }

    @Test
    public void objectMapperMapsAsExpected() throws Exception {
        String actual = new ObjectMapper().writeValueAsString(new DenialReason("a", "1", true));
        assertEquals("{\"id\":\"a\",\"reason\":\"1\",\"fraud\":true}", actual);
    }

}
