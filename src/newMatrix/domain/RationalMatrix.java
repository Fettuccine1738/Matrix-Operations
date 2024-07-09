package newMatrix.domain;

import java.util.Arrays;

public class RationalMatrix extends GenericMatrix<Rational>{

    @Override
    protected Rational add(Rational e1, Rational e2) {
        return e1.add(e2);
    }

    @Override
    protected Rational multiply(Rational e1, Rational e2) {
        return e1.multiply(e2);
    }

    @Override
    protected Rational subtract(Rational e1, Rational e2) {
        return e1.subtract(e2);
    }

    //@Override
    protected Rational zero() {
        return new Rational(0, 1);
    }

    @Override
    protected Rational divide(Rational e1, Rational e2) {
        return multiply(e2, e1);
    }

    @Override
    protected Rational signOfCofactor(int zerothRow, int i) {
        return (zerothRow + 1 + i + 1) % 2 == 0 ? new Rational(1, 1) : new Rational(1, -1);
    }

    @Override
    protected Rational[] wrapSolution(Number[] matrix) {
        Rational[] solution = new Rational[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
           solution[i] = new Rational(matrix[i].intValue(), 1);
        }
        return solution;
    }

    private static Rational rezero() {
        return new Rational(0, 1);
    }
    // matrix operations
    // checks if argument is a square matrix
    private static void validate(Rational[][] rational) {
        if (rational.length != rational[0].length) {throw new IllegalArgumentException("Not a square matrix. ");};
    }

    // row reduction
    public static Rational[][] upperTriangle(Rational[][] rational) {
        int row = rational.length;
        int col = rational[0].length;
        for (int i = 0; i < row; i++) {
            Rational[] temp = rational[i];
            for (int j = i + 1; j < row; j++) {
                if (!(rational[i][i].equals(rezero())) && rational[j][i].equals(rezero())) {
                    continue;
                }
                else if (rational[i][i].equals(rezero()) && !(rational[j][i].equals(rezero()))) {
                    rational[i] = rational[j];
                    rational[j] = temp;
                    continue; // swap rows and no operation is needed
                }
                else {
                    Rational x = (new Rational(-1, 1).multiply(rational[j][i].divide(rational[i][i])));
                    for (int k = 0; k < col; k++) {
                        rational[j][k] = (x.multiply(rational[i][k]).add(rational[j][k]));

                    }
                }
            }
        }
        return rational;
    }

    public Rational[][] inverse(Rational[][] rational) {
         validate(rational);
        int row = rational.length;
        Rational[][] identityMatrix = new Rational[row][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                if (i == j) {
                    //initialize diagonals
                    identityMatrix[i][j] = new Rational(1, 1);
                } else identityMatrix[i][j] = new Rational(0, 1);
            }
        }

        System.out.println("Initialize diagonals");
        pprint(identityMatrix);

        // transform identity matrix to upper triangle using coefficient from rational
        for (int i = 0; i < row; i++) {
            Rational[] temp = rational[i];
            Rational[] identityMatrixTemp = identityMatrix[i];
            for (int j = i + 1; j < row; j++) {
                if (!(rational[i][i].equals(rezero())) && rational[j][i].equals(rezero())) {
                    continue;
                }
                // swap rows if pivot == 0 and no immediate rezero()s below it
                else if (rational[i][i].equals(rezero()) && !(rational[j][i].equals(rezero()))) {
                    rational[i] = rational[j];
                    rational[j] = temp;
                    // coordinate swaps with identity matrix
                    identityMatrix[i] = identityMatrix[j];
                    identityMatrix[j] = identityMatrixTemp;
                    continue; // swap rows and no operation is needed
                }
                else {
                    Rational x = (new Rational(-1, 1).multiply(rational[j][i].divide(rational[i][i])));
                    for (int k = 0; k < row; k++) {
                        rational[j][k] = (x.multiply(rational[i][k]).add(rational[j][k]));
                        identityMatrix[j][k] = (x.multiply(identityMatrix[i][k]).add(identityMatrix[j][k]));
                    }
                }
            }
        }
        System.out.println("\n --------identity matrix after transformation to upper Triangle--");
        pprint(identityMatrix);
        System.out.println("\n ----Rational after transformation to upper Triangle--");
        pprint(rational);
        System.out.println("\nLower triangle");

        for (int i = row - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (!(rational[i][i].equals(rezero())) && rational[j][i].equals(rezero())) {
                    continue;
                } // no need for swaps, matrix alread in row echelon form
                else {
                    Rational y = (new Rational(-1, 1).multiply(rational[j][i].divide(rational[i][i])));
                    for (int k = 0; k < row; k++) {
                        rational[j][k] = (y.multiply(rational[i][k]).add(rational[j][k]));
                        identityMatrix[j][k] = (y.multiply(identityMatrix[i][k]).add(identityMatrix[j][k]));
                    }
                }
            }
        }
        // normalize matrix : divide by each row of both the identity matrix and the rational matrix by diagonals
        for (int i = 0; i < row; i++) {
            Rational diagonal = rational[i][i];
            for (int j = 0; j < row; j++) {
                identityMatrix[i][j] = identityMatrix[i][j].divide(diagonal);
                rational[i][j] = rational[i][j].divide(diagonal);
            }
        }
        System.out.println("\n --------identity matrix after inversion--");
        pprint(identityMatrix); // identity matrix now inverse matrix.
        System.out.println("\n ----Rational after inversion--"); // rational should now be a identity matrix
        pprint(rational);
        return identityMatrix;
    }

    private static char solutionFlag(Rational[][] rational) {
        int m = rational.length - 1;
        int n = rational[0].length;
        // matrix is already in row echelon form, only check last row to determine solution set and flag
        if (rational[m][n - 2].equals(rezero()) && rational[m][n - 1].equals(rezero())) return 'm'; // multi solution extend with
        if (rational[m][n - 2].equals(rezero()) && !(rational[m][n - 1].equals(rezero()))) return 'n';
        return 'u'; // unique solution set
    }

    public static Rational[] backSubstitution(Rational[][] rational) {
        int m = rational.length;
        int n = rational[0].length - 1;

        Rational[] solution = new Rational[n];

        if(solutionFlag(rational) == 'u') {

            for (int i = m - 1; i >= 0; i--) {
                Rational constant = rational[i][n];
                for (int j = i + 1; j < n; j++) {
                    constant = constant.subtract(rational[i][j].multiply(solution[j]));
                }
                solution[i] = constant.divide(rational[i][i]);
            }
            return solution;
        }
        else if (solutionFlag(rational) == 'm') {
            System.out.println("Multiple solutions. ");
            System.exit(1);
        } else { // no soluion
            System.out.println("No solution : {}");
            System.exit(1);
        }
        return solution;
    }

    public static void gaussian(Rational[][] rational) {
        //System.out.println(Arrays.toString(backSubstitution(upperTriangle(rational))));
        Rational[] solution = backSubstitution(upperTriangle(rational));
        for (int i = 0; i < solution.length; i++) {
            System.out.println(solution[i].intValue());
        }
    }



    public static int rank(Rational[][] rational) {
        int rank = 0;
        int m = rational[0].length - 1;
        int n = rational.length;
        upperTriangle(rational);
        for (int i = 0; i < n; i++) {
            if(rational[i][m - 1].equals(rezero()) && rational[i][m].equals(rezero()) && rational[i][0].equals(rezero())) continue;
            rank++;
        }
        return rank;
    }

    public static void pprint(Rational[][] rational) {
        for (int i = 0; i < rational.length; i++) {
            System.out.println(Arrays.toString(rational[i]));
        }
    }


}
