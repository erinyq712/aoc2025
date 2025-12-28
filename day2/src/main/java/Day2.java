void main(String[] args) {
    var input = "input.txt";
    // input = "sample.txt";
    if (args.length > 0) {
        input = args[0];
    }
    try (var stream = this.getClass().getClassLoader().getResourceAsStream(input)) {
        if (stream != null) {
            new BufferedReader(new InputStreamReader(stream)).lines()
                    .findFirst()
                    .ifPresent(line -> {
                        exercise1(Arrays.stream(line.split(",")).toList());
                        exercise2(Arrays.stream(line.split(",")).toList());
                    });
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

private void exercise1(List<String> lines) {
    var invalidIds = lines.stream().flatMap(line -> {
        var range = line.split("-");
        var start = Long.parseLong(range[0]);
        var end = Long.parseLong(range[1]);
        return LongStream.range(start, end+1).mapToObj(id -> {
            // split string in equal parts
            var divider = 2;
            var str = String.valueOf(id);
            var mid = str.length() / divider;
            var firstHalf = str.substring(0, mid);
            var secondHalf = str.substring(mid);
            if (firstHalf.equals(secondHalf)) {
                return id;
            } else {
                return 0L;
            }
        }).filter(s -> s != 0L);
    }).toList();
    // invalidIds.forEach(System.out::println);
    var counter = invalidIds.stream().map(BigInteger::valueOf).reduce(BigInteger.ZERO, BigInteger::add);
    System.out.println(counter);
}

private void exercise2(List<String> lines) {
    var invalidIds = lines.stream().flatMapToLong(line -> {
        var range = line.split("-");
        var start = Long.parseLong(range[0]);
        var end = Long.parseLong(range[1]);
        return LongStream.range(start, end+1).flatMap(id -> {
            var str = String.valueOf(id);
            return IntStream.range(2,str.length()+1).mapToLong(divider -> {
                // split string in equal parts
                if (str.length() % divider == 0) {
                    var parts = new HashSet<String>();
                    int partLength = str.length() / divider;
                    for (int i = 0; i < str.length(); i += partLength) {
                        parts.add(str.substring(i, i + partLength));
                    }
                    if (parts.size() == 1) {
                        return id;
                    } else {
                        return 0L;
                    }
                }
                return 0L;
            });
        }).filter(s -> s != 0L);
    }).mapToObj(BigInteger::valueOf).distinct().toList();
    // invalidIds.forEach(System.out::println);
    var counter = invalidIds.stream().reduce(BigInteger.ZERO, BigInteger::add);
    System.out.println(counter);
}