package ua.knu.csc.iss.ynortman.ring;

import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;
import ua.knu.csc.iss.ynortman.utils.ComputationUtils;

import java.math.BigInteger;
import java.util.Arrays;

@Slf4j
public class RingUtils {
    public static RingInteger[] multiplyVectorByConst(RingInteger[] vector, RingInteger cnst) {
        RingInteger[] result = new RingInteger[vector.length];
        for(int i = 0; i < vector.length; ++i) {
            result[i] = vector[i].multiply(cnst);
        }
        return result;
    }

    public static boolean isZeroVector(RingInteger[] vector) {
        for (RingInteger ringInteger : vector) {
            if (ringInteger.equals(RingInteger.zero(vector[0].getM()))) {
                return true;
            }
        }
        return false;
    }

    public static RingInteger[] addVectors(RingInteger[] a, RingInteger[] b) {
        if(a.length != b.length) {
            throw new IllegalArgumentException("Vectors should be the same size");
        }
        RingInteger[] result = new RingInteger[a.length];
        for(int i = 0; i < a.length; ++i) {
            result[i] = a[i].add(b[i]);
        }
        return result;
    }

    public static RingInteger[] substractVectors(RingInteger[] a, RingInteger[] b) {
        if(a.length != b.length) {
            throw new IllegalArgumentException("Vectors should be the same size");
        }
        RingInteger[] result = new RingInteger[a.length];
        for(int i = 0; i < a.length; ++i) {
            result[i] = a[i].substract(b[i]);
        }
        return result;
    }

    public static RingInteger[] zeroArray(int n, int r) {
        RingInteger[] arr = new RingInteger[n];
        for(int i = 0; i < n; ++i) {
            arr[i] = RingInteger.zero(r);
        }
        return arr;
    }

    /**
     * Computes greatest common divisor of all elements in array arr
     */
    public static RingInteger gcd(RingInteger[] arr) {
        RingInteger result = RingInteger.zero(arr[0].getM());
        for(RingInteger element : arr) {
            result = result.gcd(element);
            if(RingInteger.one(arr[0].getM()).equals(result)) {
                return RingInteger.one(arr[0].getM());
            }
        }
        return result;
    }

    /**
     * Generates random vector that has n elements in ring r
     */
    public static RingInteger[] randomVector(int n, int r) {
        RingInteger[] vector = new RingInteger[n];
        for(int i = 0; i < n; ++i) {
            vector[i] = RingInteger.random(r);
        }
        // simplifies ? vector
        return simplifyVector(vector);
    }

    /**
     *
     * @param vector original vector
     * @return simplified vector
     */
    public static RingInteger[] simplifyVector(RingInteger[] vector) {
        RingInteger[] simplified = new RingInteger[vector.length];
        System.arraycopy(vector, 0, simplified, 0, vector.length);
        RingInteger gcd = gcd(vector);
        if(RingInteger.zero(1).equals(gcd)) {
            log.warn("GCD is zero => vector is all zero");
            return vector;
        }
        if(!RingInteger.one(gcd.getM()).equals(gcd)) {
            for(int i = 0; i < vector.length; ++i) {
                simplified[i] = vector[i].divide(gcd);
            }
        }
        return simplified;
    }

    public static RingInteger[] extendedEuclid(RingInteger[] equation) {
        if (equation.length == 1) {
            int res = BigInteger.valueOf(equation[0].getNumber()).modInverse(BigInteger.valueOf(equation[0].getM())).intValue();
            return new RingInteger[] {
                    RingInteger.one(equation[0].getM()),
                    new RingInteger(res, equation[0].getM())
            };
        }
        int[] coordinates = new int[equation.length];
        for (int i = 0; i < equation.length; ++i) {
            coordinates[i] = equation[i].getNumber();
        }
        int[] extendedEuclid = ComputationUtils.extendedEuclid(coordinates);
        log.debug("extended euclid int solution: {}", Arrays.toString(extendedEuclid));
        RingInteger[] result = new RingInteger[extendedEuclid.length];
        for (int i = 0; i < extendedEuclid.length; ++i) {
            result[i] = new RingInteger(extendedEuclid[i], equation[0].getM());
        }
        return result;
    }

}
