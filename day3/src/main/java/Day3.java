void main(String[] args) {
    var input = "input.txt";
    input = "sample.txt";
    if (args.length > 0) {
        input = args[0];
    }
    try (var stream = this.getClass().getClassLoader().getResourceAsStream(input)) {
        if (stream != null) {
            var lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();
            // exercise1(lines);
            exercise2(lines);
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

private void exercise1(List<String> lines) {
    var counter = lines.stream().map(line -> {
        int digits = 2;
        var result = getDigits(line, digits);
        var str = result.stream().map(s -> "" + (char)s.intValue()).reduce("", String::concat);
        return Long.parseLong(str);
    }).reduce(0L, Long::sum);
    // invalidIds.forEach(System.out::println);
    System.out.println(counter);
}

private void exercise11(List<String> lines) {
    var counter = lines.stream().map(line -> {
        var values = line.chars().boxed().toList();
        var ordered = line.chars().boxed().sorted(Comparator.reverseOrder()).toList();
        var max = ordered.getFirst();
        var maxPositions = getMaxPositions(values, max, 2);
        // Handles edge case when max positions are less than 2
        if (! maxPositions.isEmpty()) {
            int firstPosition = maxPositions.get(0);
            if (maxPositions.size() > 1) {
                int secondPosition = maxPositions.get(1);
                return Long.parseLong("" + (char) values.get(firstPosition).intValue() + (char) values.get(secondPosition).intValue());
            } else {
                var tail = line.substring(firstPosition+1).chars().boxed().toList();
                var secondOrdered = tail.stream().sorted(Comparator.reverseOrder()).toList();
                var secondMaxPositions = getMaxPositions(tail, secondOrdered.getFirst(), 1);
                int secondPosition = secondMaxPositions.getFirst();
                return Long.parseLong("" + (char) values.get(firstPosition).intValue() + (char) tail.get(secondPosition).intValue());
            }
        } else {
            // Handles edge case when max is last character
            var secondMaxPositions = getMaxPositions(values, ordered.get(1), 2);
            int firstPosition = secondMaxPositions.getFirst();
            var secondOrdered = line.substring(firstPosition).chars().boxed().sorted(Comparator.reverseOrder()).toList();
            var thirdMax = secondOrdered.getFirst();
            var tail = line.substring(firstPosition+1).chars().boxed().toList();
            var thirdMaxPositions = getMaxPositions(tail, thirdMax, 1);
            int secondPosition = thirdMaxPositions.getFirst();
            return Long.parseLong("" + (char) values.get(firstPosition).intValue() + (char) tail.get(secondPosition).intValue());
        }
    }).reduce(0L, Long::sum);
    // invalidIds.forEach(System.out::println);
    System.out.println(counter);
}

private static List<Integer> getMaxPositions(List<Integer> values, int max, int noOfDigits) {
    return IntStream.range(0, values.size()-noOfDigits+1)
            .filter(i -> Objects.equals(values.get(i), max)).boxed().toList();
}

private void exercise2(List<String> lines) {
    var counter = lines.stream().map(line -> {
        int digits = 12;
        var result = getDigits(line, digits);
        var str = result.stream().map(s -> "" + (char)s.intValue()).reduce("", String::concat);
        return Long.parseLong(str);
    }).reduce(0L, Long::sum);
    // invalidIds.forEach(System.out::println);
    System.out.println(counter);
}

private static ArrayList<Integer> getDigits(String line, int digits) {
    int lastpos = 0;
    var result = new ArrayList<Integer>();
    while (digits > 0) {
        var values = line.substring(lastpos).chars().boxed().toList();
        var ordered = values.stream().sorted(Comparator.reverseOrder()).toList();
        var max = ordered.getFirst();
        var maxPositions = getMaxPositions(values, max, digits);
        int current = 1;
        while (maxPositions.isEmpty() && current < ordered.size() ) {
            maxPositions = getMaxPositions(values, ordered.get(current), digits);
            current++;
        }
        var firstPosition = maxPositions.getFirst();
        result.add(values.get(firstPosition));
        lastpos += firstPosition + 1;
        digits--;
    }
    return result;
}