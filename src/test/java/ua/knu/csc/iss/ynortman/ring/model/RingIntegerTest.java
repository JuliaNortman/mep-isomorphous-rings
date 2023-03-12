package ua.knu.csc.iss.ynortman.ring.model;

import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class RingIntegerTest {

    private final int k = 25;

    @Test
    void gcdTest() {
        assertEquals(
                RingInteger.valueOf(7, k),
                RingInteger.valueOf(14, k).gcd(RingInteger.valueOf(7, k)));

        assertEquals(
                RingInteger.valueOf(7, k),
                RingInteger.valueOf(7, k).gcd(RingInteger.valueOf(14, k)));

        assertEquals(
                RingInteger.valueOf(8, k),
                RingInteger.valueOf(0, k).gcd(RingInteger.valueOf(8, k)));

        assertEquals(
                RingInteger.valueOf(4, k),
                RingInteger.valueOf(12, k).gcd(RingInteger.valueOf(8, k)));
    }

    @Test
    void complementTest() {
        assertEquals(RingInteger.valueOf(1, 3), RingInteger.valueOf(2, 3).complement());
        assertEquals(RingInteger.valueOf(0, 8), RingInteger.valueOf(8, 8).complement());
    }

    @Test
    public void equalsHashCodeContracts() {
        EqualsVerifier.forClass(RingInteger.class)
                .withIgnoredFields("m")
                .verify();
    }
}