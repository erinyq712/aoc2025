import se.nyquist.day5.Input;

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
    Input input = new Input(lines);
    var inRange = input.getInRange();
    System.out.println(inRange.size());
}

private void exercise2(List<String> lines) {
    Input input = new Input(lines);
    var extent = input.getExtent();
    System.out.println(extent);
}
