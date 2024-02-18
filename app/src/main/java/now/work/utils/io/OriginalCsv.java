package now.work.utils.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import now.work.annotations.CsvHeader;
import now.work.dtos.RecieveDetail;
import now.work.dtos.RecievePlan;

public class OriginalCsv {
    private static final String separatorByOS = System.getProperty("line.separator");
    private static final String csvDelimiter = ",";

    public static void createCsvWithHeader(Path filePath, Class<?> clazz) throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        String header = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvHeader.class))
                .map(field -> field.getAnnotation(CsvHeader.class).headerName())
                .collect(Collectors.joining(csvDelimiter));

        Files.write(filePath, (header.concat(separatorByOS)).getBytes(), StandardOpenOption.CREATE);

        System.out.println("successfully created csv file");
    }

    public static List<RecieveDetail> convertCsvToRecieveDetailList(Path filePath)
            throws IOException {
        List<String> headerline = Arrays.asList(Files.lines(filePath).findFirst().orElse("").split(csvDelimiter));

        try (Stream<String> lines = Files.lines(filePath)) {
            return Files.lines(filePath)
                    .skip(1)
                    .map(line -> line.split(csvDelimiter))
                    .map(values -> createRecieveDetailFromValues(headerline, values))
                    .collect(Collectors.toList());
        }
    }

    private static RecieveDetail createRecieveDetailFromValues(List<String> headerline,
            String[] values) {
        RecieveDetail instance = new RecieveDetail();

        for (int i = 0; i < headerline.size(); i++) {
            String headerName = headerline.get(i);
            String value = i < values.length ? values[i] : "";
            BiConsumer<RecieveDetail, String> setter = RecieveDetail.setterMap.get(headerName);

            if (!Objects.isNull(setter)) {
                setter.accept(instance, value);
            }
        }
        return instance;
    }

    public static void writeRecieveDetailsToCsv(List<RecievePlan> recievePlanList, Path outputPath)
            throws IOException {
        String headerline = String.join(csvDelimiter, RecievePlan.getterMap.keySet());

        List<String> dataLines = recievePlanList.stream()
                .map(recievePlan -> RecievePlan.getterMap.entrySet().stream()
                        .map(entry -> entry.getValue().apply(recievePlan))
                        .collect(Collectors.joining(csvDelimiter)))
                .collect(Collectors.toList());

        Files.write(outputPath,
                (headerline.concat(separatorByOS).concat(String.join(separatorByOS, dataLines))).getBytes());
    }
}
