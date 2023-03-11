package ua.knu.csc.iss.ynortman.ring.model;

import lombok.Data;

@Data
public class HLDE {
    private final RingInteger[] a;
    private final int[] coefs;

    public HLDE(int[] coefs, int m) {
        this.coefs = new int[coefs.length];
        a = new RingInteger[coefs.length];
        for(int i = 0; i < coefs.length; ++i) {
            a[i] = new RingInteger(coefs[i], m);
            this.coefs[i] = a[i].getNumber();
        }
    }

    public HLDE(RingInteger[] a) {
        this.a = a;
        this.coefs = new int[a.length];
        for (int i = 0; i < a.length; ++i) {
            this.coefs[i] = a[i].getNumber();
        }
    }

    public int size() {
        return a.length;
    }

    public int ringOrder() {
        return a[0].getM();
    }

    public RingInteger get(int i) {
        return a[i];
    }
}
