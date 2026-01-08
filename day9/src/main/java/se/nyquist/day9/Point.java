package se.nyquist.day9;

public record Point(Long x, Long y) implements Comparable<Point> {
    public Point(String s) {
        var values = s.split(",");
        this(Long.parseLong(values[0]), Long.parseLong(values[1]));
    }

    @Override
    public int compareTo(Point o) {
        if (o == null) { return 1; }
        var xCompare = x.compareTo(o.x);
        if (xCompare == 0 ) {
            return y.compareTo(o.y);
        } else {
            return xCompare;
        }
    }
    
}
