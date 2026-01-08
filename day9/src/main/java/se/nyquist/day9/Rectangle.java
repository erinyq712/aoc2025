package se.nyquist.day9;

public record Rectangle(Point topLeft, Point bottomRight) {
    public Long area() {
        return Math.abs(bottomRight().x()-topLeft().x()+1) * Math.abs(bottomRight().y()-topLeft().y()+1);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "topLeft=" + topLeft +
                ", bottomRight=" + bottomRight +
                '}';
    }
}
