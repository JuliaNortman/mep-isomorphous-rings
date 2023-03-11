package ua.knu.csc.iss.ynortman.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;
import ua.knu.csc.iss.ynortman.ring.RingUtils;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ComputationUtilsTest {

    @Test
    void factorizationTest() {
        List<ComputationUtils.FactorModel> expected = new LinkedList<>();
        expected.add(new ComputationUtils.FactorModel(2, 2));
        expected.add(new ComputationUtils.FactorModel(3, 1));
        assertEquals(expected, ComputationUtils.factorization(12));

        expected = new LinkedList<>();
        expected.add(new ComputationUtils.FactorModel(17, 1));
        assertEquals(expected, ComputationUtils.factorization(17));

        expected = new LinkedList<>();
        expected.add(new ComputationUtils.FactorModel(5, 2));
        assertEquals(expected, ComputationUtils.factorization(25));

        expected = new LinkedList<>();
        expected.add(new ComputationUtils.FactorModel(17, 1));
        expected.add(new ComputationUtils.FactorModel(31, 1));
        assertEquals(expected, ComputationUtils.factorization(527));
    }

    @Test
    void gcd() {
        assertEquals(7, ComputationUtils.gcd(0, 7));
    }

    @Test
    void extendedEuclid() {
        int[] expected = new int[] {1, -9, 47};
        assertArrayEquals(expected, ComputationUtils.extendedEuclid(120, 23));

        expected = new int[] {1, -7, 8};
        assertArrayEquals(expected, ComputationUtils.extendedEuclid(17, 15));

        expected = new int[] {1, 1, 0};
        assertArrayEquals(expected, ComputationUtils.extendedEuclid(1, 7));

        expected = new int[] {2, -12, 11};
        assertArrayEquals(expected, ComputationUtils.extendedEuclid(64, 70));
    }

    @Test
    void testExtendedEuclid() {
        int k = 10000;
        int[] args = new int[]{47, 64, 70};
        int[] actual = ComputationUtils.extendedEuclid(args);
        RingInteger[] ringIntegerArgs = new RingInteger[args.length];
        for (int i = 0; i < ringIntegerArgs.length; ++i) {
            ringIntegerArgs[i] = RingInteger.valueOf(args[i], k);
        }

        assertEquals(RingUtils.gcd(ringIntegerArgs).getNumber(), actual[0]);
        int check = 0;
        for(int i = 1; i < actual.length; ++i) {
            check += actual[i]*args[i-1];
        }
        assertEquals(actual[0], check);
    }

    @Test
    void testExtendedEuclid2() {
        int k = 100000;
        int[] args = new int[]{45, 1, 17, 86, 9, 3};
        int[] actual = ComputationUtils.extendedEuclid(args);
        RingInteger[] ringIntegerArgs = new RingInteger[args.length];
        for (int i = 0; i < ringIntegerArgs.length; ++i) {
            ringIntegerArgs[i] = RingInteger.valueOf(args[i], k);
        }

        assertEquals(RingUtils.gcd(ringIntegerArgs).getNumber(), actual[0]);
        int check = 0;
        for(int i = 1; i < actual.length; ++i) {
            check += actual[i]*args[i-1];
        }
        assertEquals(actual[0], check);
    }
}