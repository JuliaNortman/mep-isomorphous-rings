package ua.knu.csc.iss.ynortman.ring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Slf4j
public class NHLDE {
    final RingInteger[] a;
    final RingInteger b;

    public NHLDE multiplyConstant(RingInteger constant) {
        RingInteger[] aNew = new RingInteger[a.length];
        for(int i = 0; i < a.length; ++i) {
            aNew[i] = a[i].multiply(constant);
        }
        RingInteger bNew = b.multiply(constant);
        return new NHLDE(aNew, bNew);
    }

    public NHLDE substractEquation(NHLDE nhlde) {
        RingInteger[] aNew = new RingInteger[a.length];
        for(int i = 0; i < a.length; ++i) {
//            log.debug("First: {}, second: {}, result: {}", a[i], nhlde.a[i], a[i].substract(nhlde.a[i]));
            aNew[i] = a[i].substract(nhlde.a[i]);
        }
        RingInteger bNew = b.substract(nhlde.b);
        return new NHLDE(aNew, bNew);
    }

    public String toString() {
        StringBuilder arrRow = new StringBuilder();
        arrRow.append("a=[");
        for (int j = 0; j < a.length; ++j) {
            arrRow.append(a[j].getNumber());
            if(j+1 != a.length) {
                arrRow.append(", ");
            }
        }
        arrRow.append("]; b=")
                .append(b.getNumber())
                .append("}");
        return arrRow.toString();
    }
}
