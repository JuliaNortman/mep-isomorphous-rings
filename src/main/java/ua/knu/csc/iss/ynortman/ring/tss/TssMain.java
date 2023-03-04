package ua.knu.csc.iss.ynortman.ring.tss;

import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.model.NHLDE;
import ua.knu.csc.iss.ynortman.ring.model.NHLDESystem;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

@Slf4j
public class TssMain {
    public static void main(String[] args) {
        NHLDE first = new NHLDE(
                new RingInteger[] {
                        new RingInteger(2, 3),
                        new RingInteger(1, 3),
                        new RingInteger(0, 3),
                        new RingInteger(1, 3),
                        new RingInteger(2, 3),
                },
                new RingInteger(2, 3)
        );
        NHLDE second = new NHLDE(
                new RingInteger[] {
                        new RingInteger(1, 3),
                        new RingInteger(2, 3),
                        new RingInteger(1, 3),
                        new RingInteger(0, 3),
                        new RingInteger(1, 3),
                },
                new RingInteger(1, 3)
        );
        NHLDE third = new NHLDE(
                new RingInteger[] {
                        new RingInteger(1, 3),
                        new RingInteger(1, 3),
                        new RingInteger(2, 3),
                        new RingInteger(2, 3),
                        new RingInteger(0, 3),
                },
                new RingInteger(2, 3)
        );
        NHLDESystem system = new NHLDESystem(new NHLDE[]{first, second, third});
        log.debug("{}", NHLDESystemTSS.transform(system));
    }
}
