package ua.knu.csc.iss.ynortman.ring;

import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.ArrayUtils;
import ua.knu.csc.iss.ynortman.utils.ComputationUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class RingOps {

    /*
    * Given order k and coefficients a,c of expression f(i)=a*i+c
    * defines substitution between ring Zk and associative-commutative ring Gk
    * */
    public static RingInteger[] substitution(int a, int c, int k) {
        if(!BigInteger.valueOf(a).gcd(BigInteger.valueOf(k)).equals(BigInteger.ONE)) {
            throw new RuntimeException("GCD of a and k must be equal to one");
        }
        RingInteger[] b = new RingInteger[k];
        for(int i = 0; i < k; ++i) {
            b[i] = new RingInteger(f(a, c, k, i), k);
        }
        for(int i = 0; i < k; ++i) {
            if(RingInteger.zero(k).equals(b[i]) && i != k-1) {
                b = ArrayUtils.swap(b, i, k-1);
            }
            if(RingInteger.one(k).equals(b[i]) && i != 0) {
                b = ArrayUtils.swap(b, i, 0);
            }
        }
        return b;
    }

    public static RingInteger[] genG(int a, int c, int k) {
        RingInteger[] substitution = substitution(a, c, k);
        RingInteger[] P = new RingInteger[k];
        P[0] = substitution[0];
        for(int i = 0; i < k-1; ++i) {
            P[substitution[i].getNumber()] = substitution[i+1];
        }
        return P;
    }

    public static RingInteger[][] additionTable(int k, RingInteger[] unityRow) {
        RingInteger[][] table = new RingInteger[k][k];
        for(int i = 0; i < k; ++i) {
            Arrays.fill(table[i], RingInteger.zero(k));
            table[0][i] = RingInteger.valueOf(i, k);
            table[i][0] = RingInteger.valueOf(i, k);
            table[1][i] = unityRow[i];
            table[i][1] = unityRow[i];
        }
        int prevElement = 1;
        while (!table[prevElement][1].equals(RingInteger.zero(k))) {
            RingInteger element = table[prevElement][1];
            for(int i = 2; i < k; ++i) {
                table[element.getNumber()][i] = table[1][table[prevElement][i].getNumber()];
                table[i][element.getNumber()] = table[element.getNumber()][i];
            }
            prevElement = element.getNumber();
        }
        log.debug(Arrays.deepToString(table));
        return table;
    }

    public static RingInteger[][] multiplicationTable(int k, RingInteger[][] additionTable) {
        RingInteger[][] table = new RingInteger[k][k];
        for(int i = 0; i < k; ++i) {
            Arrays.fill(table[i], RingInteger.zero(k));
            table[0][i] = RingInteger.zero(k);
            table[i][0] = RingInteger.zero(k);
            table[1][i] = RingInteger.valueOf(i, k);
            table[i][1] = RingInteger.valueOf(i, k);
        }
        int prevElement = 1;
        while (!additionTable[prevElement][1].equals(RingInteger.zero(k))) {
            RingInteger element = additionTable[prevElement][1];
            for(int i = 2; i < k; ++i) {
                table[element.getNumber()][i] = additionTable[i][table[prevElement][i].getNumber()];
                table[i][element.getNumber()] = table[element.getNumber()][i];
            }
            prevElement = element.getNumber();
        }
        log.debug(Arrays.deepToString(table));
        return table;
    }

    /*
    * Converts value from ring Zk to associative-commutative ring Gk
    * with given substitution if flag toACRing is true and backwards if false
    * Example:
    * 0(Zk) -> substitution[0] (Gk)
    * 1 -> substitution[1]
    * 2 -> substitution[2]
    * */
    public static RingInteger isomorphism(RingInteger value, RingInteger[] substitution, boolean toACRing) {
        RingInteger isomorph = null;
        if(toACRing) {
            if(value.getNumber() == 0) {
                isomorph = substitution[substitution.length-1];
            } else {
                isomorph = substitution[value.getNumber()-1];
            }
        } else {
            for (int i = 0; i < substitution.length; ++i) {
                if(value.equals(substitution[i])) {
                    isomorph = new RingInteger((i+1)%substitution[i].getM(), substitution[i].getM());
                    break;
                }
            }
        }
        return isomorph;
    }

    /*
    * Using isomorphism between ring and associative-commutative ring transform matrix values from
    * ring to ass-comm ring
    * */
    public static RingInteger[][] matrixToACRing(RingInteger[][] matrix, RingInteger[] substritution) {
        RingInteger[][] ACRingMatrix = new RingInteger[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; ++i) {
            for(int j = 0; j < matrix[0].length; ++j) {
                ACRingMatrix[i][j] = isomorphism(matrix[i][j], substritution, true);
            }
        }
        return ACRingMatrix;
    }

    public static RingInteger[][] matrixFromACRing(RingInteger[][] matrix, RingInteger[] substritution) {
        RingInteger[][] ACRingMatrix = new RingInteger[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; ++i) {
            for(int j = 0; j < matrix[0].length; ++j) {
                ACRingMatrix[i][j] = isomorphism(matrix[i][j], substritution, false);
            }
        }
        return ACRingMatrix;
    }

    public static RingInteger[] vectorToACRing(RingInteger[] vector, RingInteger[] substritution) {
        RingInteger[] ACRingVector = new RingInteger[vector.length];
        for(int i = 0; i < vector.length; ++i) {
            ACRingVector[i] = isomorphism(vector[i], substritution, true);
        }
        return ACRingVector;
    }

    public static RingInteger[] vectorFromACRing(RingInteger[] vector, RingInteger[] substritution) {
        RingInteger[] ACRingVector = new RingInteger[vector.length];
        for(int i = 0; i < vector.length; ++i) {
            ACRingVector[i] = isomorphism(vector[i], substritution, false);
        }
        return ACRingVector;
    }

    //
    public static boolean isDivisorByOne(RingInteger value, RingInteger[] substitution) {
        // Transform value from AC ring to ordinary ring
        RingInteger valueInRing = isomorphism(value, substitution, false);

        // Check that gcd with ring order is 1
        return BigInteger.valueOf(valueInRing.getNumber())
                .gcd(BigInteger.valueOf(valueInRing.getM())).equals(BigInteger.ONE);
    }


    /**
     *
     * @param basisVectorsSet basis vectors computed in fields and ghost rings with order of factors
     * @param factors factors of ring order
     * @param ringOrder order of ring
     * @return Resulted basis of ring with order ringOrder
     */
    public static RingInteger[][] combineRingBasis(List<RingInteger[][]> basisVectorsSet,
                                                   List<ComputationUtils.FactorModel> factors,
                                                   int ringOrder) {
        List<RingInteger[]> basisVectors = new LinkedList<>();
        for(int i = 0; i < basisVectorsSet.size(); ++i) {
            RingInteger[][] basis = basisVectorsSet.get(i);
            int m = ringOrder / (int) Math.pow(factors.get(i).number, factors.get(i).pow);
            for(RingInteger[] vector : basis) {
                RingInteger[] vectorInRing = new RingInteger[vector.length];
                for(int j = 0; j < vector.length; ++j) {
                    vectorInRing[j] = new RingInteger(vector[j].getNumber(), ringOrder);
                }
                basisVectors.add(RingUtils.multiplyVectorByConst(vectorInRing, RingInteger.valueOf(m, ringOrder)));
            }
        }

        return basisVectors.toArray(RingInteger[][]::new);
    }

    private static int f(int a, int c, int k, int i) {
        return BigInteger.valueOf(a)
                .multiply(BigInteger.valueOf(i))
                .add(BigInteger.valueOf(c))
                .mod(BigInteger.valueOf(k))
                .intValue();
    }
}
