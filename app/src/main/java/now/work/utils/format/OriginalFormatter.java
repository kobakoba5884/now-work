package now.work.utils.format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class OriginalFormatter {
    public static final String preDateFormat = "yyyyMMdd";
    public static final String dateFormat = "yyyy/MM/dd";
    public static final int dateLength = dateFormat.length();
    public static final int designCdLength = 2;

    public static String formalizeDateOfManufacture(String dateOfManufacture) {
        return formalizeDate(dateOfManufacture).orElse(" ".repeat(dateLength));

    }

    public static String formalizeDesignCd(String designCd) {
        return designCd.isEmpty() ? " ".repeat(designCdLength) : designCd;
    }

    public static String formalizeStockData(int index) {
        String stockDataFormat = "stockData%d";
        return String.format(stockDataFormat, index);
    }

    private static Optional<String> formalizeDate(String dateOfManufacture) {
        if (dateOfManufacture.isEmpty()) {
            return Optional.empty();
        }

        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern(preDateFormat);

        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern(dateFormat);

        LocalDate date = LocalDate.parse(dateOfManufacture, originalFormat);

        return Optional.of(date.format(newFormat));
    }
}
