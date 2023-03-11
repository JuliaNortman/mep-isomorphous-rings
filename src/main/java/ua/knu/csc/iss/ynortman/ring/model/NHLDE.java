package ua.knu.csc.iss.ynortman.ring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.RingUtils;

@Data
@AllArgsConstructor
@Slf4j
public class NHLDE {
    private final HLDE a;
    private final RingInteger b;

    private final int[] coefs;
    private final int constantTerm;

    public NHLDE(int[] coefs, int constant, int m) {
        this.constantTerm = constant;
        this.a = new HLDE(coefs, m);
        this.b = new RingInteger(constant, m);
        this.coefs = this.a.getCoefs();
    }

    public NHLDE(RingInteger[] a, RingInteger b) {
        this.a = new HLDE(a);
        this.b = b;
        this.coefs = new int[a.length];
        for (int i = 0; i < a.length; ++i) {
            this.coefs[i] = a[i].getNumber();
        }
        this.constantTerm = b.getNumber();
    }

    public NHLDE(HLDE a, RingInteger b) {
        this.a = a;
        this.b = b;
        this.coefs = new int[a.size()];
        for (int i = 0; i < a.size(); ++i) {
            this.coefs[i] = a.get(i).getNumber();
        }
        this.constantTerm = b.getNumber();
    }

    public int ringOrder() {
        return this.a.get(0).getM();
    }


    public int size() {
        return a.size();
    }

    public RingInteger get(int i) {
        return a.get(i);
    }

    public String toString() {
        StringBuilder arrRow = new StringBuilder();
        arrRow.append("a=[");
        for (int j = 0; j < a.size(); ++j) {
            arrRow.append(a.get(j).getNumber());
            if(j+1 != a.size()) {
                arrRow.append(", ");
            }
        }
        arrRow.append("]; b=")
                .append(b.getNumber())
                .append("}");
        return arrRow.toString();
    }

    public static boolean checkSolution(final RingInteger[][] basis, NHLDE nhlde) {
        RingInteger[][] multBasis = new RingInteger[basis.length-1][basis[0].length];
        for(int i = 1; i < basis.length; ++i) {
            RingInteger basisCoef = RingInteger.random(basis[0][0].getM());
            multBasis[i-1] = RingUtils.multiplyVectorByConst(basis[i], basisCoef);
        }
        RingInteger[] sumVector = RingUtils.zeroArray(basis[0].length, basis[0][0].getM());
        for (RingInteger[] ringIntegers : multBasis) {
            sumVector = RingUtils.addVectors(sumVector, ringIntegers);
        }

        sumVector = RingUtils.addVectors(sumVector, basis[0]);

        RingInteger res = RingInteger.zero(nhlde.ringOrder());
        for (int i = 0; i < nhlde.size(); ++i) {
            res = res.add(nhlde.get(i).multiply(sumVector[i]));
        }
        return res.equals(nhlde.getB());
    }

    public static RingInteger[] randomSolutionVector(final RingInteger[][] basis) {
        RingInteger[][] multBasis = new RingInteger[basis.length-1][basis[0].length];
        for(int i = 1; i < basis.length; ++i) {
            RingInteger basisCoef = RingInteger.random(basis[0][0].getM());
            multBasis[i-1] = RingUtils.multiplyVectorByConst(basis[i], basisCoef);
        }
        RingInteger[] sumVector = RingUtils.zeroArray(basis[0].length, basis[0][0].getM());
        for (RingInteger[] ringIntegers : multBasis) {
            sumVector = RingUtils.addVectors(sumVector, ringIntegers);
        }

        sumVector = RingUtils.addVectors(sumVector, basis[0]);

        return sumVector;
    }
}
