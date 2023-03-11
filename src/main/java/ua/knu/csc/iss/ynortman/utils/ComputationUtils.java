package ua.knu.csc.iss.ynortman.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ComputationUtils {

    // https://www.geeksforgeeks.org/prime-numbers/
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }

        // Check if n=2 or n=3
        if (n == 2 || n == 3) {
            return true;
        }

        // Check whether n is divisible by 2 or 3
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }

        // Check from 5 to square root of n
        // Iterate i by (i+6)
        for (int i = 5; i <= Math.sqrt(n); i = i + 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }


    @ToString
    @AllArgsConstructor
    @EqualsAndHashCode
    /*
    * Model that represents factor of a number in form of prime_number^pow
    * */
    public static class FactorModel {
        public final int number;
        public final int pow;
    }

    // https://iq.opengenus.org/integer-factorization-algorithms/
    // trial division
    public static List<FactorModel> factorization(int n) {
        LinkedList<FactorModel> factors = new LinkedList<>();
        int f = 2;
        while (n > 1) {
            if(n % f == 0) {
                n /= f;
                if(factors.isEmpty() || factors.getLast().number != f) {
                    factors.add(new FactorModel(f, 1));
                } else {
                    FactorModel factor = new FactorModel(f, factors.getLast().pow+1);
                    factors.removeLast();
                    factors.add(factor);
                }
            } else {
                f++;
            }
        }
        return factors;
    }

    public static int gcd(int a, int b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
    }

    /**
     *
     * @param p first number
     * @param q second number
     * @return  triple (d, a, b) such that d = gcd(p, q), ap + bq = d
     */
    public static int[] extendedEuclid(int p, int q) {
        if (q == 0)
            return new int[]{p, 1, 0};

        int[] vals = extendedEuclid(q, p % q);
        int d = vals[0];
        int a = vals[2];
        int b = vals[1] - (p / q) * vals[2];
        return new int[]{d, a, b};
    }

    public static int[] extendedEuclid(int[] vector) {
        int[] result = new int[vector.length+1];
        Arrays.fill(result, 1);
        List<Integer> intermidiateCoeffs = new LinkedList<>();
        int[] gcd = extendedEuclid(vector[vector.length-2], vector[vector.length-1]);

        intermidiateCoeffs.add(gcd[1]);
        intermidiateCoeffs.add(gcd[2]);
        for(int i = vector.length-2; i > 0; --i) {
            gcd = extendedEuclid(vector[i-1], gcd[0]);
            intermidiateCoeffs.add(gcd[1]);
            intermidiateCoeffs.add(gcd[2]);
        }
        for(int i = intermidiateCoeffs.size()-1; i >=0; --i) {
            if(i % 2 == 0) {
                result[result.length-1-((i+2)/2)] *= intermidiateCoeffs.get(i);
            } else {
                for (int j = result.length-((i+1)/2); j < result.length; ++j) {
                    result[j] *= intermidiateCoeffs.get(i);
                }
            }
        }
        result[0] = gcd[0];
        return result;
    }
}
