import se.nyquist.day4.BinaryMatrix;

void main(String[] args) {
    var input = "input.txt";
    // input = "sample.txt";
    if (args.length > 0) {
        input = args[0];
    }
    try (var stream = this.getClass().getClassLoader().getResourceAsStream(input)) {
        if (stream != null) {
            var lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();
            exercise1(lines);
            exercise2(lines);
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

private void exercise1(List<String> lines) {
    var matrix = getBinaryMatrix(lines);
    // matrix
    var counter = 0;
    for (BinaryMatrix.Position p : matrix) {
        if (matrix.get(p)) {
            var neighbourhood = matrix.getNeighbourhood(p);
            var count = neighbourhood.count(true);
            // System.out.printf("%s: %d%n", p, count);
            if (count < 4 && neighbourhood.get(1, 1)) {
                counter++;
            }
        }
    }
    System.out.println(counter);
}

private static BinaryMatrix getBinaryMatrix(List<String> lines) {
    int cols = lines.size();
    int rows = lines.getFirst().length();
    var matrix = new BinaryMatrix(rows, cols);
    IntStream.range(0,cols).forEach(row -> {
        var line = lines.get(row);
        var values = line.chars().boxed().toList();
        IntStream.range(0,cols).forEach(col -> {
            if ("@".equals("" + (char)values.get(col).intValue())) {
                matrix.set(row, col, true);
            }
        });
    });
    return matrix;
}

private void exercise2(List<String> lines) {
    var matrix = getBinaryMatrix(lines);
    var total = 0;
    // matrix
    var done = false;
    do {
        var counter = 0;
        var newMatrix = new BinaryMatrix(matrix);
        for (BinaryMatrix.Position p : matrix) {

            if (matrix.get(p)) {
                var neighbourhood = matrix.getNeighbourhood(p);
                var count = neighbourhood.count(true);
                // System.out.printf("%s: %d%n", p, count);
                if (count < 4 && neighbourhood.get(1, 1)) {
                    counter++;
                    newMatrix.set(p, false);
                }
            }
        }
        total += counter;
        done = counter == 0;
        matrix = newMatrix;
        // System.out.println(counter);
    } while(! done);
    System.out.println(total);
}
