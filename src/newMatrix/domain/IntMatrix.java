package newMatrix.domain;

import java.util.Arrays;

public class IntMatrix extends GenericMatrix<Integer> {


    @Override
    protected Integer add(Integer a, Integer b) {
        return  a + b;
    }

    @Override
    protected Integer subtract(Integer a, Integer b) {
        return a - b;
    }

    @Override
    protected Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    @Override
    protected Integer zero() {
        return 0;
    }

    @Override
    protected Integer divide(Integer numerator, Integer denominator) {
        return numerator / denominator;
    }

    @Override
    protected Integer signOfCofactor(int zerothRow, int i) {
        return ((zerothRow + 1 + i + 1) % 2 == 0)  ? 1 : -1;
    }

    @Override
    protected Integer[] wrapSolution(Number[] matrix) {
        Integer[] intMatrix = new Integer[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            intMatrix[i] = (Integer) matrix[i];
        }
        return intMatrix;
    }

    public static void pprint(Integer[][] integers) {
        for (int i = 0; i < integers.length; i++) {
            System.out.println(Arrays.toString(integers[i]));
        }
    }


}
