package ua.knu.csc.iss.ynortman.protocol.receiver;

import org.junit.jupiter.api.Test;
import ua.knu.csc.iss.ynortman.ring.RingOps;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.MatrixUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiverTest {

    @Test
    void step1() {
        int k = 25;
        RingInteger[] substitution = {
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

        RingInteger[][] A = new RingInteger[][] {
                {
                    RingInteger.valueOf(5, k),
                    RingInteger.valueOf(6, k),
                    RingInteger.valueOf(9, k),
                    RingInteger.valueOf(21, k),
                },
                {
                        RingInteger.valueOf(0, k),
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(11, k),
                        RingInteger.valueOf(14, k),
                }
        };

        List<RingInteger[]> shiftVectors = new ArrayList<>(2);
        RingInteger[] a1 = new RingInteger[] {
                RingInteger.valueOf(1, k),
                RingInteger.valueOf(5, k)
        };

        RingInteger[] a2 = new RingInteger[] {
                RingInteger.valueOf(1, k),
                RingInteger.valueOf(1, k)
        };
        shiftVectors.add(a1);
        shiftVectors.add(a2);

        RingInteger[][] B = new RingInteger[][] {
                {
                        RingInteger.valueOf(2, k),
                        RingInteger.valueOf(1, k)
                },
                {
                        RingInteger.valueOf(24, k),
                        RingInteger.valueOf(24, k)
                }
        };
        List<MatrixUtils.InvertibleMatrixModel> invMatrices = new ArrayList<>(1);
        invMatrices.add(new MatrixUtils.InvertibleMatrixModel(B, B));

        Receiver receiver = new Receiver(2, 5, k);
        receiver.setA(A);
        receiver.setShiftVectors(shiftVectors);
        receiver.setSubstitution(substitution);
        receiver.setInvMatrices(invMatrices);

        receiver.generateD(1);

        RingInteger[][] D = receiver.getD();
        RingInteger[][] expectedD = new RingInteger[][] {
                {
                        RingInteger.valueOf(10, k),
                        RingInteger.valueOf(13, k),
                        RingInteger.valueOf(4, k),
                        RingInteger.valueOf(6, k),
                        RingInteger.valueOf(8, k),
                },
                {
                        RingInteger.valueOf(20, k),
                        RingInteger.valueOf(18, k),
                        RingInteger.valueOf(5, k),
                        RingInteger.valueOf(15, k),
                        RingInteger.valueOf(20, k),
                }
        };

        assertArrayEquals(expectedD, D);

        RingInteger[][] actualD_acRing = RingOps.matrixToACRing(D, substitution);
        RingInteger[][] expectedD_acRing = new RingInteger[][] {
                {
                        RingInteger.valueOf(9, k),
                        RingInteger.valueOf(15, k),
                        RingInteger.valueOf(10, k),
                        RingInteger.valueOf(4, k),
                        RingInteger.valueOf(5, k),
                },
                {
                        RingInteger.valueOf(18, k),
                        RingInteger.valueOf(14, k),
                        RingInteger.valueOf(2, k),
                        RingInteger.valueOf(19, k),
                        RingInteger.valueOf(18, k),
                }
        };

        assertArrayEquals(expectedD_acRing, actualD_acRing);
    }

    @Test
    void testStep1() {
        int k = 25;
        Receiver receiver = new Receiver(5, 7, k);
        RingInteger[] substitution = {
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
        receiver.setSubstitution(substitution);
        receiver.step1(5, 5);
    }
}