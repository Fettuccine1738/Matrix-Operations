package newMatrix.domain;
// 25.05.2024


import java.util.ArrayList;
import java.util.List;

public abstract class GenericMatrix<E extends Number> {

     protected  abstract E add(E a, E b);
     protected  abstract E subtract(E a, E b);
     protected  abstract E multiply(E a, E b);
     protected  abstract E zero();
     protected abstract E divide(E numerator, E denominator);
//    protected  abstractvoid set(int i);

    protected abstract E[] wrapSolution(Number[] matrix);

//    protected List<E> list = new ArrayList<>();



    public  E[][] addition(E[][] aMatrix, E[][] bMatrix) {
        int rows; int columns;
        // new generic result
        E[][] cMatrix;
        if (aMatrix.length == bMatrix.length && aMatrix[0].length == bMatrix[0].length) {
            rows = aMatrix.length;;
            columns = bMatrix.length;
            // Generics cannot be instantiated directly
            // cast of type E is applied to Number
            cMatrix = (E[][]) new Number[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    cMatrix[i][j] = add(aMatrix[i][j], bMatrix[i][j]);
                }
            }
        } else throw new IllegalArgumentException("Matrix addition operation requires matrices of the same dimension.");
        return cMatrix;
    }

    public E[][] subtraction(E[][] aMatrix, E[][] bMatrix) {
        int rows; int columns;
        E[][] cMatrix;
        if (aMatrix.length == bMatrix.length && aMatrix[0].length == bMatrix[0].length) {
            rows = aMatrix.length;;
            columns = bMatrix.length;
            // Generics cannot be instantiated directly
            // cast of type E is applied to Number
            cMatrix = (E[][]) new Number[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    cMatrix[i][j] = subtract(aMatrix[i][j], bMatrix[i][j]);
                }
            }
        } else throw new IllegalArgumentException("Matrix addition requires matrices of the same dimension.");
        return cMatrix;
    }

    public E[][] multiplication (E[][] aMatrix, E[][] bMatrix) {
        int rows; int columns;
        E[][] cMatrix;

        // check if inner dimensions matches # 4 x 3  * 3 x 2 matches results in a 4 x 2 matrix
        if (aMatrix[0].length == bMatrix.length) {
            rows = aMatrix.length;
            columns = bMatrix[0].length;
            cMatrix = (E[][]) new Number[aMatrix.length][bMatrix[0].length];

            for (int i = 0; i < rows; i++) {

                for (int j = 0; j < columns; j++) {
                    cMatrix[i][j] = zero();
                    for (int k = 0; k < rows; k++) {
                        cMatrix[i][j] = add(cMatrix[i][j], multiply(aMatrix[i][k], bMatrix[k][j]));
                    }
                }

            }
        } else throw new IllegalArgumentException("Inner dimension of matrices are not equal.");
        return cMatrix;
    }

    public E[][] transpose(E[][] matrix) {
        E[][] transposeMatrix;
        int rows = matrix.length;
        int columns = matrix[0].length;
        // flip columns and rows
        transposeMatrix = (E[][]) new Number[columns][rows];

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < columns; j++) {
                transposeMatrix[i][j] = matrix[j][i];
            }
        }
        return transposeMatrix;
    }



    private E determinant(E[][] matrix) {
        if (matrix.length == 2) {
            // matrix 2 x 2 : det(A) {[a b], [c, d]} =  ad - cb
            E e1 = multiply(matrix[0][0], matrix[1][1]);
            E e2 = multiply(matrix[1][0], matrix[0][1]);
            return subtract(e1, e2);
        } else if (matrix.length == 3) {
            //Rule of Sarrus to calculate diagonal
            // positive diagonal
            E pDiagonal1 = multiply(multiply(matrix[0][0],matrix[1][1]), matrix[2][2]);
            E pDiagonal2 = multiply(multiply(matrix[0][1],matrix[1][2]), matrix[2][0]);
            E pDiagonal3 = multiply(multiply(matrix[0][2],matrix[1][0]), matrix[2][1]);
            E positiveSum = add(add(pDiagonal1,pDiagonal2), pDiagonal3);

            // negative diagonal

            E nDiagonal1 = multiply(multiply(matrix[0][2],matrix[1][1]), matrix[2][0]);
            E nDiagonal2 = multiply(multiply(matrix[0][0],matrix[1][2]), matrix[2][1]);
            E nDiagonal3 = multiply(multiply(matrix[0][1],matrix[1][0]), matrix[2][2]);
            E negativeSum = add(add(nDiagonal1, nDiagonal2), nDiagonal3);

            // subtract negative diagonal from positive diagonal
            return subtract(positiveSum, negativeSum);
        } else if (matrix.length > 3) {
            // Laplace expansion, algorithm needed


            return null;
        }

        return null;
    }

    public Object[] solveLinearEqns(E[][] system) {
        /*
         split augmented matrix into variables and constant
         and solve using Cramer's rule
         */
        Number[] objects;
        E[] temp;
        int rows; int columns;
        E[][] variables; E[] constants;
        E determinant;
        Number[] holdDeterminants;

        // Cramers rule is only used for square matrices (number of equations = number of unknowns
        if (system.length != (system[0].length - 1))
            throw new RuntimeException("Not  a square matrix");

        switch (system.length) {

            case 2:
                rows = system.length;
                columns = system[0].length  - 1; // separating variables and consts
                variables = (E[][]) new Number[rows][columns];
                constants = (E[]) new Number[rows];

                //set varaiables
                for ( int i = 0; i < system.length; i++) {
                    // omit last columns (constants)
                    if (system[0].length - 1 >= 0)
                        System.arraycopy(system[i], 0, variables[i], 0, system[0].length - 1);
                }
                // set constants
                constants[0] = system[0][2];
                constants[1] = system[1][2];

                determinant = determinant(variables);
                temp = (E[]) new Number[rows];
                /* E[] is not a subclass of Number
                although E extends Number. Solve type safety by instantiating a Object array.
                 */

                temp[0] = (E) variables[0][0];
                temp[1] = (E) variables[1][0];



                // replace variables with constants to solve for x1
                variables[0][0] = constants[0];
                variables[1][0] = constants[1];
                //invert the determinant
                E determinantX1 = determinant(variables);
                // revert changes from constant
                variables[0][0] = (E) temp[0];
                variables[1][0] = (E) temp[1];
                E x1 = divide(determinantX1, determinant);

                //replace column x2 with constants and calculate
                variables[0][1] = constants[0];
                variables[1][1] = constants[1];
                E determinantX2 = determinant(variables);
                E x2 = divide(determinantX2, determinant);

                // initailize array to hold determinants
                objects =  new Number[]{x1, x2};

                return wrapSolution(objects);
//
            case 3:
                rows = system.length;
                columns = system[0].length  - 1; // separating variables and consts
                variables = (E[][]) new Number[rows][columns];
                constants = (E[]) new Number[rows];

                for (int i = 0; i < rows; i++) {

                    if (system[0].length - 1 >= 0)
                        System.arraycopy(system[i], 0, variables[i], 0, system[0].length - 1);
                }
                constants[0] = system[0][3];
                constants[1] = system[1][3];
                constants[2] = system[2][3];

                determinant = determinant(variables);
                temp = (E[]) new Number[rows];
                /* E[] is not a subclass of Number
                although E extends Number. Solve type safety by instantiating a Object array.
                 */

                temp[0] = (E) variables[0][0];
                temp[1] = (E) variables[1][0];
                temp[2] = (E) variables[2][0];

                objects =  new Number[rows];
                holdDeterminants = new Number[rows];
                // replace variables with constants to solve for x1
                variables[0][0] = constants[0];
                variables[1][0] = constants[1];
                variables[2][0] = constants[2];
                //invert the determinant
                holdDeterminants[0] = determinant(variables);
                // revert changes from constant
                variables[0][0] = (E) temp[0];
                variables[1][0] = (E) temp[1];
                variables[2][0] = (E) temp[2];
                objects[0] = divide((E) holdDeterminants[0], determinant);

                //replace column x2 with constants and calculate
                temp[0] = (E) variables[0][1];
                temp[1] = (E) variables[1][1];
                temp[2] = (E) variables[2][1];
                variables[0][1] = constants[0];
                variables[1][1] = constants[1];
                variables[2][1] = constants[2];
                holdDeterminants[1] = determinant(variables);
                objects[1] = divide((E) holdDeterminants[1], determinant);

                // initailize array to hold determinants
                // revert changes from constant
                variables[0][1] = (E) temp[0];
                variables[1][1] = (E) temp[1];
                variables[2][1] = (E) temp[2];


                //replace column x3 with constants and calculate
                variables[0][2] = constants[0];
                variables[1][2] = constants[1];
                variables[2][2] = constants[2];
                holdDeterminants[2] = determinant(variables);
                objects[2] = divide((E) holdDeterminants[2], determinant);

                return wrapSolution(objects);
            default: throw new RuntimeException("Matrix too large.");
        }

        //        return null;
    }

    /* for laplace expansion

     */
    private int findZerothRow(Number[][] matrix) {
        int zerothRow = 0;
        int zerothRowCount = 0;
        // find row with the most zeros
        for (int i = 0; i < matrix.length; i++) {
            int count = 0; // count zeroes on every row
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == zero()) count++;
                else continue;
            }
            if (count > zerothRowCount) {
                zerothRow = i;
                zerothRowCount = count;
            }
        }  return zerothRow;
    }

    public static void printResult(Number[][] m1, Number[][] m2, Number[][] m3, char op) {
        for(int i = 0; i < m1.length; i++) {

            for (int j = 0; j < m1[0].length; j++) {
                System.out.print(" " + m1[i][j]);
            }

            if(i == m1.length / 2) {
                System.out.print("  " + op + " ");
            }else System.out.print("    ");

            for(int j = 0; j < m2[0].length; j++) {
                System.out.print(" " + m2[i][j]);
            }

            if( i == m1.length / 2) {
                System.out.print("  = ");
            } else System.out.print("    ");

            for (int j = 0; j < m3.length; j++) {
                System.out.print(m3[i][j] + " ");
            }
            System.out.println();
        }
    }







}
