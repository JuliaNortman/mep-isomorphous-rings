package ua.knu.csc.iss.ynortman.ring.tss;

import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.RingOps;
import ua.knu.csc.iss.ynortman.ring.RingUtils;
import ua.knu.csc.iss.ynortman.ring.model.HLDE;
import ua.knu.csc.iss.ynortman.ring.model.HLDESystem;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.ComputationUtils;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class HLDESystemTSS {

    public static RingInteger[][] ringBasis(final HLDESystem system) {
//        log.debug("System: {}", system);
        List<RingInteger[][]> basisVectorsSet = new LinkedList<>();
        List<ComputationUtils.FactorModel> factors = ComputationUtils.factorization(system.ringOrder());
        for (ComputationUtils.FactorModel f : factors) {
//            log.debug("system.getCoefs(): {}", Arrays.deepToString(system.getCoefs()));
            HLDESystem systemInFactorRingOrder = new HLDESystem(system.getCoefs(), (int)Math.pow(f.number, f.pow));
//            log.debug("systemInFactorRingOrder: {}", systemInFactorRingOrder);
            RingInteger[][] basis = basis(systemInFactorRingOrder);
//            log.debug(MatrixUtils.printMatrix(basis, "BASIS"));
            basisVectorsSet.add(basis);
        }
        return RingOps.combineRingBasis(basisVectorsSet, factors, system.ringOrder());
    }

    public static RingInteger[][] basis(HLDESystem system) {
        RingInteger[][] basis = HldeTSS.ringBasis(system.get(0));
//        log.debug(MatrixUtils.printMatrix(basis, "basis 1"));
        for(int i = 1; i < system.size(); ++i) {
            HLDE hlde = constructHlde(system.get(i), basis);
//            log.debug("Constructed hlde: {}", hlde);
            RingInteger[][] secondBasis = HldeTSS.ringBasis(hlde);
//            log.debug(MatrixUtils.printMatrix(secondBasis, "basis 2"));
            basis = calculateBasis(basis, secondBasis);
//            log.debug(MatrixUtils.printMatrix(basis, "basis final"));

        }
        return basis;
    }

    public static RingInteger[][] calculateBasis(final RingInteger[][] firstBasis, final RingInteger[][] secondBasis) {
        RingInteger[][] basis = new RingInteger[secondBasis.length][firstBasis[0].length];
        for(int i = 0; i < secondBasis.length; ++i) {
            RingInteger[] vector = secondBasis[i];
            RingInteger[] resultVector = null;
            for(int j = 0; j < vector.length; ++j) {
                if(!RingInteger.zero(vector[i].getM()).equals(vector[j])) {
                    if(resultVector == null) {
                        resultVector = RingUtils.multiplyVectorByConst(
                                firstBasis[j], vector[j]);
                    } else {
                        resultVector = RingUtils.addVectors(
                                resultVector,
                                RingUtils.multiplyVectorByConst(firstBasis[j], vector[j])
                        );
                    }
                }
            }
            basis[i] = resultVector;
        }
        return basis;
    }

    public static HLDE constructHlde(HLDE equation, RingInteger[][] basis) {
        RingInteger[] quotients = new RingInteger[basis.length];
        boolean inconsistent = true;
        for (int j = 0; j < basis.length; ++j) {
            RingInteger[] vector = basis[j];
            RingInteger q = RingInteger.zero(equation.ringOrder());
            for(int i = 0; i < vector.length; ++i) {
                RingInteger element = vector[i];
                if(!element.equals(RingInteger.zero(equation.ringOrder()))) {
                    q = q.add(equation.get(i).multiply(element));
                }
            }
            quotients[j] = q;
            if(!quotients[j].equals(RingInteger.zero(equation.ringOrder()))) {
                inconsistent = false;
            }
        }
        if (inconsistent) {
            throw new RuntimeException("The system is inconsistent!");
        }
        quotients = RingUtils.simplifyVector(quotients);
        return new HLDE(quotients);
    }

}
