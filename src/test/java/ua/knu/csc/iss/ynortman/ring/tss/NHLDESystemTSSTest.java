package ua.knu.csc.iss.ynortman.ring.tss;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ua.knu.csc.iss.ynortman.ring.RingUtils;
import ua.knu.csc.iss.ynortman.ring.model.HLDESystem;
import ua.knu.csc.iss.ynortman.ring.model.NHLDE;
import ua.knu.csc.iss.ynortman.ring.model.NHLDESystem;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.MatrixUtils;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class NHLDESystemTSSTest {

    @Test
    void toExtendedHlde_Z24_SystemTest() {
        int k = 24;
        int[][] nhlde = new int[][] {
            {2, 3, 8, 6},
            {4, 6, 2, 3},
            {2, 3, 2, 2}
        };
        int[] coefs = new int[] {20, 22, 16};
        NHLDESystem system = new NHLDESystem(nhlde, coefs, k);
        HLDESystem expected = new HLDESystem(
                new int[][] {
                        {2, 3, 8, 6, 4},
                        {4, 6, 2, 3, 2},
                        {2, 3, 2, 2, 8}
                }, k
        );
        HLDESystem actual = NHLDESystemTSS.toExtendedHldeSystem(system);
        assertEquals(expected, actual);
    }

    @Test
    void toExtendedHlde_Z12_SystemTest() {
        int k = 12;
        int[][] nhlde = new int[][] {
                {2, 3, 8, 6, 4},
                {4, 3, 6, 6, 8}
        };
        int[] coefs = new int[] {8, 6};
        NHLDESystem system = new NHLDESystem(nhlde, coefs, k);
        HLDESystem expected = new HLDESystem(
                new int[][] {
                        {2, 3, 8, 6, 4, 4},
                        {4, 3, 6, 6, 8, 6}
                }, k
        );
        HLDESystem actual = NHLDESystemTSS.toExtendedHldeSystem(system);
        assertEquals(expected, actual);
    }

    @Test
    void constructEquationTest() {
        int k = 24;
        RingInteger[][] basis = new RingInteger[][] {
                {
                    RingInteger.valueOf(0, k),
                    RingInteger.valueOf(8, k),
                    RingInteger.valueOf(0, k),
                    RingInteger.valueOf(0, k),
                    RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(8, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(8, k),
                        RingInteger.valueOf(8, k),
                },
                {
                        RingInteger.valueOf(15, k),
                        RingInteger.valueOf(6, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(6, k),
                        RingInteger.valueOf(6, k),
                        RingInteger.valueOf(9, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(12, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                }
        };
        RingInteger[] expected = new RingInteger[] {
                RingInteger.valueOf(8, k),
                RingInteger.valueOf(9, k),
        };
        assertArrayEquals(expected, NHLDESystemTSS.constructEquation(basis));
    }

    @Test
    void separateSolutionTest() {
        int k = 24;
        RingInteger[] equation = new RingInteger[] {
                RingInteger.valueOf(8, k),
                RingInteger.valueOf(9, k),
        };
        RingInteger[][] basis = new RingInteger[][] {
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(8, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(8, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(8, k),
                        RingInteger.valueOf(8, k),
                },
                {
                        RingInteger.valueOf(15, k),
                        RingInteger.valueOf(6, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(6, k),
                        RingInteger.valueOf(6, k),
                        RingInteger.valueOf(9, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(12, k),
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(0, k),
                }
        };
        RingInteger[] expected = new RingInteger[] {
                RingInteger.valueOf(16, k),
                RingInteger.valueOf(0, k),
                RingInteger.valueOf(6, k),
                RingInteger.valueOf(22, k),
        };
        assertArrayEquals(expected, NHLDESystemTSS.separateSolution(equation, basis));
    }

    @Test
    void basisTest() {
        int k = 24;
        int[][] nhlde = new int[][] {
                {2, 3, 8, 6},
                {4, 6, 2, 3},
                {2, 3, 2, 2}
        };
        int[] coefs = new int[] {20, 22, 16};
        NHLDESystem system = new NHLDESystem(nhlde, coefs, k);
        RingInteger[][] actualBasis = NHLDESystemTSS.basis(system);
        log.debug(MatrixUtils.printMatrix(actualBasis, ""));

        for (NHLDE nhlde1 : system.getSystem()) {
            log.debug("NHLDE: {}", nhlde1);
            assertTrue(NHLDE.checkSolution(actualBasis, nhlde1));
        }
        log.debug(MatrixUtils.printMatrix(actualBasis, ""));
    }

    @Test
    void randomBasisTest() {
        int k = 125;
//        RingInteger[][] nhlde = MatrixUtils.nonSingularSimpleMatrix(4, 8, k);
        RingInteger[][] nhlde = new RingInteger[][] {
                {
                    RingInteger.valueOf(5, k),
                    RingInteger.valueOf(6, k),
                    RingInteger.valueOf(9, k),
                    RingInteger.valueOf(24, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(11, k),
                        RingInteger.valueOf(14, k),
                }
        };
//        log.debug(MatrixUtils.printMatrix(nhlde, ""));
        RingInteger[] coefs = RingUtils.randomVector(2, k);
        log.debug(MatrixUtils.printVector(coefs, "Random vector"));

//        BigInteger[][] genSolution = ua.knu.csc.iss.ynortman.tss.field.NHLDESystem
//                .generalSolution(nhldeBI, coefsBI, BigInteger.valueOf(k));
//        boolean res = ua.knu.csc.iss.ynortman.tss.field.NHLDESystem.verifySolution(nhldeBI, coefsBI, BigInteger.valueOf(k), genSolution);
//        assertTrue(res);


        NHLDESystem system = new NHLDESystem(nhlde, coefs);
        RingInteger[][] actualBasis = NHLDESystemTSS.basis(system);
        log.debug(MatrixUtils.printMatrix(actualBasis, ""));

        for (NHLDE nhlde1 : system.getSystem()) {
//            log.debug("NHLDE: {}", nhlde1);
            assertTrue(NHLDE.checkSolution(actualBasis, nhlde1));
        }
//        log.debug(MatrixUtils.printMatrix(actualBasis, ""));
    }

    @Test
    void randomBasis100Test() {
        for(int i = 0; i < 100; ++i) {
            randomBasisTest();
        }
    }
}