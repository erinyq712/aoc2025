import se.nyquist.day8.Coordinate;
import se.nyquist.day8.Point;

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

private void exercise2(List<String> lines) {
    var coordinates = lines.stream().map(Coordinate::new).toList();
    var distances = IntStream.range(0, coordinates.size()).boxed().flatMap(i ->
                    IntStream.range(i+1, coordinates.size()).mapToObj(j ->
                            Map.entry(new Point(coordinates.get(i), coordinates.get(j)), coordinates.get(i).distance(coordinates.get(j)))))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    var pairs = distances.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .toList();
    var circuits = new HashMap<Coordinate, Integer>();
    IntStream.range(0, coordinates.size()).boxed().forEach(i -> circuits.put(coordinates.get(i), i));
    int counter = 0;
    final int maxCircuits = pairs.size();
    while (counter < maxCircuits && circuits.values().stream().distinct().count() > 1) {
        var currentPair = pairs.get(counter);
        var first = currentPair.getKey().a();
        var second = currentPair.getKey().b();
        var mergedCircuit = circuits.get(first);
        var replacedCircuit = circuits.get(second);
        if (! mergedCircuit.equals(replacedCircuit)) {
            circuits.keySet().stream()
                    .filter(c -> circuits.get(c).equals(replacedCircuit))
                    .forEach(c -> circuits.put(c,mergedCircuit));
        }
        counter++;
    }
    var lastPair = pairs.get(counter-1);
    var result = lastPair.getKey().a().x().multiply(lastPair.getKey().b().x());
    System.out.println(result.longValue());
}

private void exercise1(List<String> lines) {
    var coordinates = lines.stream().map(Coordinate::new).toList();
    var distances = IntStream.range(0, coordinates.size()).boxed().flatMap(i -> 
        IntStream.range(i+1, coordinates.size()).mapToObj(j ->
                Map.entry(new Point(coordinates.get(i), coordinates.get(j)), coordinates.get(i).distance(coordinates.get(j)))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    var pairs = distances.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .toList();
    var circuits = new HashMap<Coordinate, Integer>();
    IntStream.range(0, coordinates.size()).boxed().forEach(i -> circuits.put(coordinates.get(i), i));
    int counter = 0;
    final int maxCircuits = 1000;
    while (counter < maxCircuits) {
        var currentPair = pairs.get(counter);
        var first = currentPair.getKey().a();
        var second = currentPair.getKey().b();
        var mergedCircuit = circuits.get(first);
        var replacedCircuit = circuits.get(second);
        if (! mergedCircuit.equals(replacedCircuit)) {
            circuits.keySet().stream()
                    .filter(c -> circuits.get(c).equals(replacedCircuit))
                    .forEach(c -> circuits.put(c,mergedCircuit));
        }
        counter++;
    }
    var result = circuits.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));
    // result.entrySet().stream().sorted(Map.Entry.<Integer,Long>comparingByValue().reversed())
    //        .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));
    System.out.println(result.entrySet().stream().sorted(Map.Entry.<Integer,Long>comparingByValue().reversed())
            .limit(3).map(Map.Entry::getValue).reduce(1L, (x, y) -> x*y));
}
