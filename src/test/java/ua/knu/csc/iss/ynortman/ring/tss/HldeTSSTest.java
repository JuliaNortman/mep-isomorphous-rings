package ua.knu.csc.iss.ynortman.ring.tss;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ua.knu.csc.iss.ynortman.ring.model.HLDE;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.MatrixUtils;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class HldeTSSTest {

    private final int[] hlde_ring12 = new int[] {2, 5, 7, 3, 6};
    private final int[] hlde_field3 = new int[] {2, 1, 0, 1, 2};

    @Test
    void validate() {

    }

    @Test
    void tss_ring12Test() {
        int k = 12;
        HLDE hlde = new HLDE(hlde_ring12, k);
        HldeTSS hldeTSS = new HldeTSS();
        RingInteger[][] expected = new RingInteger[][] {
                {
                    RingInteger.valueOf(5, k),
                    RingInteger.valueOf(0, k),
                    RingInteger.valueOf(2, k),
                    RingInteger.valueOf(0, k),
                    RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(3, k),
                        RingInteger.valueOf(5, k),
                        RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(6, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(5, k),
                }
        };
        RingInteger complement = new RingInteger(5, k);
        RingInteger[][] actual = hldeTSS.tss(hlde, complement, 2);
        assertArrayEquals(expected, actual);
    }


    @Test
    void tss_ring3Test() {
        int k = 3;
        HLDE hlde = new HLDE(hlde_field3, k);
        HldeTSS hldeTSS = new HldeTSS();
        RingInteger[][] expected = new RingInteger[][] {
                {
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(2, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(1, k),
                }
        };
        RingInteger complement = new RingInteger(1, k);
        RingInteger[][] actual = hldeTSS.tss(hlde, complement, 0);
        assertArrayEquals(expected, actual);
    }

    @Test
    void fieldBasis() {
        int k = 3;
        int[] fieldCoeffs = new int[] {2, 3, 8, 6, 4};
        HLDE hlde = new HLDE(fieldCoeffs, k);
        HldeTSS hldeTSS = new HldeTSS();
//        RingInteger[][] expected = new RingInteger[][] {
//                {
//                        RingInteger.valueOf(2, k),
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(1, k),
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(0, k),
//                },
//                {
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(1, k),
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(0, k),
//                },
//                {
//                        RingInteger.valueOf(1, k),
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(1, k),
//                },
//                {
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(0, k),
//                        RingInteger.valueOf(1, k),
//                        RingInteger.valueOf(0, k),
//                }
//        };

        RingInteger[][] actual = hldeTSS.fieldBasis(hlde);
        log.debug(MatrixUtils.printMatrix(actual, ""));
        assertTrue(hldeTSS.checkSolution(actual, hlde));
    }

    @Test
    void ghostRingBasis() {
        int k = 8;
        int[] fieldCoeffs = new int[] {2, 3, 8, 6, 4};
        HLDE hlde = new HLDE(fieldCoeffs, k);
        HldeTSS hldeTSS = new HldeTSS();


        RingInteger[][] actual = hldeTSS.ghostRingBasis(hlde);
        log.debug(MatrixUtils.printMatrix(actual, ""));
        assertTrue(hldeTSS.checkSolution(actual, hlde));
    }

    @Test
    void ringBasis_order384Test() {
        int k = 384;
        int[] fieldCoeffs = new int[] {2, 3, 8, 6, 4};
        HLDE hlde = new HLDE(fieldCoeffs, k);
        HldeTSS hldeTSS = new HldeTSS();
        RingInteger[][] actual = hldeTSS.ringBasis(hlde);
        log.debug(MatrixUtils.printMatrix(actual, ""));
        assertTrue(hldeTSS.checkSolution(actual, hlde));
    }

    @Test
    void ringBasis_order24Test() {
        int k = 24;
        int[] fieldCoeffs = new int[] {2, 3, 8, 6, 4};
        HLDE hlde = new HLDE(fieldCoeffs, k);
        HldeTSS hldeTSS = new HldeTSS();
        RingInteger[][] actual = hldeTSS.ringBasis(hlde);
        log.debug(MatrixUtils.printMatrix(actual, ""));
        assertTrue(hldeTSS.checkSolution(actual, hlde));
    }

    @Test
    void ringBasis_order8Test() {
        int k = 8;
        int[] fieldCoeffs = new int[] {2, 3, 8, 6, 4};
        HLDE hlde = new HLDE(fieldCoeffs, k);
        HldeTSS hldeTSS = new HldeTSS();
        RingInteger[][] actual = hldeTSS.ringBasis(hlde);
        log.debug(MatrixUtils.printMatrix(actual, ""));
        assertTrue(hldeTSS.checkSolution(actual, hlde));
    }

    @Test
    void ringBasis_order3Test() {
        int k = 3;
        int[] fieldCoeffs = new int[] {2, 3, 8, 6, 4};
        HLDE hlde = new HLDE(fieldCoeffs, k);
        HldeTSS hldeTSS = new HldeTSS();
        RingInteger[][] actual = hldeTSS.ringBasis(hlde);
        log.debug(MatrixUtils.printMatrix(actual, ""));
        assertTrue(hldeTSS.checkSolution(actual, hlde));
    }
}