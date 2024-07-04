package newMatrix.domain;

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
    protected Integer[] wrapSolution(Number[] matrix) {
        Integer[] intMatrix = new Integer[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            intMatrix[i] = (Integer) matrix[i];
        }
        return intMatrix;
    }


}
