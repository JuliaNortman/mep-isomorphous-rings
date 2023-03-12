package ua.knu.csc.iss.ynortman;

import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.protocol.receiver.Receiver;
import ua.knu.csc.iss.ynortman.protocol.sender.Sender;

import java.util.Scanner;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.debug("Symmetric system for message exchanging based on rings isomorphism");

        String mode = args[0];
        int k = 125;

        if ("receiver".equals(mode)) {
            Receiver receiver = new Receiver(7, 7, k);
            String msg = receiver.init();
            log.info("Main receiver: {}", msg);
        } else if ("sender".equals(mode)) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter a string: ");
            String msg = sc.nextLine();
            Sender sender = new Sender(7, 7, k);
            sender.setTextMsg(msg);
            sender.init();
        }
    }
}
