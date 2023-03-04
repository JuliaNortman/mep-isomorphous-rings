package ua.knu.csc.iss.ynortman.ring.model;

import lombok.Data;

import java.util.Random;

@Data
public class RingInteger implements Comparable<RingInteger>{
    private final int number;
    private final int m;

    // should be used only in equals() operation otherwise may produce errors
    // while performing add() or multiply() operations because ring of this constant is always 1
//    public static final RingInteger ZERO;
//    public static final RingInteger ONE;

//    static {
//        ZERO = new RingInteger(0, 1);
//        ONE = new RingInteger(1, 1);
//    }

    public RingInteger(int number, int m) {
        this.number = complement(number, m);
        this.m = m;
    }

    public static RingInteger valueOf(int number, int m) {
        return new RingInteger(number, m);
    }

    public static RingInteger zero(int r) {
        return new RingInteger(0, r);
    }

    public static RingInteger one(int r) {
        return new RingInteger(1, r);
    }

    public RingInteger add(RingInteger other) {
//        if(ZERO.equals(this) || ONE.equals(this)) {
//            return new RingInteger((number + other.number) % other.m, other.m);
//        }
//        if(ZERO.equals(other) || ONE.equals(other)) {
//            return new RingInteger((number + other.number) % m, m);
//        }
        if(m != other.m) {
            throw new ArithmeticException("Cannot add numbers which ring modules are not equal");
        }
        return new RingInteger((number + other.number) % m, m);
    }

    public RingInteger substract(RingInteger other) {
//        if(ZERO.equals(this) || ONE.equals(this)) {
//            int res = number - other.number;
//            while (res < 0) {
//                res += other.m;
//            }
//            return new RingInteger(res, other.m);
//        }
//        if(ZERO.equals(other) || ONE.equals(other)) {
//            int res = number - other.number;
//            while (res < 0) {
//                res += m;
//            }
//            return new RingInteger(res, m);
//        }
        if(m != other.m) {
            throw new ArithmeticException();
        }
        int res = number - other.number;
        while (res < 0) {
            res += m;
        }
        return new RingInteger(res, m);
    }

    // обернений
    public RingInteger complement() {
//        if(ZERO.equals(this) || ONE.equals(this)) {
//            throw new ArithmeticException("The ring of this values are unknown");
//        }
        return new RingInteger(complement(number, m), m);
    }

    public RingInteger multiply(RingInteger other) {
//        if(ZERO.equals(this) || ONE.equals(this)) {
//            return new RingInteger((number * other.number) % other.m, other.m);
//        }
//        if(ZERO.equals(other) || ONE.equals(other)) {
//            return new RingInteger((number * other.number) % m, m);
//        }
        if(m != other.m) {
            throw new ArithmeticException();
        }
        return new RingInteger((number * other.number) % m, m);
    }

    public RingInteger divide(RingInteger other) {
        if(m != other.m) {
            throw new ArithmeticException();
        }
        return new RingInteger((number / other.number) % m, m);
    }

    // Generates random value in ring
    public static RingInteger random(int r) {
        Random rand = new Random();
        return new RingInteger(rand.nextInt(r), r);
    }

    public RingInteger gcd(RingInteger other) {
        if (zero(other.m).equals(other)) {
            return this;
        }
        return other.gcd(new RingInteger(other.number % number, m));
    }

    @Override
    public int compareTo(RingInteger o) {
        return Integer.compare(number, o.number);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RingInteger)) {
            return false;
        }
        RingInteger other = (RingInteger) o;
        if (other.number == 0) {
            return number == 0;
        } else if (other.number == 1) {
            return number == 1;
        }
        return number == other.number && m == other.m;
    }

    private int complement(int number, int r) {
        if(number < 0) {
            while (number < 0) {
                number += r;
            }
        }
        return number % r;
    }

}
