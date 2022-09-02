package ua.knu.csc.iss.ynortman.ring;

import ua.knu.csc.iss.ynortman.utils.ArrayUtils;

import java.math.BigInteger;

public class RingOps {
    public static int[] genG(int a, int c, int k) {
        if(!BigInteger.valueOf(a).gcd(BigInteger.valueOf(k)).equals(BigInteger.ONE)) {
            throw new RuntimeException("GCD of a and k must be equal to one");
        }
        Integer[] b = new Integer[k];
        for(int i = 0; i < k; ++i) {
            b[i] = f(a, c, k, i);
        }
        for(int i = 0; i < k; ++i) {
            if(b[i] == 0 && i != k-1) {
                b = ArrayUtils.swap(b, i, k-1);
            }
            if(b[i] == 1 && i != 0) {
                b = ArrayUtils.swap(b, i, 0);
            }
        }
        int[] P = new int[k];
        P[0] = b[0];
        for(int i = 0; i < k-1; ++i) {
            P[b[i]] = b[i+1];
        }
        return P;
    }

    private static int f(int a, int c, int k, int i) {
        return BigInteger.valueOf(a)
                .multiply(BigInteger.valueOf(i))
                .add(BigInteger.valueOf(c))
                .mod(BigInteger.valueOf(k))
                .intValue();
    }
}
