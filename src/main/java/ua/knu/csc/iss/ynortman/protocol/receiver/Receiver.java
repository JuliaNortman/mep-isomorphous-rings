package ua.knu.csc.iss.ynortman.protocol.receiver;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.enoder.Encoder;
import ua.knu.csc.iss.ynortman.protocol.sender.Sender;
import ua.knu.csc.iss.ynortman.ring.RingOps;
import ua.knu.csc.iss.ynortman.ring.RingUtils;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.FileUtils;
import ua.knu.csc.iss.ynortman.utils.MatrixUtils;
import ua.knu.csc.iss.ynortman.utils.RabbitUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Data
@Slf4j
public class Receiver {
    private RingInteger[][] A;
    private RingInteger[][] D;

    private final int b;
    private final int c;
    private final int k;

    private RingInteger[] substitution;
    private List<MatrixUtils.InvertibleMatrixModel> invMatrices;
    private List<RingInteger[]> shiftVectors;

    private String htmlContent = "<!DOCTYPE html>\n" +
            "<html><head>\n" +
            "<title>Alice</title>\n" +
            "<meta charset=\"utf-8\">\n" +
            "</head><body>";

    int r = 1; //TODO: remove fix constant

    public void prerequisites() {
        //Generate an initial substitution
        this.substitution = RingOps.substitution(b, c, k);
        this.htmlContent += FileUtils.printSubstitutionRow(this.substitution);
    }

    private ReceivedMessage receivedMessage = new ReceivedMessage();

    static class ReceivedMessage {
        private final StringBuilder msg = new StringBuilder();
        public void appendMessageBlock(String str) {
            msg.append(str);
        }
        public String getMessage() {
            return msg.toString();
        }
    }

    @ToString
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class MatricesModel implements Serializable {
        public final RingInteger[][] A;
        public final RingInteger[][] B;
    }

    public String init() {
        class dWrapper {
            RingInteger[] d;
        }

        try {
            prerequisites();
            MatricesModel model = step1(5, 7);
            RabbitUtils.send(model, "StepOneQueue", true);
            dWrapper wrapper = new dWrapper();
            wrapper.d = new RingInteger[1];
            while (wrapper.d.length > 0) {
                log.debug("d length: {}", wrapper.d.length);
                RabbitUtils.receive("StepTwoQueue", o -> {
                    Sender.VectorModel receivedVectors = (Sender.VectorModel) o;
                    if(receivedVectors.d.length > 0) {
                        //log.debug("Received vector: {}", Arrays.toString(d));
                        RingInteger[] res = step3(receivedVectors.d, receivedVectors.d1);
                        String messageBlock = Encoder.getInstance(k).numbersToText(res);
                        receivedMessage.appendMessageBlock(messageBlock);
                        System.out.println("RECEIVED MESSAGE IS: " + Arrays.toString(res));
                        System.out.println("RECEIVED MESSAGE IS: " + messageBlock);
                    }
                    wrapper.d = receivedVectors.d;
                    log.debug("d wrapper length: {}", wrapper.d.length);
                    log.debug("d length 1: {}", receivedVectors.d.length);
                    synchronized (wrapper) {
                        log.debug("Notify");
                        wrapper.notify();
                        log.debug("Notified");
                    }
                    if(wrapper.d.length == 0) {
                        synchronized (wrapper) {
                            log.debug("Notify 2");
                            wrapper.notify();
                            log.debug("Notified 2");
                        }
                    }
                });
                synchronized (wrapper) {
                    wrapper.wait();
                }
                log.debug("End wait");
            }
        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Received message: {}", receivedMessage.getMessage());
        return receivedMessage.getMessage();
    }

    // During the algorithm we will need to calculate inverted matrices
    // We will choose MatrixUtils.triangularInvertibleMatrix() method for this
    // The algo used in this method builds matrices in special form - their dimension will be
    // divisible by three. That is why parameter m is NOT a dimension but is ONE THIRD of dimension
    // of the resulted matrices
    // step1() method expects to have value of 3*m still LESS than q
    public MatricesModel step1(int m, int q) {
        /*
        * 1. Generates a system of expressions A(x)
        * {a11*x1, a12*x2, ..., a1q*xq}
        * {a21*x1, a22*x2, ..., a2q*xq}
        * ............................
        * {am1*x1, am2*x2, ..., amq*xq}
        * */

//        this.A = MatrixUtils.nonSingularSimpleMatrix(3*m, q, k);
        this.A = this.mockMatrixA();

        /*
        * 2. Select vectors a1, a2, ..., ar, a(r+1) - these will be called shift vectors
        * and square (m*m) matrices B1, B2, ..., Br - will be needed to build inverted for those matrices
        * */
        this.shiftVectors = new ArrayList<>(r+1);
        this.invMatrices = new ArrayList<>(r);
        for(int i = 0; i < r; ++i) {
            this.invMatrices.add(i, new MatrixUtils.InvertibleMatrixModel(
                    this.mockMatrixB(), this.mockInvMatrixB()));
//            this.invMatrices.add(i, MatrixUtils.triangularInvertibleMatrix(m, k));
            this.shiftVectors.add(i, RingUtils.randomVector(2, k));
            this.htmlContent += FileUtils.printVector(this.shiftVectors.get(i), "Вектор a" + i + ":");

//            this.shiftVectors.add(i, this.mockShift1());
//            this.shiftVectors.add(i, RingUtils.randomVector(3*m, k));
        }
        this.shiftVectors.add(r, RingUtils.randomVector(2, k));
        this.htmlContent += FileUtils.printVector(this.shiftVectors.get(r), "Вектор a" + r + ":");

//        this.shiftVectors.add(r, this.mockShift2());
//        this.shiftVectors.add(r, RingUtils.randomVector(3*m, k));

        /*
        * Transforms A(x) as follows:
        * D(x) = Br(B(r-1)(...B2(B1(A(x)+a1)+a2)+ ... +a(r-1))+ar)+a(r+1)
        * */
        generateD(r);

        // Transform values of A and D to associative-commutative ring
        RingInteger[][] acRingA = RingOps.matrixToACRing(this.A, substitution);
        RingInteger[][] acRingD = RingOps.matrixToACRing(this.D, substitution);

        log.debug(MatrixUtils.printMatrix(acRingA, "A"));
        log.debug(MatrixUtils.printMatrix(acRingD, "D"));

        this.htmlContent += FileUtils.printMatrix(A, "Матриця A:");
        this.htmlContent += FileUtils.printMatrix(D, "Матриця D:");
        this.htmlContent += FileUtils.printMatrix(acRingA, "Матриця A в АКК:");
        this.htmlContent += FileUtils.printMatrix(acRingD, "Матриця D в АКК:");

        return new MatricesModel(acRingA, acRingD);
    }

    public void generateD(int r) {
        D = new RingInteger[A.length][A[0].length+1]; //Ax + a1
        for (int i = 0; i < A.length; ++i) {
            System.arraycopy(A[i], 0, D[i], 0, A[i].length);
            D[i][D[i].length-1] = shiftVectors.get(0)[i];
        }
        for(int i = 0; i < r; ++i) {
            D = MatrixUtils.matrixAddVectorColumn(MatrixUtils.operator(invMatrices.get(i).matrix, D),
                    shiftVectors.get(i+1), D[i].length-1);
        }
    }

    public RingInteger[] step3(RingInteger[] acRingVectorD, RingInteger[] acRingVectorD1) {

        RingInteger[] d = RingOps.vectorFromACRing(acRingVectorD, this.substitution);
        RingInteger[] d1 = RingOps.vectorFromACRing(acRingVectorD1, this.substitution);

        this.htmlContent += FileUtils.printVector(acRingVectorD, "Вектор d в АКК:");
        this.htmlContent += FileUtils.printVector(acRingVectorD1, "Вектор d1 в АКК:");
        this.htmlContent += FileUtils.printVector(d, "Вектор d:");
        this.htmlContent += FileUtils.printVector(d1, "Вектор d1:");


        log.debug(MatrixUtils.printVector(d, "d"));
        log.debug(MatrixUtils.printVector(d1, "d1"));

        d1 = RingUtils.substractVectors(d1, shiftVectors.get(r));
        log.debug(MatrixUtils.printVector(shiftVectors.get(r), "a2"));
        log.debug(MatrixUtils.printVector(d1, "d1-a2"));
        RingInteger[][] d1Column = MatrixUtils.rowToColumn(d1);
        log.debug(MatrixUtils.printVector(d1, "d1-a2"));
        log.debug(MatrixUtils.printMatrix(d1Column, "d1-a2 (col)"));

        for(int i = r-1; i >= 0; --i) {
            d1Column = MatrixUtils.operator(invMatrices.get(i).inverted, d1Column);
            log.debug(MatrixUtils.printMatrix(d1Column, "B^(-1)[d1-a2] (col)"));
            d1 = MatrixUtils.columnToRow(d1Column);
            log.debug(MatrixUtils.printVector(d1, "B^(-1)[d1-a2]"));
            log.debug(MatrixUtils.printVector(shiftVectors.get(i), "a1"));
            d1 = RingUtils.substractVectors(d1, shiftVectors.get(i));
            log.debug(MatrixUtils.printVector(d1, "B^(-1)[d1-a2] - a1"));
            d1Column = MatrixUtils.rowToColumn(d1);
        }
        d1 = MatrixUtils.columnToRow(d1Column);
        RingInteger[] v = RingUtils.substractVectors(d1, d);
        log.debug(MatrixUtils.printVector(v, "v"));

        this.htmlContent += FileUtils.printVector(v, "Вектор v:");
        this.htmlContent += FileUtils.end();

        return v;
    }

    @SneakyThrows
    public void createReport() {
        this.htmlContent += "</body>\n" +
                "</html>";

        File file = new File("Alice.html");
        try {
            // Replace file if it already exists
            if (file.exists()) {
                file.delete();
            }

            // Create new file
            file.createNewFile();

            // Write content to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
            writer.write(this.htmlContent);
            writer.close();

            log.debug("File created successfully.");
        } catch (IOException e) {
            log.error("An error occurred while creating the file.");
            e.printStackTrace();
        }
    }

    private RingInteger[][] mockMatrixA() {
        return new RingInteger[][] {
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
    }

    private RingInteger[][] mockMatrixB() {
        return new RingInteger[][] {
                {
                        RingInteger.valueOf(2, k),
                        RingInteger.valueOf(1, k),
                },
                {
                        RingInteger.valueOf(24, k),
                        RingInteger.valueOf(24, k),
                }
        };
    }

    private RingInteger[][] mockInvMatrixB() {
        if(k == 25){
            return new RingInteger[][]{
                    {
                            RingInteger.valueOf(1, k),
                            RingInteger.valueOf(1, k),
                    },
                    {
                            RingInteger.valueOf(24, k),
                            RingInteger.valueOf(23, k),
                    }
            };
        }

        return new RingInteger[][] {
                {
                        RingInteger.valueOf(1, k),
                        RingInteger.valueOf(26, k),
                },
                {
                        RingInteger.valueOf(124, k),
                        RingInteger.valueOf(73, k),
                }
        };
    }

//    private RingInteger[] mockShift1() {
//        return new RingInteger[]{
//                        RingInteger.valueOf(1, k),
//                        RingInteger.valueOf(5, k),
//        };
//    }
//
//    private RingInteger[] mockShift2() {
//        return new RingInteger[]{
//                RingInteger.valueOf(1, k),
//                RingInteger.valueOf(1, k),
//        };
//    }
}
