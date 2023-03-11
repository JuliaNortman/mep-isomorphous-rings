package ua.knu.csc.iss.ynortman;

import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.protocol.receiver.Receiver;
import ua.knu.csc.iss.ynortman.protocol.sender.Sender;

import java.math.BigInteger;
import java.util.Scanner;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.debug("Symmetric system for message exchanging based on rings isomorphism");

        String mode = args[0];

        String p = args[1];
        int m = args.length > 2 ? Integer.parseInt(args[2]) : 2;
        int n = args.length > 3 ? Integer.parseInt(args[3]) : 10;

        if ("receiver".equals(mode)) {
            Receiver receiver = new Receiver(3, 7, 25);
            receiver.init();
            //System.out.println(receiver.getMessage());
        } else if ("sender".equals(mode)) {
//            Scanner sc = new Scanner(System.in);
//            System.out.print("Enter a string: ");
//            String msg = sc.nextLine();
            Sender sender = new Sender(3, 7, 25);
            sender.init();
        }
    }
}
