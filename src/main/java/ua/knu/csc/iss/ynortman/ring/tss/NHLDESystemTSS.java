package ua.knu.csc.iss.ynortman.ring.tss;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import ua.knu.csc.iss.ynortman.ring.RingUtils;
import ua.knu.csc.iss.ynortman.ring.model.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class NHLDESystemTSS {

    /**
     * Transforms given NHLDE system to HLDE system by adding extra column to
     * original HLDE system
     * @param nhldeSystem NHLDE system
     * @return extended HLDE system
     */
    public static HLDESystem toExtendedHldeSystem(NHLDESystem nhldeSystem) {
        HLDE[] hldeSystem = new HLDE[nhldeSystem.size()];
        for(int i = 0; i < nhldeSystem.size(); ++i) {
            NHLDE nhld = nhldeSystem.get(i);
            int[] coefs = nhld.getCoefs();
            RingInteger constantTerm = nhld.getB();
            HLDE hlde = new HLDE(
                    ArrayUtils.addAll(coefs, constantTerm.complement().getNumber()),
                    nhld.getB().getM());
            hldeSystem[i] = hlde;
        }
        return new HLDESystem(hldeSystem);
    }

    /**
     *
     * @param basis basis of HLDE system
     * @return array where coordinates are last
     * non-zero coordinates of vectors from given basis
     */
    public static RingInteger[] constructEquation(RingInteger[][] basis) {
        List<RingInteger> equation = new LinkedList<>();
        for (RingInteger[] vector : basis) {
            if(!RingInteger.zero(vector[0].getM()).equals(vector[vector.length-1])) {
                equation.add(vector[vector.length-1]);
            }
        }
        if (equation.isEmpty()) {
            throw new ArithmeticException("System is inconsistent");
        }
        return equation.toArray(RingInteger[]::new);
    }

    /**
     * Constructs separate solution for NHLDE system
     * @param equation equation constructed from basis using constructEquation method
     * @param basis basis for corresponding HLDE system
     * @return separate solution
     */
    public static RingInteger[] separateSolution(RingInteger[] equation, RingInteger[][] basis) {
        RingInteger[] equationSolution = RingUtils.extendedEuclid(equation);
        RingInteger[] solution = new RingInteger[basis[0].length-1];
        RingInteger[][] basisWithNonZeroLast = basisWithNonZeroLast(basis);
        Arrays.fill(solution, RingInteger.zero(basis[0][0].getM()));
        for (int i = 1; i < equationSolution.length; ++i) {
            for (int j = 0; j < solution.length; ++j) {
                solution[j] = solution[j].add(equationSolution[i].multiply(basisWithNonZeroLast[i-1][j]));
            }
        }
        return solution;
    }

    public static RingInteger[][] basis(NHLDESystem nhldeSystem) {
        log.debug("nhldeSystem: {}", nhldeSystem);

        HLDESystem extendedHldeSystem = toExtendedHldeSystem(nhldeSystem);
        log.debug("extendedHldeSystem: {}", extendedHldeSystem);

        RingInteger[][] hldeSystemBasis = HLDESystemTSS.ringBasis(extendedHldeSystem);
//        log.debug(MatrixUtils.printMatrix(hldeSystemBasis, "hldeSystemBasis"));
        RingInteger[] equation = constructEquation(hldeSystemBasis);
        RingInteger[] separateSolution = separateSolution(equation, hldeSystemBasis);
        RingInteger[][] basisWithZeroLast = basisWithZeroLast(hldeSystemBasis);
//        log.debug(MatrixUtils.printMatrix(basisWithZeroLast, "basisWithZeroLast"));
        RingInteger[][] basis = new RingInteger[basisWithZeroLast.length+1][separateSolution.length];
        basis[0] = separateSolution;
        for (int i = 1; i < basis.length; ++i) {
            System.arraycopy(basisWithZeroLast[i-1], 0, basis[i], 0, basisWithZeroLast[i-1].length-1);
        }
        return basis;
    }

    public static RingInteger[][] basisWithNonZeroLast(RingInteger[][] basis) {
        List<RingInteger[]> basisWithNonZeroLast = new LinkedList<>();
        for (RingInteger[] vector : basis) {
            if(!RingInteger.zero(basis[0][0].getM()).equals(vector[vector.length-1])) {
                basisWithNonZeroLast.add(vector);
            }
        }
        return basisWithNonZeroLast.toArray(RingInteger[][]::new);
    }

    public static RingInteger[][] basisWithZeroLast(RingInteger[][] basis) {
        List<RingInteger[]> basisWithNonZeroLast = new LinkedList<>();
        for (RingInteger[] vector : basis) {
            if(RingInteger.zero(basis[0][0].getM()).equals(vector[vector.length-1])) {
                basisWithNonZeroLast.add(vector);
            }
        }
        return basisWithNonZeroLast.toArray(RingInteger[][]::new);
    }

//    public static RingInteger[] solution
}
