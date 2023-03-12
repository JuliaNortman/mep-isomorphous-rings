package ua.knu.csc.iss.ynortman.protocol.sender;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.enoder.Encoder;
import ua.knu.csc.iss.ynortman.protocol.receiver.Receiver;
import ua.knu.csc.iss.ynortman.ring.RingOps;
import ua.knu.csc.iss.ynortman.ring.RingUtils;
import ua.knu.csc.iss.ynortman.ring.model.NHLDE;
import ua.knu.csc.iss.ynortman.ring.model.NHLDESystem;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.ring.tss.NHLDESystemTSS;
import ua.knu.csc.iss.ynortman.utils.FileUtils;
import ua.knu.csc.iss.ynortman.utils.MatrixUtils;
import ua.knu.csc.iss.ynortman.utils.RabbitUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

@Slf4j
@Data
public class Sender {

    private final int a;
    private final int c;
    private final int k;

    private RingInteger[] substitution;

    private RingInteger[] msg;
    private String textMsg;

    private String htmlContent = "<!DOCTYPE html>\n" +
            "<html><head>\n" +
            "<title>Bob</title>\n" +
            "<meta charset=\"utf-8\">\n" +
            "</head><body>";

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
            this.msg = Encoder.getInstance(k).textToNumbers(textMsg);
            log.debug("Msg: {}", Arrays.toString(msg));
            RabbitUtils.receive("StepOneQueue", o -> {
                Receiver.MatricesModel message = (Receiver.MatricesModel) o;
//                this.c = DBKeyManagement.getKeysForSender(message.getA().length, id);
                //log.debug("Received matrix A: {}", Arrays.deepToString(message.getA()));
                //log.debug("Received matrix D: {}", Arrays.deepToString(message.getD()));
                int messageBlocksNum = (msg.length + message.A.length - 1) / message.A.length - 1;
                for(int i = 0; i < msg.length; i = i + message.A.length) {
                    RingInteger[] messageBlock = Arrays.copyOfRange(msg, i, i + message.A.length);


                    if(msg.length < i + message.A.length) {
                        //log.debug("Fill with empty chars");
                        for (int j = (msg.length - (msg.length/message.A.length)*message.A.length);
                             j < message.A.length; ++j) {
                            //log.debug("j={}, symbol = {}", j, Encoder.getInstance(p).getCharNumber(""));
                            messageBlock[j] = Encoder.getInstance(k).getCharNumber("");
                        }
                    }
                    log.debug("MESSAGE BLOCK IS: {}", Arrays.toString(messageBlock) );
//                    log.debug("MESSAGE IS: {}", Arrays.toString(msg) );
//                    log.debug("Start index: {}, end index: {}, message blocks num: {}",
//                            i,  i + message.A.length, messageBlocksNum);
                    VectorModel vectorModel = step2(message.A, message.B, messageBlock);
                    try {
                        RabbitUtils.send(vectorModel, "StepTwoQueue", false);
                    } catch (IOException | TimeoutException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                try {
//                    Thread.sleep(20);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                try {
                    RabbitUtils.send(new VectorModel(new RingInteger[0], new RingInteger[0]), "StepTwoQueue", false);
                    log.debug("Empty is sent");
                    this.createReport();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void prerequisites() {
        //Generate an initial substitution
        this.substitution = RingOps.substitution(a, c, k);
    }

    public VectorModel step2(RingInteger[][] acRingA, RingInteger[][] acRingD, RingInteger[] msg) {
        prerequisites();

        RingInteger[][] A = RingOps.matrixFromACRing(acRingA, this.substitution);
        RingInteger[][] D = RingOps.matrixFromACRing(acRingD, this.substitution);

        log.debug(MatrixUtils.printMatrix(acRingA, "acRingA"));
        log.debug(MatrixUtils.printMatrix(acRingD, "acRingD"));

        log.debug(MatrixUtils.printMatrix(A, "A"));
        log.debug(MatrixUtils.printMatrix(D, "D"));

        this.htmlContent += FileUtils.printMatrix(acRingA, "Матриця A в АКК:");
        this.htmlContent += FileUtils.printMatrix(acRingD, "Матриця D в АКК:");
        this.htmlContent += FileUtils.printMatrix(A, "Матриця A:");
        this.htmlContent += FileUtils.printMatrix(D, "Матриця D:");

        this.htmlContent += FileUtils.printVector(msg, "Вектор v:");

//        msg = new RingInteger[] {
//                RingInteger.valueOf(17, A[0][0].getM()),
//                RingInteger.valueOf(0, A[0][0].getM())
//        };
        RingInteger[][] basis = NHLDESystemTSS.basis(new NHLDESystem(A, msg));
        RingInteger[] x = NHLDE.randomSolutionVector(basis);
        log.debug(MatrixUtils.printVector(x, "x"));
        RingInteger[] a = RingUtils.randomVector(x.length, x[0].getM());

        RingInteger[] d = calculateSystemVector(A, a);
        RingInteger[] d1 = calculateSystemVector(D, RingUtils.addVectors(x, a));
        log.debug(MatrixUtils.printVector(d, "d"));
        log.debug(MatrixUtils.printVector(d1, "d1"));


        RingInteger[] acRingVectorD = RingOps.vectorToACRing(d, substitution);
        RingInteger[] acRingVectorD1 = RingOps.vectorToACRing(d1, substitution);

        log.debug(MatrixUtils.printVector(acRingVectorD, "acRingVectorD"));
        log.debug(MatrixUtils.printVector(acRingVectorD1, "acRingVectorD1"));

        this.htmlContent += FileUtils.printVector(a, "Вектор a:");
        this.htmlContent += FileUtils.printVector(x, "Вектор x:");
        this.htmlContent += FileUtils.printVector(d, "Вектор d:");
        this.htmlContent += FileUtils.printVector(d1, "Вектор d1:");
        this.htmlContent += FileUtils.printVector(acRingVectorD, "Вектор d в АКК:");
        this.htmlContent += FileUtils.printVector(acRingVectorD1, "Вектор d1 в АКК:");

        this.htmlContent += FileUtils.end();


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

    @SneakyThrows
    public void createReport() {
        this.htmlContent += "</body>\n" +
                "</html>";

        File file = new File("Bob.html");
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
}
