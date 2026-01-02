package se.nyquist.day5;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Input {
    final List<Long> values = new ArrayList<>();
    final List<Range> mergedRanges = new ArrayList<>();

    public Input(List<String> lines) {
        var regex = Pattern.compile("(\\d+)-(\\d+)");
        var ranges = lines.stream().map(l -> {
            var matcher = regex.matcher(l);
            if (matcher.matches()) {
                return new Range(Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2)));
            } else {
                if (!l.isEmpty()) {
                    values.add(Long.parseLong(l));
                }
                return null;
            }
        }).filter(Objects::nonNull).sorted().toList();
        int pos = 0;
        var current = ranges.get(pos);
        while (pos < ranges.size()-1) {
            var next = ranges.get(pos+1);
            if (current != null) {
                if (current.overlaps(next)) {
                    current = current.merge(next);
                } else {
                    mergedRanges.add(current);
                    current = next;
                }
            }
            pos++;
        }
        mergedRanges.add(current);
    }

    public List<Long> getInRange() {
        return values.stream().filter(v -> contains(mergedRanges,v)).toList();
    }

    private boolean contains(List<Range> ranges, Long v) {
        return ranges.stream().anyMatch(r -> r.contains(v));
    }

    public Long getExtent() {
        return mergedRanges.stream().map(Range::getExtent).reduce(0L, Long::sum);
    }
}
