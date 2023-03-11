package ua.knu.csc.iss.ynortman.ring.model;

import lombok.Data;

@Data
public class HLDESystem {
    private final HLDE[] system;
    private final int[][] coefs;

    public HLDESystem(HLDE[] system) {
        this.system = system;
        this.coefs = new int[system.length][system[0].size()];
        for (int i = 0; i < coefs.length; ++i) {
            coefs[i] = system[i].getCoefs();
        }
    }

    public HLDESystem(int[][] coefs, int m) {
        system = new HLDE[coefs.length];
        this.coefs = new int[coefs.length][coefs[0].length];
        for (int i = 0; i < coefs.length; ++i) {
            system[i] = new HLDE(coefs[i], m);
            this.coefs[i] = system[i].getCoefs();
        }
    }

    public int ringOrder() {
        return system[0].ringOrder();
    }

    public HLDE get(int i) {
        return system[i];
    }

    public int size() {
        return system.length;
    }
}
