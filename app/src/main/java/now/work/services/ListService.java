package now.work.services;

// import java.io.File;
// import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import now.work.annotations.CsvHeader;
import now.work.dtos.RecieveDetail;
import now.work.dtos.RecievePlan;

public class ListService {
    public static void main(String[] args) throws IOException {
        Path inputFilePath = Paths.get("data/input.csv");
        Path outputFilePath = Paths.get("data/output.csv");
        List<RecieveDetail> recieveDetailList = convertCsvToRecieveDetailList(inputFilePath);
        // get printKey
        int printKey = Math.abs(new Random().nextInt());

        AtomicInteger planListNo = new AtomicInteger(1);
        List<RecievePlan> recievePlanList = new ArrayList<>();

        // outer for statement
        recieveDetailList.forEach(recieveDetail -> {
            // filter and get recievePlan 
            Optional<RecievePlan> foundRecievePlan = recievePlanList.stream()
                    .filter(generatePredicate(recieveDetail))
                    .findFirst();

            if (foundRecievePlan.isPresent()) { // editing
                String updatedDateOfManufacture = foundRecievePlan.get().getDateOfManufacture()
                        .concat(recieveDetail.getDateOfManufacture());

                foundRecievePlan.get().setDateOfManufacture(updatedDateOfManufacture);
            } else { // adding
                String newItemCd = recieveDetail.getItemCd();
                String newDesignCd = recieveDetail.getDesignCd();
                String newDateOfManufacture = recieveDetail.getDateOfManufacture();
                String newOther = recieveDetail.getOther();

                RecievePlan newRecievePlan = new RecievePlan.Builder()
                        .setPrintKey(printKey)
                        .setPlanListNo(planListNo.getAndIncrement())
                        .setItemCd(newItemCd)
                        .setDesignCd(newDesignCd)
                        .setDateOfManufacture(newDateOfManufacture)
                        .setOther(newOther)
                        .build();

                recievePlanList.add(newRecievePlan);
            }
        });

        writeRecieveDetailsToCsv(recievePlanList, outputFilePath);
    }

    private static Predicate<RecievePlan> generatePredicate(RecieveDetail recieveDetail) {
        Predicate<RecievePlan> matchesItemCd = recievePlan -> recievePlan.getItemCd().equals(recieveDetail.getItemCd());
        Predicate<RecievePlan> matchesItemCd2 = recievePlan -> recievePlan.getDesignCd().equals(recieveDetail.getDesignCd());

        return matchesItemCd.and(matchesItemCd2);
    }

    private static void writeRecieveDetailsToCsv(List<RecievePlan> recievePlanList, Path outputPath)
            throws IOException {
        String headerline = String.join(",", RecievePlan.getterMap.keySet());

        List<String> dataLines = recievePlanList.stream()
                .map(recievePlan -> RecievePlan.getterMap.entrySet().stream()
                        .map(entry -> entry.getValue().apply(recievePlan))
                        .collect(Collectors.joining(",")))
                .collect(Collectors.toList());

        Files.write(outputPath, (headerline + "\n" + String.join("\n", dataLines)).getBytes());
    }

    private static List<RecieveDetail> convertCsvToRecieveDetailList(Path filePath) throws IOException {
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

    @SuppressWarnings("unused")
    private static void generateCsvWithHeader(Path filePath, Class<?> clazz) throws IOException {
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

    @SuppressWarnings("unused")
    private static List<RecieveDetail> generateRecieveDetailList(int size) {
        final int startInclusive = 1;
        final int recieveKey = 1;
        final String itemCdFormat = "itemCd%d";
        final String designCdFormat = "designCd%d";
        final String dateOfManufactureFormat = "dateOfManufacture%d";
        final String otherFormat = "other%d";

        List<RecieveDetail> generateList = IntStream.rangeClosed(startInclusive, size)
                .mapToObj(i -> {
                    return new RecieveDetail.Builder()
                            .setRecieveKey(recieveKey)
                            .setRecieveDetailKey(i)
                            .setItemCd(String.format(itemCdFormat, i))
                            .setDesignCd(String.format(designCdFormat, i))
                            .setDateOfManufacture(String.format(dateOfManufactureFormat, i))
                            .setOther(String.format(otherFormat, i))
                            .build();
                }).collect(Collectors.toList());

        generateList.addAll(IntStream.rangeClosed(startInclusive, size)
                .mapToObj(i -> {
                    return new RecieveDetail.Builder()
                            .setRecieveKey(recieveKey + 1)
                            .setRecieveDetailKey(i + size)
                            .setItemCd(String.format(itemCdFormat, i))
                            .setDesignCd(String.format(designCdFormat, i + size))
                            .setDateOfManufacture(String.format(dateOfManufactureFormat, i + size))
                            .setOther(String.format(otherFormat, i + size))
                            .build();
                })
                .collect(Collectors.toList()));

        return generateList;
    }

}
