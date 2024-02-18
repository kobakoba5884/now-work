package now.work.utils.format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class OriginalFormatter {
    public static String formalizeDateOfManufacture(String dateOfManufacture) {
        return formalizeDate(dateOfManufacture)
                .orElse(" ".repeat(10));

    }

    public static String formalizeDesignCd(String designCd) {
        return designCd.isEmpty() ? " ".repeat(2) : designCd;
    }

    public static String formalizeStockData(int index) {
        String stockDataFormat = "stockData%d";
        return String.format(stockDataFormat, index);
    }

    private static Optional<String> formalizeDate(String dateOfManufacture) {
        if (dateOfManufacture.isEmpty()) {
            return Optional.empty();
        }

        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        LocalDate date = LocalDate.parse(dateOfManufacture, originalFormat);

        return Optional.of(date.format(newFormat));
    }
}
