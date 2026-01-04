import static java.util.function.Predicate.not;

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
    int current = 0;
    var firstLine = lines.get(current);
    var startLocation = IntStream.range(0,firstLine.length()).filter(i -> firstLine.charAt(i) == 'S').toArray()[0];
    current++;
    var timelines = Map.of(startLocation,1L);
    var splitCounter = 0;
    while (current < lines.size()) {
        final var line = lines.get(current);
        final var currentTimelines = Map.copyOf(timelines);
        var splitterLocations = IntStream.range(0,line.length()).filter(i -> line.charAt(i) == '^').boxed().toList();
        if (! splitterLocations.isEmpty()) {
            var splitted = currentTimelines.entrySet().stream().filter(e -> splitterLocations.contains(e.getKey()))
                    .flatMap(t -> Stream.of(
                            Map.entry(t.getKey()-1,currentTimelines.get(t.getKey())),
                            Map.entry(t.getKey()+1,currentTimelines.get(t.getKey()))))
                    .toList();
            var nonSplitted = timelines.entrySet().stream().filter(e -> ! splitterLocations.contains(e.getKey()));
            timelines = Stream.concat(nonSplitted,splitted.stream())
                            .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));
        }
        current++;
    }
    System.out.println(timelines.values().stream().reduce(0L, Long::sum));
}

private void exercise1(List<String> lines) {
    int current = 0;
    var firstLine = lines.get(current);
    var startLocation = IntStream.range(0,firstLine.length()).filter(i -> firstLine.charAt(i) == 'S').toArray()[0];
    current++;
    var startLocations = List.of(startLocation);
    var splitCounter = 0;
    while (current < lines.size()) {
        final var line = lines.get(current);
        final List<Integer> newStartLocations = new ArrayList<>();
        var splitterLocations = IntStream.range(0,line.length()).filter(i -> line.charAt(i) == '^').boxed().toList();
        if (! splitterLocations.isEmpty()) {
            var matches = splitterLocations.stream().filter(startLocations::contains).toList();
            var nonMatches = startLocations.stream().filter(not(splitterLocations::contains));
            if (! matches.isEmpty()) {
                newStartLocations.addAll(matches.stream().flatMapToInt(i ->IntStream.of(i-1,i+1)).boxed().toList());
                splitCounter += matches.size();
            }
            startLocations = Stream.concat(nonMatches,newStartLocations.stream()).sorted().distinct().toList();
        }
        current++;
    }
    System.out.println(splitCounter);
}
