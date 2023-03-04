package ua.knu.csc.iss.ynortman.ring.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class NHLDESystem {
    final NHLDE[] system;
    final int m;


    public NHLDESystem(NHLDE[] system) {
        this.system = system;
        this.m = system[0].b.getM();
    }

    public int size() {
        return system.length;
    }

    public NHLDE getEquation(int i) {
        return system[i];
    }

    public void setEquation(int i, NHLDE equation) {
        system[i] = equation;
    }

    @Override
    public String toString() {
        StringBuilder arrRow = new StringBuilder();
        for(int i = 0; i < system.length; ++i) {
            arrRow.append("{a[")
                    .append(i)
                    .append("]=[");
            for (int j = 0; j < system[i].a.length; ++j) {
                arrRow.append(system[i].a[j].getNumber());
                if(j+1 != system[i].a.length) {
                    arrRow.append(", ");
                }
            }
            arrRow.append("]; b=")
                    .append(system[i].b.getNumber())
                    .append("}");
            if(i + 1 != system.length) {
                arrRow.append(", ");
            }
        }
        return arrRow.toString();
    }
}
