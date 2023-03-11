package ua.knu.csc.iss.ynortman.utils;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.RingUtils;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

import java.math.BigInteger;
import java.util.Random;

@Slf4j
public class MatrixUtils {

    @ToString
    @AllArgsConstructor
    /*
    * Model that encapsulates matrix and corresponding to it inverted matrix
    * */
    public static class InvertibleMatrixModel {
        public final RingInteger[][] matrix;
        public final RingInteger[][] inverted;
    }

    /*
    * Generates matrix with m rows and n columns in the following way:
    * 1. Generates square nonsingular matrix m*m
    * 2. Fills the first n-m columns with ONE
     */
    public static RingInteger[][] nonSingularSimpleMatrix(int m, int n, int r) {
        RingInteger[][] matrix = new RingInteger[m][n];
        RingInteger[][] squareMatrix = nonSingularSquareMatrix(m, r);
        for(int i = 0; i < m; ++i) {
            for(int j = 0; j < n; ++j) {
                if(j < n-m) {
                    matrix[i][j] = RingInteger.one(r);
                } else {
                    matrix[i][j] = squareMatrix[i][j - (n-m)];
                }
            }
        }
        return matrix;
    }

    /*
    * Generates random nonsingular square matrix with dimension dim in ring r
    * 1. Upper triangular matrix is generated (which is nonsingular by definition)
    * 2. Perform operations that do not change singularity
    * Operations: Interchange two rows, multiply each element in a row by a non-zero number,
    * Multiply a row by a non-zero number and substract the result from another row
    * */
    public static RingInteger[][] nonSingularSquareMatrix(int dim, int r) {
        RingInteger[][] matrix = upperTriangularMatrix(dim, r);
        log.debug(MatrixUtils.printMatrix(matrix, ""));
        Random random = new Random();
        int iterations = random.nextInt(200 - 20) + 20;
        for(int i = 0; i < iterations; ++i) {
            int operation = random.nextInt(4);
            int j = random.nextInt(dim);
            int k = random.nextInt(dim);
            switch (operation) {
                case 0: {
                    //Interchange two rows
                    RingInteger[] tmp = matrix[j];
                    matrix[j] = matrix[k];
                    matrix[k] = tmp;
                    break;
                }
                case 1: {
                    // Multiply each element in a row by a non-zero number
                    RingInteger cnst = RingInteger.random(r);
                    // make sure that number is nonzero
                    if(RingInteger.zero(r).equals(cnst)) {
                        cnst = RingInteger.one(r);
                    }
                    RingInteger[] tmp = RingUtils.multiplyVectorByConst(matrix[j], cnst);
                    if(!RingUtils.isZeroVector(tmp)) {
                        matrix[j] = tmp;
                        break;
                    }
                }
                case 2: {
                    //Multiply a row by a non-zero number and add the result to another row
                    RingInteger cnst = RingInteger.random(r);
                    if(RingInteger.zero(r).equals(cnst)) {
                        cnst = RingInteger.one(r);
                    }
                    RingInteger[] tmp = RingUtils.addVectors(matrix[j],
                            RingUtils.multiplyVectorByConst(matrix[k], cnst));
                    if(!RingUtils.isZeroVector(tmp)) {
                        matrix[j] = tmp;
                        break;
                    }
                }
                case 3: {
                    //Multiply a row by a non-zero number and substract the result from another row
                    RingInteger cnst = RingInteger.random(r);
                    if(RingInteger.zero(r).equals(cnst)) {
                        cnst = RingInteger.one(r);
                    }
                    RingInteger[] tmp = RingUtils.substractVectors(matrix[j],
                            RingUtils.multiplyVectorByConst(matrix[k], cnst));
                    if(!RingUtils.isZeroVector(tmp)) {
                        matrix[j] = tmp;
                        break;
                    }
                }
            }
        }
        return matrix;
    }


    /*
    * Generates upper triangular matrix with dimension dim in ring r
    * This matrix is nonsingular by definition
    * */
    private static RingInteger[][] upperTriangularMatrix(int dim, int r) {
        RingInteger[][] matrix = new RingInteger[dim][dim];
        for(int i = 0; i < dim; ++i) {
            for(int j = 0; j < dim; ++j) {
                if(j < i) {
                    matrix[i][j] = RingInteger.zero(r);
                } else if(j ==i) {
                    RingInteger el = RingInteger.zero(r);
                    while (RingInteger.zero(r).equals(el)) {
                        el = RingInteger.random(r);
                    }
                    matrix[i][j] = el;
                } else {
                    matrix[i][j] = RingInteger.random(r);
                }
            }
        }
        return matrix;
    }

    public static RingInteger[][] matrixMultiply(RingInteger[][] A, RingInteger[][] B) {
        RingInteger[][] result = new RingInteger[B.length][B[0].length];
        for(int i = 0; i < B.length; ++i) {
            result[i] = RingUtils.zeroArray(B[0].length, B[0][0].getM());
        }
        for (int i = 0; i < A.length; ++i) {
            for (int j = 0; j < B[0].length; ++j) {
                for(int k = 0; k < A[i].length; ++k) {
                    result[i][j] = result[i][j].add(A[i][k].multiply(B[k][j]));
                }
            }
        }
        return result;
    }

    /*
    * Generates upper-triangular matrix M (3n*3n) of form
    * {I(n), A, B}
    * {0, I(n), B}
    * {0, 0, I(n)}
    * and its inverted matrix M^(-1) which looks like
    * {I(n), -A, AB}
    * {0, I(n), -B}
    * {0, 0, I(n)}
    * */
    public static InvertibleMatrixModel triangularInvertibleMatrix(int n, int r) {
        RingInteger[][] A = nonSingularSquareMatrix(n, r);
        //log.debug(Arrays.deepToString(A));
        RingInteger[][] B = nonSingularSquareMatrix(n, r);
        //log.debug(Arrays.deepToString(B));
        RingInteger[][] AB = matrixMultiply(A, B);
        RingInteger[][] matrix = new RingInteger[3*n][3*n];
        RingInteger[][] inverted = new RingInteger[3*n][3*n];
        for(int i = 0; i < matrix.length; ++i) {
            for(int j = 0; j < matrix[i].length; ++j) {
                if(i == j) {
                    matrix[i][j] = RingInteger.one(r);
                    inverted[i][j] = RingInteger.one(r);
                } else if(j >= n && j < 2 * n && i < n) {
                    matrix[i][j] = A[i][j-n];
                    inverted[i][j] = A[i][j-n].complement();
                } else if(j >= 2*n && j < 3 * n && i < 2*n && i >= n) {
                    matrix[i][j] = B[i-n][j-2*n];
                    inverted[i][j] = B[i-n][j-2*n].complement();
                } else if(j >= 2*n && j < 3*n && i < 2*n) {
                    matrix[i][j] = RingInteger.zero(r);
                    inverted[i][j] = AB[i][j-2*n];
                } else {
                    matrix[i][j] = RingInteger.zero(r);
                    inverted[i][j] = RingInteger.zero(r);
                }
            }
        }
        return new InvertibleMatrixModel(matrix, inverted);
    }

    public static RingInteger[][] operator(RingInteger[][] operator, RingInteger[][] matrix) {
        return matrixMultiply(operator, matrix);
    }

    public static RingInteger[][] rowToColumn(RingInteger[] row) {
        RingInteger[][] column = new RingInteger[row.length][1];
        for (int i = 0; i < row.length; ++i) {
            column[i][0] = row[i];
        }
        return column;
    }

    public static RingInteger[] columnToRow(RingInteger[][] column) {
        RingInteger[] row = new RingInteger[column.length];
        for(int i = 0; i < column.length; ++i) {
            row[i] = column[i][0];
        }
        return row;
    }

    /*
    * Adds vector to matrix column with index colIndex
    * */
    public static RingInteger[][] matrixAddVectorColumn(RingInteger[][] matrix, RingInteger[] vector,
                                                       int colIndex) {
        for(int i = 0; i < matrix.length; ++i) {
            matrix[i][colIndex] = matrix[i][colIndex].add(vector[i]);
        }
        return matrix;
    }

    public static String printMatrix(RingInteger[][] matrix, String name) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("Matrix ")
                .append(name)
                .append(":\n");
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (j == 0) {
                    stringBuilder.append("{");
                }
                stringBuilder.append(matrix[i][j].getNumber());
                if (j != matrix[i].length-1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("}");
            if (i != matrix.length-1) {
                stringBuilder.append(",\n");
            }
        }
        return stringBuilder.toString();
    }

    public static String printVector(RingInteger[] vector, String name) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("Vector ")
                .append(name)
                .append(":\n");
        for (int i = 0; i < vector.length; ++i) {
            if (i == 0) {
                stringBuilder.append("{");
            }
            stringBuilder.append(vector[i].getNumber());
            if (i != vector.length-1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
