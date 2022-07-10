package fine.fractals.formatter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Formatter {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss").withZone(ZoneId.of("CET"));

    public static String roundString(double d) {
        long multiplier = 100000000000000L;
        return String.format("%1$,.14f", Math.floor(d * multiplier) / multiplier).replaceAll(",", ".");
    }

    public static String now() {
        return LocalDateTime.now().format(dtf);
    }
}
