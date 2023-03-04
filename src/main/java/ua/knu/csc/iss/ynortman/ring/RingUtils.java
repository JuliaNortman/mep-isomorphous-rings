package ua.knu.csc.iss.ynortman.ring;

import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

import java.math.BigInteger;

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
        // спрощує ? вектор
        RingInteger gcd = gcd(vector);
        if(!RingInteger.one(r).equals(gcd)) {
            for(int i = 0; i < n; ++i) {
                vector[i] = vector[i].divide(gcd);
            }
        }
        return vector;
    }
}
