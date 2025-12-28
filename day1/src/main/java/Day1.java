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

enum Direction {
    LEFT("L"), RIGHT("R");
    private final String value;
    Direction(String value) {
        this.value = value;
    }
    static Direction get(String value) {
        for (Direction direction : Direction.values()) {
            if (direction.value.equals(value)) {
                return direction;
            }
        }
        throw new IllegalArgumentException("No direction found for " + value);
    }
}

private void exercise1(List<String> lines) {

    long start = 50;
    long counter = 0;
    for (String line : lines) {
        Direction direction = Direction.get(line.substring(0, 1));
        var change = Long.parseLong(line.substring(1));
        if (direction == Direction.LEFT) {
            var delta = (start - change) % 100;
            start = delta < 0 ? delta + 100 : delta;
        } else {
            start = (start + change) % 100;
        }
        if (start < 0) {
            throw new IllegalStateException("Invalid position: " + start);
        }
        if (start == 0) {
            counter++;
        }
    }
    System.out.println(counter);
}

private void exercise2(List<String> lines) {
    long start = 50;
    long counter = 0;
    long previous;
    for (String line : lines) {
        Direction direction = Direction.get(line.substring(0, 1));
        var change = Long.parseLong(line.substring(1));
        var rotations = change / 100; // effective change
        change = change - rotations * 100;
        counter += rotations;
        previous = start;
        if (direction == Direction.LEFT) {
            var delta = start - change;
            start = delta < 0 ? (delta + 100) % 100 : delta;
            if ((delta < 0 && previous != 0) || start == 0) {
                System.out.printf("%s: %d -> %d%n", line, previous, start);
                counter++;
            }
        } else {
            var delta = start + change;
            start = delta % 100;
            if (delta > 99 || start == 0) {
                System.out.printf("%s: %d -> %d%n", line, previous, start);
                counter++;
            }
        }
        if (start < 0 || start > 99) {
            throw new IllegalStateException("Invalid position: " + start);
        }
    }
    System.out.println(counter);
}