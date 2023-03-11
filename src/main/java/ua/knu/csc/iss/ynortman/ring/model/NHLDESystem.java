package ua.knu.csc.iss.ynortman.ring.model;

import lombok.Data;

@Data
public class NHLDESystem {
    private final NHLDE[] system;

    public NHLDESystem(NHLDE[] system) {
        this.system = system;
    }

    public NHLDESystem(RingInteger[][] system, RingInteger[] constants) {
        this.system = new NHLDE[system.length];
        for(int i = 0; i < system.length; ++i) {
            this.system[i] = new NHLDE(system[i], constants[i]);
        }
    }

    public NHLDESystem(int[][] coefs, int[] constants, int m) {
        this.system = new NHLDE[coefs.length];
        for (int i = 0; i < coefs.length; ++i) {
            this.system[i] = new NHLDE(coefs[i], constants[i], m);
        }
    }

    public int size() {
        return system.length;
    }

    public NHLDE get(int i) {
        return system[i];
    }

    @Override
    public String toString() {
        StringBuilder arrRow = new StringBuilder();
        for(int i = 0; i < system.length; ++i) {
            arrRow.append("{a[")
                    .append(i)
                    .append("]=[");
            for (int j = 0; j < system[i].getA().size(); ++j) {
                arrRow.append(system[i].getA().get(j).getNumber());
                if(j+1 != system[i].getA().size()) {
                    arrRow.append(", ");
                }
            }
            arrRow.append("]; b=")
                    .append(system[i].getB().getNumber())
                    .append("}");
            if(i + 1 != system.length) {
                arrRow.append(", ");
            }
        }
        return arrRow.toString();
    }
}
