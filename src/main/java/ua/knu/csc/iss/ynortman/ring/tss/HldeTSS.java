package ua.knu.csc.iss.ynortman.ring.tss;

import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.RingOps;
import ua.knu.csc.iss.ynortman.ring.RingUtils;
import ua.knu.csc.iss.ynortman.ring.model.HLDE;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.ComputationUtils;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class HldeTSS {
    public static RingInteger[][] ringBasis(HLDE hlde) {
        List<RingInteger[][]> basisVectorsSet = new LinkedList<>();
        List<ComputationUtils.FactorModel> factors = ComputationUtils.factorization(hlde.ringOrder());
        for (ComputationUtils.FactorModel f : factors) {
            if(f.pow == 1) {
                //field
                HLDE fieldHlde = new HLDE(hlde.getCoefs(), f.number);
                RingInteger[][] fieldBasis = fieldBasis(fieldHlde);
                basisVectorsSet.add(fieldBasis);
            } else {
                //ghost ring
                HLDE ringHlde = new HLDE(hlde.getCoefs(), (int) Math.pow(f.number, f.pow));
                RingInteger[][] ringBasis = ghostRingBasis(ringHlde);
                basisVectorsSet.add(ringBasis);
            }
        }
        return RingOps.combineRingBasis(basisVectorsSet, factors, hlde.ringOrder());
    }

    /*
     * Computes basis of HLDE in field - so that order is a prime number
     */
    public static RingInteger[][] fieldBasis(HLDE hlde) {
        if(!ComputationUtils.isPrime(hlde.ringOrder())) {
            throw new IllegalArgumentException("Field order must be prime!");
        }
        int firstNonZeroIndex = firstNonZeroIndex(hlde);
        RingInteger complement = hlde.get(firstNonZeroIndex).complement();
        RingInteger[][] basis = tss(hlde, complement, firstNonZeroIndex);
        return basis;
    }

    /*
     * Computes basis of HLDE in ghost ring (примарне кільце) - so that order is a prime number^k
     */
    public static RingInteger[][] ghostRingBasis(HLDE hlde) {
        List<ComputationUtils.FactorModel> factors = ComputationUtils.factorization(hlde.ringOrder());
        if(factors.size() > 1 || factors.get(0).pow <= 1) {
            throw new IllegalArgumentException("Not a ghost ring");
        }
        int firstNonZeroIndex = firstCoprimeIndex(hlde);
        RingInteger complement = hlde.get(firstNonZeroIndex).complement();
        RingInteger[][] basis = tss(hlde, complement, firstNonZeroIndex);
        return basis;
    }

    public static RingInteger[][] tss(HLDE hlde, RingInteger complement, int firstNonZeroIndex) {
        RingInteger[][] basis = new RingInteger[hlde.size()-1][hlde.size()];
        for(int i = 0; i < hlde.size(); ++i) {
            if(i < firstNonZeroIndex) {
                if (RingInteger.zero(hlde.ringOrder()).equals(hlde.get(i))) {
                    basis[i] = canonicalVector(i, hlde.size(), hlde.ringOrder());
                } else {
                    basis[i] = basisVector(hlde.get(i), i, complement, firstNonZeroIndex, hlde.size());
                }
            } else if(i > firstNonZeroIndex) {
                if (RingInteger.zero(hlde.ringOrder()).equals(hlde.get(i))) {
                    basis[i-1] = canonicalVector(i, hlde.size(), hlde.ringOrder());
                } else {
                    basis[i-1] = basisVector(hlde.get(i), i, complement, firstNonZeroIndex, hlde.size());
                }
            }
        }
        return basis;
    }

    /*
    * Finds the first coefficient position in HLDE that is coprime with ring order
    * If there is no such a coefficient then throws an exception
    * */
    public static int firstCoprimeIndex(HLDE hlde) {
        for (int i = 0; i < hlde.size(); ++i) {
            RingInteger coef = hlde.get(i);
            if(ComputationUtils.gcd(coef.getNumber(), coef.getM()) == 1) {
                return i;
            }
        }
        throw new ArithmeticException("HLDE coefficients have to include at least one coefficient that is coprime with ring order");
    }

    /*
     * Finds the first coefficient position in HLDE that is coprime with ring order
     * If there is no such a coefficient then throws an exception
     * */
    public static int firstNonZeroIndex(HLDE hlde) {
        for (int i = 0; i < hlde.size(); ++i) {
            RingInteger coef = hlde.get(i);
            if(!RingInteger.zero(hlde.ringOrder()).equals(coef)) {
                return i;
            }
        }
        throw new ArithmeticException("HLDE coefficients have to include at least one non-zero coefficient");
    }

    protected static RingInteger[] basisVector(RingInteger a, int j, RingInteger complement, int k, int n) {
        RingInteger[] vector = RingUtils.zeroArray(n, a.getM());
        RingInteger gcd = a.gcd(complement);
        if(RingInteger.one(a.getM()).equals(gcd)) {
            vector[k] = a;
            vector[j] = complement;
        } else {
            vector[k] = a.divide(gcd);
            vector[j] = complement.divide(gcd);
        }
        return vector;
    }

    /**
     *
     * @param j position (index) of unity element
     * @param size length of resulted canonical vector
     * @param ringOrder ring order of elements of canonical vector
     * @return canonical vector
     */
    protected static RingInteger[] canonicalVector(int j, int size, int ringOrder) {
        RingInteger[] vector = RingUtils.zeroArray(size, ringOrder);
        vector[j] = RingInteger.one(ringOrder);
        return vector;
    }

    public static boolean checkSolution(final RingInteger[][] basis, HLDE hlde) {
        RingInteger[][] multBasis = new RingInteger[basis.length][basis[0].length];
        for(int i = 0; i < basis.length; ++i) {
            RingInteger basisCoef = RingInteger.random(basis[0][0].getM());
            multBasis[i] = RingUtils.multiplyVectorByConst(basis[i], basisCoef);
        }
        RingInteger[] sumVector = RingUtils.zeroArray(hlde.size(), hlde.ringOrder());
        for (RingInteger[] ringIntegers : multBasis) {
            sumVector = RingUtils.addVectors(sumVector, ringIntegers);
        }
        RingInteger res = RingInteger.zero(hlde.ringOrder());
        for (int i = 0; i < hlde.size(); ++i) {
            res = res.add(hlde.get(i).multiply(sumVector[i]));
        }
        return res.equals(RingInteger.zero(hlde.ringOrder()));
    }
}
