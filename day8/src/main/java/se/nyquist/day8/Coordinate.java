package se.nyquist.day8;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.stream.Stream;

public record Coordinate(BigInteger x, BigInteger y, BigInteger z) implements Comparable<Coordinate> {
    static final MathContext mc = MathContext.DECIMAL64;

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public Coordinate(Long x, Long y, Long z) {
        this(BigInteger.valueOf(x), BigInteger.valueOf(y), BigInteger.valueOf(z));
    }

    public Coordinate(String line) {
        var parts = Stream.of(line.split(",")).map(String::trim).map(Long::parseLong).toList();
        if (parts.size() != 3) {
            throw new IllegalArgumentException("Invalid coordinate: " + line);
        }
        this(BigInteger.valueOf(parts.getFirst()), BigInteger.valueOf(parts.get(1)), BigInteger.valueOf(parts.getLast()));
    }
    
    public BigDecimal length() {
        return new BigDecimal(x.pow(2).add(y.pow(2).add(z).pow(2))).sqrt(mc);
    }
    
    public Coordinate add(Coordinate other) {
        return new Coordinate(x.add(other.x), y.add(other.y), z.add(other.z));
    }

    public BigDecimal distance(Coordinate o) {        
        return new BigDecimal(x.subtract(o.x).pow(2).add((y.subtract(o.y).pow(2).add(z.subtract(o.z).pow(2))))).sqrt(mc);
    }

    public Coordinate subtract(Coordinate other) {
        return new Coordinate(x.subtract(other.x), y.subtract(other.y), z.subtract(other.z));
    }

    @Override
    public int compareTo(Coordinate o) {
        if (o == null) { return 1; }
        var xCompare = x.compareTo(o.x);
        if (xCompare == 0 ) {
            var yCompare = y.compareTo(o.y);
            if (yCompare == 0) {
                return z.compareTo(o.z);
            } else {
                return yCompare;
            }
        } else {
            return xCompare;
        }
    }
}
