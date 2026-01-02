package se.nyquist.day5;

public record Range(long minInclusive, long maxInclusive) implements Comparable<Range> {
    @Override
    public int compareTo(Range o) {
        if (o == null) { return 1;}
        if (minInclusive == o.minInclusive) {
            return Long.compare(maxInclusive, o.maxInclusive);
        } else if (minInclusive < o.minInclusive) {
            return -1;
        } else {
            return 1;
        }
    }

    public boolean contains(long value) {
        return value >= minInclusive && value <= maxInclusive;
    }

    public boolean overlaps(Range o) {
        if (o == null) {
            return false;
        }
        if (minInclusive <= o.minInclusive && maxInclusive >= o.minInclusive) {
            return true;
        } else return minInclusive >= o.minInclusive && minInclusive <= o.maxInclusive;
    }

    public Range merge(Range next) {
        return new Range(Math.min(minInclusive, next.minInclusive), Math.max(maxInclusive, next.maxInclusive));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Range r) {
            return compareTo(r) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(minInclusive) ^ Long.hashCode(maxInclusive);
    }

    @Override
    public String toString() {
        return String.format("%d-%d", minInclusive, maxInclusive);
    }

    public long getExtent() {
        return maxInclusive - minInclusive + 1;
    }
}
