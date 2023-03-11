package ua.knu.csc.iss.ynortman.ring;

import org.junit.jupiter.api.Test;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

import static org.junit.jupiter.api.Assertions.*;

class RingUtilsTest {

    @Test
    void simplifyVectorTest() {
        int k = 8;
        RingInteger[] vector = new RingInteger[] {
                RingInteger.valueOf(0, k),
                RingInteger.valueOf(2, k),
                RingInteger.valueOf(4, k),
        };

        RingInteger[] expected = new RingInteger[] {
                RingInteger.valueOf(0, k),
                RingInteger.valueOf(1, k),
                RingInteger.valueOf(2, k),
        };
        assertArrayEquals(expected, RingUtils.simplifyVector(vector));
    }

    @Test
    void extendedEuclid() {
        int k = 24;
        RingInteger[] equation = new RingInteger[] {
                RingInteger.valueOf(8, k),
                RingInteger.valueOf(9, k),
        };
        RingInteger[] expected = new RingInteger[] {
                RingInteger.valueOf(1, k),
                RingInteger.valueOf(23, k),
                RingInteger.valueOf(1, k),
        };
        assertArrayEquals(expected, RingUtils.extendedEuclid(equation));
    }
}