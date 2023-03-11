package ua.knu.csc.iss.ynortman.ring.tss;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ua.knu.csc.iss.ynortman.ring.model.HLDESystem;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.MatrixUtils;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class HLDESystemTSSTest {

    @Test
    void basisField_3Test() {
        int k = 3;
        HLDESystem system = new HLDESystem(new int[][]{
                {2, 1, 0, 1, 2},
                {1, 2, 1, 0, 1},
                {1, 1, 2, 2, 0}
        }, k);
        RingInteger[][] basis = HLDESystemTSS.basis(system);
        for (int i = 0; i < system.size(); ++i) {
            assertTrue(HldeTSS.checkSolution(basis, system.get(i)));
        }
    }

    @Test
    void basisGhostRing_8Test() {
        int k = 8;
        HLDESystem system = new HLDESystem(new int[][]{
                {2, 3, 8, 6, 4},
                {4, 6, 2, 3, 2},
                {2, 3, 2, 2, 8}
        }, k);
        RingInteger[][] basis = HLDESystemTSS.basis(system);
        for (int i = 0; i < system.size(); ++i) {
            assertTrue(HldeTSS.checkSolution(basis, system.get(i)));
        }
    }

    @Test
    void basisRing_3Test() {
        int k = 3;
        HLDESystem system = new HLDESystem(new int[][]{
                {2, 3, 8, 6, 4},
                {4, 6, 2, 3, 2},
                {2, 3, 2, 2, 8}
        }, k);
        RingInteger[][] basis = HLDESystemTSS.basis(system);
        log.debug(MatrixUtils.printMatrix(basis, ""));
        for (int i = 0; i < system.size(); ++i) {
            assertTrue(HldeTSS.checkSolution(basis, system.get(i)));
        }
    }

    @Test
    void ring_24BasisTest() {
        int k = 24;
        HLDESystem system = new HLDESystem(new int[][]{
                {2, 3, 8, 6, 4},
                {4, 6, 2, 3, 2},
                {2, 3, 2, 2, 8}
        }, k);
        RingInteger[][] basis = HLDESystemTSS.ringBasis(system);
        log.debug(MatrixUtils.printMatrix(basis, ""));
        for (int i = 0; i < system.size(); ++i) {
            assertTrue(HldeTSS.checkSolution(basis, system.get(i)));
        }
    }

    @Test
    void ring_384BasisTest() {
        int k = 384;
        HLDESystem system = new HLDESystem(new int[][]{
                {2, 3, 8, 6, 4},
                {4, 6, 2, 3, 2},
                {2, 3, 2, 2, 8}
        }, k);
        RingInteger[][] basis = HLDESystemTSS.ringBasis(system);
        log.debug(MatrixUtils.printMatrix(basis, ""));
        for (int i = 0; i < system.size(); ++i) {
            assertTrue(HldeTSS.checkSolution(basis, system.get(i)));
        }
    }
}