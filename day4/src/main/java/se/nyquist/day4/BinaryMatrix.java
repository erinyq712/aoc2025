package se.nyquist.day4;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BinaryMatrix implements Iterable<BinaryMatrix.Position> {
    private final boolean[][] data;

    public BinaryMatrix(int rows, int cols) {
        this.data = new boolean[rows][cols];
    }

    public BinaryMatrix(BinaryMatrix matrix) {
        this.data = new boolean[matrix.data.length][matrix.data[0].length];
        IntStream.range(0,matrix.data[0].length).forEach(row ->
                IntStream.range(0,matrix.data.length).forEach(col -> data[row][col] = matrix.data[row][col]));
    }

    public boolean get(int row, int col) {
        return data[row][col];
    }

    public void set(int row, int col, boolean value) {
        data[row][col] = value;
    }

    @Override
    public String toString() {
        return IntStream.range(0,data[0].length).mapToObj(row ->
            IntStream.range(0,data.length).mapToObj(col -> data[row][col] ? "@" : ".").collect(Collectors.joining())
        ).collect(Collectors.joining("\n"));
    }

    public View getView(int xpos, int ypos, int width, int height) {
        return new View(xpos, ypos, width, height);
    }

    public View getNeighbourhood(int row, int col) {
        return getView(row-1, col-1, 3, 3);
    }

    public View getNeighbourhood(Position p) {
        return getNeighbourhood(p.row, p.col);
    }

    public boolean get(Position p) {
        return get(p.row, p.col);
    }

    public void set(Position p, boolean b) {
        set(p.row, p.col, b);
    }

    public static class IteratorImpl implements Iterator<Position> {
        private final BinaryMatrix matrix;
        private int row;
        private int col;

        public IteratorImpl(BinaryMatrix matrix) {
            this.row = 0;
            this.col = 0;
            this.matrix = matrix;
        }
        @Override
        public boolean hasNext() {
            return row < matrix.data.length;
        }

        @Override
        public Position next() {
            if (row == matrix.data.length) {
                throw new NoSuchElementException();
            } else {
                var result = new Position(row, col++);
                if (col == matrix.data[0].length) {
                    row++;
                    col = 0;
                }
                return result;
            }
        }
    }

    @Override
    public Iterator<Position> iterator() {
        return new IteratorImpl(this);
    }

    @Override
    public void forEach(Consumer<? super Position> action) {
        IntStream.range(0,data[0].length).forEach(row ->
                IntStream.range(0,data.length).forEach(col -> action.accept(new Position(row,col))));
    }

    @Override
    public Spliterator<Position> spliterator() {
        return Iterable.super.spliterator();
    }

    public record Position(int row, int col) {
    }

    public class View {
        private final int xpos;
        private final int ypos;
        private final int width;
        private final int height;

        public View(int xpos, int ypos, int width, int height) {
            this.xpos = xpos;
            this.ypos = ypos;
            this.width = width;
            this.height = height;
        }

        public boolean get(int row, int col) {
            return data[xpos+row][ypos+col];
        }

        /**
         * Counts occurrences of specified boolean value
         */
        public long count(boolean value) {
            return IntStream.range(xpos,xpos+width).mapToLong(row ->
                    IntStream.range(ypos,ypos+height).filter(col -> {
                        if (row >= 0 && row < data.length && col >= 0 && col < data[0].length) {
                          return data[row][col] == value;
                        } else {
                            return !value;
                        }
                    }).count()
            ).reduce(0L, Long::sum)-1;
        }
    }


}
