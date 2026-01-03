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
    var operations = lines.get(lines.size() - 1).split("\\s+");
    var maps = IntStream.range(0,lines.size()-1).mapToObj(i -> {
        var values = lines.get(i).split("\\s+");
        return IntStream.range(0,values.length)
                .mapToObj(c -> Map.entry(c, Long.parseLong(values[c])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }).toList();
    var data = IntStream.range(0,operations.length).mapToObj(i ->
                    Map.entry(i, maps.stream().map(m -> m.get(i)).toList()))
            .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

    var result = IntStream.range(0,operations.length).mapToObj(i ->
                    switch(operations[i]) {
                        case "*" -> data.get(i).stream().mapToLong(l -> l.stream().reduce(1L, (a,b) -> a*b)).findFirst().orElse(0L);
                        case "+" -> data.get(i).stream().mapToLong(l -> l.stream().reduce(0L, Long::sum)).findFirst().orElse(0L);
                        default -> 0L;
                    })
            .reduce(0L, Long::sum);
    System.out.println(result);
}

private void exercise2(List<String> lines) {
    var fieldSizes = Pattern.compile("([*+])( *)(?=[*+]|$)");
    var operationsLine = lines.getLast();
    var sizes = new ArrayList<Integer>();
    var opMatcher = fieldSizes.matcher(operationsLine);
    var counter = 0;
    while (opMatcher.find()) {
        var numberOfSpaces = opMatcher.group(2).length() + 1;
        sizes.add(counter);
        counter += numberOfSpaces;
    }

    var numbers = IntStream.range(0, sizes.size()).mapToObj(i ->
            IntStream.range(0, lines.size() - 1).mapToObj(j -> i == sizes.size() - 1 ?
                    lines.get(j).substring(sizes.get(i)) :
                    lines.get(j).substring(sizes.get(i), sizes.get(i + 1) - 1)).toList()).toList();

    var weirdNumbers = IntStream.range(0, numbers.size())
            .mapToObj(i -> {
                var c = i == sizes.size()-1 ? operationsLine.length()-sizes.get(i)+1 : sizes.get(i+1) - sizes.get(i)-1;
                var numberList = numbers.get(i);
                return IntStream.range(1, c+1).mapToObj(p ->
                        IntStream.range(0, numberList.size()).mapToObj(v -> {
                    var str = numberList.get(v);
                    var digit = str.charAt(str.length() - p);
                    if (digit != ' ') {
                        return String.valueOf((char) digit);
                    } else {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.joining())).toList();
            }).toList();

    var operations = IntStream.range(0,sizes.size()).mapToObj(i ->
            lines.get(lines.size()-1).substring(sizes.get(i), sizes.get(i)+1)).toList();
    
    var result = IntStream.range(0,weirdNumbers.size()).mapToObj(i ->
            switch(operations.get(i)) {
                case "+" -> weirdNumbers.get(i).stream().mapToLong(Long::parseLong).reduce(0L, Long::sum);
                case "*" -> weirdNumbers.get(i).stream().mapToLong(Long::parseLong).reduce(1L, (a,b) -> a*b);                
                default -> 0L;
            }).reduce(0L, Long::sum);

    System.out.println(result);
}
