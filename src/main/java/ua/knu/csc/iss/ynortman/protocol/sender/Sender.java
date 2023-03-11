package ua.knu.csc.iss.ynortman.protocol.sender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.protocol.receiver.Receiver;
import ua.knu.csc.iss.ynortman.ring.RingOps;
import ua.knu.csc.iss.ynortman.ring.RingUtils;
import ua.knu.csc.iss.ynortman.ring.model.NHLDE;
import ua.knu.csc.iss.ynortman.ring.model.NHLDESystem;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.ring.tss.NHLDESystemTSS;
import ua.knu.csc.iss.ynortman.utils.MatrixUtils;
import ua.knu.csc.iss.ynortman.utils.RabbitUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

@Slf4j
@Data
public class Sender {

    private final int a;
    private final int c;
    private final int k;

    private RingInteger[] substitution;

    @ToString
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class VectorModel implements Serializable {
        public final RingInteger[] d;
        public final RingInteger[] d1;
    }

    public void init() {
        try {
            //RabbitUtils.send(true, "InitQueue");
            RabbitUtils.receive("StepOneQueue", o -> {
                Receiver.MatricesModel message = (Receiver.MatricesModel) o;
//                this.c = DBKeyManagement.getKeysForSender(message.getA().length, id);
                //log.debug("Received matrix A: {}", Arrays.deepToString(message.getA()));
                //log.debug("Received matrix D: {}", Arrays.deepToString(message.getD()));
//                int messageBlocksNum = (msg.length + message.getA().length - 1) / message.getA().length - 1;
//                for(int i = 0; i < msg.length; i = i + message.getA().length) {
//                    BigInteger[] messageBlock = Arrays.copyOfRange(msg, i, i + message.getA().length);


//                    if(msg.length < i + message.getA().length) {
//                        //log.debug("Fill with empty chars");
//                        for (int j = (msg.length - (msg.length/message.getA().length)*message.getA().length);
//                             j < message.getA().length; ++j) {
//                            //log.debug("j={}, symbol = {}", j, Encoder.getInstance(p).getCharNumber(""));
//                            messageBlock[j] = Encoder.getInstance(p).getCharNumber("");
//                        }
//                    }
                    //log.debug("MESSAGE BLOCK IS: {}", Arrays.toString(messageBlock) );
                    //log.debug("MESSAGE IS: {}", Arrays.toString(msg) );
                    //log.debug("Start index: {}, end index: {}, message blocks num: {}",
                    //        i,  i + message.getA().length, messageBlocksNum);
                    VectorModel vectorModel = step2(message.A, message.B);
                    try {
                        RabbitUtils.send(vectorModel, "StepTwoQueue", false);
                    } catch (IOException | TimeoutException e) {
                        e.printStackTrace();
                    }
//                }
//                try {
//                    RabbitUtils.send(new BigInteger[0], "StepTwoQueue", false);
//                } catch (IOException | TimeoutException e) {
//                    e.printStackTrace();
//                }
            });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void prerequisites() {
        //Generate an initial substitution
        this.substitution = RingOps.substitution(a, c, k);
//        this.substitution = new RingInteger[]{
//                RingInteger.valueOf(1, k),
//                RingInteger.valueOf(6, k),
//                RingInteger.valueOf(8, k),
//                RingInteger.valueOf(10, k),
//                RingInteger.valueOf(2, k),
//                RingInteger.valueOf(4, k),
//                RingInteger.valueOf(3, k),
//                RingInteger.valueOf(5, k),
//                RingInteger.valueOf(7, k),
//                RingInteger.valueOf(9, k),
//                RingInteger.valueOf(11, k),
//                RingInteger.valueOf(13, k),
//                RingInteger.valueOf(15, k),
//                RingInteger.valueOf(17, k),
//                RingInteger.valueOf(19, k),
//                RingInteger.valueOf(21, k),
//                RingInteger.valueOf(12, k),
//                RingInteger.valueOf(14, k),
//                RingInteger.valueOf(16, k),
//                RingInteger.valueOf(18, k),
//                RingInteger.valueOf(20, k),
//                RingInteger.valueOf(24, k),
//                RingInteger.valueOf(22, k),
//                RingInteger.valueOf(23, k),
//                RingInteger.valueOf(0, k)
//        };
    }

    public VectorModel step2(RingInteger[][] acRingA, RingInteger[][] acRingD) {
        prerequisites();

        RingInteger[][] A = RingOps.matrixFromACRing(acRingA, this.substitution);
        RingInteger[][] D = RingOps.matrixFromACRing(acRingD, this.substitution);

        log.debug(MatrixUtils.printMatrix(acRingA, "acRingA"));
        log.debug(MatrixUtils.printMatrix(acRingD, "acRingD"));

        log.debug(MatrixUtils.printMatrix(A, "A"));
        log.debug(MatrixUtils.printMatrix(D, "D"));

        RingInteger[] msg = new RingInteger[] {
                RingInteger.valueOf(17, A[0][0].getM()),
                RingInteger.valueOf(0, A[0][0].getM())
        };
        RingInteger[][] basis = NHLDESystemTSS.basis(new NHLDESystem(A, msg));
        RingInteger[] x = NHLDE.randomSolutionVector(basis);
        log.debug(MatrixUtils.printVector(x, "x"));
//        RingInteger[] a = new RingInteger[] {
//                RingInteger.valueOf(0, A[0][0].getM()),
//                RingInteger.valueOf(1, A[0][0].getM()),
//                RingInteger.valueOf(0, A[0][0].getM()),
//                RingInteger.valueOf(1, A[0][0].getM())
//        };
        RingInteger[] a = RingUtils.randomVector(x.length, x[0].getM());

        RingInteger[] d = calculateSystemVector(A, a);
        RingInteger[] d1 = calculateSystemVector(D, RingUtils.addVectors(x, a));
        log.debug(MatrixUtils.printVector(d, "d"));
        log.debug(MatrixUtils.printVector(d1, "d1"));

        RingInteger[] acRingVectorD = RingOps.vectorToACRing(d, substitution);
        RingInteger[] acRingVectorD1 = RingOps.vectorToACRing(d1, substitution);

        log.debug(MatrixUtils.printVector(acRingVectorD, "acRingVectorD"));
        log.debug(MatrixUtils.printVector(acRingVectorD1, "acRingVectorD1"));

        return new VectorModel(acRingVectorD, acRingVectorD1);
    }

    public RingInteger[] calculateSystemVector(RingInteger[][] system, RingInteger[] vector) {
        RingInteger[] res = new RingInteger[system.length];
        for(int i = 0; i < system.length; ++i) {
            RingInteger s = RingInteger.zero(vector[0].getM());
            for(int j = 0; j < vector.length; ++j) {
                s = s.add(system[i][j].multiply(vector[j]));
            }
            if(system[i].length > vector.length) {
                s = s.add(system[i][system[i].length - 1]);
            }
            res[i] = s;
        }
        return res;
    }
}
