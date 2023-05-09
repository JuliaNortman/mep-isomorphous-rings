package ua.knu.csc.iss.ynortman.ring;

import org.junit.jupiter.api.Test;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RingOpsTest {
    private final int k = 25;

    private final RingInteger[] substitution = {
            RingInteger.valueOf(1, k),
            RingInteger.valueOf(6, k),
            RingInteger.valueOf(8, k),
            RingInteger.valueOf(10, k),
            RingInteger.valueOf(2, k),
            RingInteger.valueOf(4, k),
            RingInteger.valueOf(3, k),
            RingInteger.valueOf(5, k),
            RingInteger.valueOf(7, k),
            RingInteger.valueOf(9, k),
            RingInteger.valueOf(11, k),
            RingInteger.valueOf(13, k),
            RingInteger.valueOf(15, k),
            RingInteger.valueOf(17, k),
            RingInteger.valueOf(19, k),
            RingInteger.valueOf(21, k),
            RingInteger.valueOf(12, k),
            RingInteger.valueOf(14, k),
            RingInteger.valueOf(16, k),
            RingInteger.valueOf(18, k),
            RingInteger.valueOf(20, k),
            RingInteger.valueOf(24, k),
            RingInteger.valueOf(22, k),
            RingInteger.valueOf(23, k),
            RingInteger.valueOf(0, k)
    };

    @Test
    public void givenCoefs_whenCalculateGenG_thenReturnRingUnityRowTest() {
        int k = 6;
        RingInteger[] expectedRow = {
                RingInteger.valueOf(1, k),
                RingInteger.valueOf(3, k),
                RingInteger.valueOf(4, k),
                RingInteger.valueOf(2, k),
                RingInteger.valueOf(5, k),
                RingInteger.valueOf(0, k)
        };
        RingInteger[] actualRow = RingOps.genG(5, 4, 6);
        assertArrayEquals(expectedRow, actualRow);
    }

    @Test
    public void givenUnityRow_whenCalculateAdditionTable_thenReturnAdditionTable() {
        int k = 6;
        RingInteger[][] expectedTable = {
                {
                    RingInteger.valueOf(0, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(2, k),
                        RingInteger.valueOf(3, k),
                        RingInteger.valueOf(4, k),
                        RingInteger.valueOf(5, k),
                },
                { //{1,3,4,2,5,0}
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(3, k),
                        RingInteger.valueOf(4, k),
                        RingInteger.valueOf(2, k),
                        RingInteger.valueOf(5, k),
                        RingInteger.valueOf(0, k),
                },
                { //{2,4,0,5,1,3},
                        RingInteger.valueOf(2, k),
                        RingInteger.valueOf(4, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(5, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(3, k),
                },
                { //                {3,2,5,4,0,1},
                        RingInteger.valueOf(3, k),
                        RingInteger.valueOf(2, k),
                        RingInteger.valueOf(5, k),
                        RingInteger.valueOf(4, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(1, k),
                },
                { //{4,5,1,0,3,2},
                        RingInteger.valueOf(4, k),
                        RingInteger.valueOf(5, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(3, k),
                        RingInteger.valueOf(2, k),
                },
                {//{5,0,3,1,2,4}
                        RingInteger.valueOf(5, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(3, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(2, k),
                        RingInteger.valueOf(4, k),
                },
        };
        RingInteger[] unityRow = {
                RingInteger.valueOf(1, k),
                RingInteger.valueOf(3, k),
                RingInteger.valueOf(4, k),
                RingInteger.valueOf(2, k),
                RingInteger.valueOf(5, k),
                RingInteger.valueOf(0, k),
        };
        RingInteger[][] actualTable = RingOps.additionTable(6, unityRow);
        assertArrayEquals(expectedTable, actualTable);
    }

//    @Test
//    public void givenAddtionTable_whenCalculateMultTable_thenReturnMultTable() {
//        int[][] additionTable = {
//                {0,1,2,3,4,5},
//                {1,3,4,2,5,0},
//                {2,4,0,5,1,3},
//                {3,2,5,4,0,1},
//                {4,5,1,0,3,2},
//                {5,0,3,1,2,4}
//        };
//        int[][] expectedTable = {
//                {0,0,0,0,0,0},
//                {0,1,2,3,4,5},
//                {0,2,2,0,0,2},
//                {0,3,0,4,3,4},
//                {0,4,0,3,4,3},
//                {0,5,2,4,3,1}
//        };
//        int[][] actualTable = RingOps.multiplicationTable(6, additionTable);
//        assertArrayEquals(expectedTable, actualTable);
//    }

    @Test
    void isomorphism_toACRing_Test() {
        RingInteger expected = RingInteger.valueOf(1, k);
        RingInteger actual = RingOps.isomorphism(RingInteger.valueOf(1, k), substitution, true);
        assertEquals(expected, actual);

        expected = RingInteger.valueOf(21, k);
        actual = RingOps.isomorphism(RingInteger.valueOf(16, k), substitution, true);
        assertEquals(expected, actual);

        expected = RingInteger.zero(k);
        actual = RingOps.isomorphism(RingInteger.valueOf(0, k), substitution, true);
        assertEquals(expected, actual);

        expected = RingInteger.valueOf(12, k);
        actual = RingOps.isomorphism(RingInteger.valueOf(17, k), substitution, true);
        assertEquals(expected, actual);
    }

    @Test
    void isomorphism_fromACRing_Test() {
        RingInteger expected = RingInteger.valueOf(1, k);
        RingInteger actual = RingOps.isomorphism(RingInteger.valueOf(1, k), substitution, false);
        assertEquals(expected, actual);

        expected = RingInteger.valueOf(2, k);
        actual = RingOps.isomorphism(RingInteger.valueOf(6, k), substitution, false);
        assertEquals(expected, actual);

        expected = RingInteger.zero(k);
        actual = RingOps.isomorphism(RingInteger.valueOf(0, k), substitution, false);
        assertEquals(expected, actual);

        expected = RingInteger.valueOf(23, k);
        actual = RingOps.isomorphism(RingInteger.valueOf(22, k), substitution, false);
        assertEquals(expected, actual);
    }

    @Test
    void isDivisorByOneTest() {
        assertTrue(RingOps.isDivisorByOne(RingInteger.valueOf(4, k), substitution));
        assertFalse(RingOps.isDivisorByOne(RingInteger.valueOf(2, k), substitution));
    }
}
