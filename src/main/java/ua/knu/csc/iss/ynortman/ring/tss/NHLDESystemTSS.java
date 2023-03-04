package ua.knu.csc.iss.ynortman.ring.tss;

import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.model.NHLDESystem;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

@Slf4j
public class NHLDESystemTSS {
    public static NHLDESystem transform(NHLDESystem system) {
        log.debug("System: {}", system);

        int minElementIndex = -1;
        int nonZeroEqCount = 0; // number of equations in system that are non homogeneous

        // find non homogeneous diophantine equation index in given system
        // and count non homogeneous equations
        for (int i = 0; i < system.size(); ++i) {
//            if(!system.getEquation(i).getB().equals(RingInteger.ZERO)) {
//                if(minElementIndex == -1) {
//                    minElementIndex = i;
//                }
//                nonZeroEqCount++;
//            }
        }

        // If nonZeroEqCount == 0 then system is homogeneous
        // If nonZeroEqCount == 1 then system has only one non homogeneous equation
        if(nonZeroEqCount <= 1) {
            return system;
        }

        while (nonZeroEqCount > 1) {
            // find equation which b is the least
            for (int i = 0; i < system.size(); ++i) {
                if (system.getEquation(i).getB().compareTo(system.getEquation(minElementIndex).getB()) < 0) {
                    minElementIndex = i;
                }
            }

            for (int i = 0; i < system.size(); ++i) {
                int coef = system.getEquation(i).getB().getNumber() / system.getEquation(minElementIndex).getB().getNumber();

                if (i != minElementIndex) {

                    system.setEquation(i,
                            system.getEquation(i)
                                    .substractEquation(
                                            system.getEquation(minElementIndex)
                                                    .multiplyConstant(new RingInteger(coef, system.getM()))));


//                    if (system.getEquation(i).getB().equals(RingInteger.ZERO)) {
//                        nonZeroEqCount--;
//                    }
                }
            }
        }
        return system;
    }
}
