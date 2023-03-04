package ua.knu.csc.iss.ynortman.ring.model;

import lombok.Data;

@Data
public class HLDE {
    final RingInteger[] a;

    public HLDE(int[] coefs, int m) {
        a = new RingInteger[coefs.length];
        for(int i = 0; i < coefs.length; ++i) {
            a[i] = new RingInteger(coefs[i], m);
        }
    }

    public int size() {
        return a.length;
    }

    public RingInteger get(int i) {
        return a[i];
    }
}
