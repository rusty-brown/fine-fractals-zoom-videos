package fine.fractals.formatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Formatter {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss").withZone(ZoneId.of("CET"));
    private static final NumberFormat nf = new DecimalFormat("#0.000000000000000000");

    public static String roundString(double d) {
        return nf.format(d);
    }

    public static String now() {
        return LocalDateTime.now().format(dtf);
    }
}
