package newMatrix.domain;

public class Rational extends Number implements Comparable<Rational>{

    private long numerator = 0;
    private long denominator = 1;

    public Rational() {
        this(0,1);
    }

    public Rational(long n, long d) {
        if (d != 0) {
            long gcd = gcd(n, d);
            /*Internally, a rational number is
            represented in its lowest terms and the numerator determines its sign (line 14).
            The denominator is always positive
             */
            this.numerator = (d > 0 ? 1 : -1) * n / gcd;
            this.denominator = Math.abs(d) /gcd;
        } else throw new ArithmeticException(" Denominator cannot be 0");
    }

    //find greatest common divisor to reduce rational to it's lowest/base form
    public static long gcd(long numerator, long denominator) {
       if ( numerator % denominator == 0) return denominator;
       else return gcd(  denominator, numerator % denominator);
    }

    public long getNumerator() {
        return numerator;
    }

    public long getDenominator() {
        return denominator;
    }

    public Rational add(Rational otherRational) {
        long n = numerator * otherRational.getDenominator() +
                denominator * otherRational.getNumerator();
        long d = denominator * otherRational.getDenominator();
        return new Rational(n, d);
    }

    public Rational subtract(Rational otherRational) {
        long n = numerator * otherRational.getDenominator() -
                denominator * otherRational.getNumerator();
        long d = denominator * otherRational.getDenominator();
        return new Rational(n, d);
    }

    public Rational multiply(Rational otherRational) {
        long n = numerator * otherRational.getNumerator();
        long d = denominator * otherRational.getDenominator();
        return new Rational(n, d);
    }

    public Rational divide(Rational otherRational) {
        long n = numerator * otherRational.getDenominator();
        long d = denominator * otherRational.getNumerator();
        return new Rational(n, d);
    }

    public String toString() {
        return (denominator == 1) ? numerator + " " : numerator + " / " + denominator;
    }

    public boolean equals(Object other) {
        if(other == this) {
            return true;
        }
        if(other instanceof Rational rational) {
            return this.subtract(rational).getNumerator() == 0;
        }
        return false;
    }

    @Override
    public int intValue() {
        return (int)doubleValue();
    }

    @Override
    public long longValue() {
        return (long)doubleValue();
    }

    @Override
    public float floatValue() {
        return (float)doubleValue();
    }

    @Override
    public double doubleValue() {
        return numerator * 1.0 / denominator;
    }

    /*A rational number consists of a numerator and a denominator.
    There are many equivalent rational numbers—for example,
    1/3 = 2/6 = 3/9 = 4/12.
    The numerator and the denominator of 1/3 have no common divisor except 1, so 1/3 is said to be in lowest terms.
     */
    @Override
    public int compareTo(Rational o) {
        if (this.subtract(o).getNumerator() > 0) {
            return 1;
        } else if (this.subtract(o).getNumerator() < 0) {
            return -1;
        } else return 0;
    }
}
