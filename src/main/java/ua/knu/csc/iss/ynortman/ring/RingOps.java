package ua.knu.csc.iss.ynortman.ring;

import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.ArrayUtils;

import java.math.BigInteger;
import java.util.Arrays;

@Slf4j
public class RingOps {

    /*
    * Given order k and coefficients a,c of expression f(i)=a*i+c
    * defines substitution (підстановка) between ring Zk and associative-commutative ring Gk
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
//        if(!BigInteger.valueOf(a).gcd(BigInteger.valueOf(k)).equals(BigInteger.ONE)) {
//            throw new RuntimeException("GCD of a and k must be equal to one");
//        }
//        Integer[] b = new Integer[k];
//        for(int i = 0; i < k; ++i) {
//            b[i] = f(a, c, k, i);
//        }
//        for(int i = 0; i < k; ++i) {
//            if(b[i] == 0 && i != k-1) {
//                b = ArrayUtils.swap(b, i, k-1);
//            }
//            if(b[i] == 1 && i != 0) {
//                b = ArrayUtils.swap(b, i, 0);
//            }
//        }
        RingInteger[] substitution = substitution(a, c, k);
        RingInteger[] P = new RingInteger[k];
        P[0] = substitution[0];
        for(int i = 0; i < k-1; ++i) {
            P[substitution[i].getNumber()] = substitution[i+1];
        }
        return P;
    }

    public static int[][] additionTable(int k, int[] unityRow) {
        int[][] table = new int[k][k];
        for(int i = 0; i < k; ++i) {
            Arrays.fill(table[i], -1);
            table[0][i] = i;
            table[i][0] = i;
            table[1][i] = unityRow[i];
            table[i][1] = unityRow[i];
        }
        int prevElement = 1;
        while (table[prevElement][1] != 0) {
            int element = table[prevElement][1];
            for(int i = 2; i < k; ++i) {
                table[element][i] = table[1][table[prevElement][i]];
                table[i][element] = table[element][i];
            }
            prevElement = element;
        }
        log.debug(Arrays.deepToString(table));
        return table;
    }

    public static int[][] multiplicationTable(int k, int[][] additionTable) {
        int[][] table = new int[k][k];
        for(int i = 0; i < k; ++i) {
            Arrays.fill(table[i], -1);
            table[0][i] = 0;
            table[i][0] = 0;
            table[1][i] = i;
            table[i][1] = i;
        }
        int prevElement = 1;
        while (additionTable[prevElement][1] != 0) {
            int element = additionTable[prevElement][1];
            for(int i = 2; i < k; ++i) {
                table[element][i] = additionTable[i][table[prevElement][i]];
                table[i][element] = table[element][i];
            }
            prevElement = element;
        }
        log.debug(Arrays.deepToString(table));
        return table;
    }

    /*
    * Converts value from ring Zk to associative-commutative ring Gk
    * with given characteristic row
    * */
//    public static RingInteger fromRingToAssCommRing(RingInteger value, RingInteger[] charRow) {
//
//    }

    private static int f(int a, int c, int k, int i) {
        return BigInteger.valueOf(a)
                .multiply(BigInteger.valueOf(i))
                .add(BigInteger.valueOf(c))
                .mod(BigInteger.valueOf(k))
                .intValue();
    }
}
