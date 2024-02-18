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
    public static void createCsvWithHeader(Path filePath, Class<?> clazz) throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        String header = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvHeader.class))
                .map(field -> field.getAnnotation(CsvHeader.class).headerName())
                .collect(Collectors.joining(","));

        Files.write(filePath, (header + "\n").getBytes(), StandardOpenOption.CREATE);

        System.out.println("successfully created csv file");
    }

    public static List<RecieveDetail> convertCsvToRecieveDetailList(Path filePath) throws IOException {
        List<String> headerline = Arrays.asList(Files.lines(filePath).findFirst().orElse("").split(","));

        try (Stream<String> lines = Files.lines(filePath)) {
            return Files.lines(filePath)
                    .skip(1)
                    .map(line -> line.split(","))
                    .map(values -> createRecieveDetailFromValues(headerline, values))
                    .collect(Collectors.toList());
        }
    }

    private static RecieveDetail createRecieveDetailFromValues(List<String> headerline, String[] values) {
        RecieveDetail recieveDetail = new RecieveDetail();

        for (int i = 0; i < headerline.size(); i++) {
            String key = headerline.get(i);
            String value = i < values.length ? values[i] : "";
            BiConsumer<RecieveDetail, String> setter = RecieveDetail.setterMap.get(key);

            if (!Objects.isNull(setter)) {
                setter.accept(recieveDetail, value);
            }
        }
        return recieveDetail;
    }

    public static void writeRecieveDetailsToCsv(List<RecievePlan> recievePlanList, Path outputPath)
            throws IOException {
        String headerline = String.join(",", RecievePlan.getterMap.keySet());

        List<String> dataLines = recievePlanList.stream()
                .map(recievePlan -> RecievePlan.getterMap.entrySet().stream()
                        .map(entry -> entry.getValue().apply(recievePlan))
                        .collect(Collectors.joining(",")))
                .collect(Collectors.toList());

        Files.write(outputPath, (headerline + "\n" + String.join("\n", dataLines)).getBytes());
    }
}
