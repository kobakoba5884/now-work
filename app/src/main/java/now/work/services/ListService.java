package now.work.services;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
// import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import now.work.dtos.RecieveDetail;
import now.work.dtos.RecievePlan;
import now.work.enums.HeaderOrDetail;
import now.work.utils.format.OriginalFormatter;
import now.work.utils.io.OriginalCsv;

public class ListService {
    public static void main(String[] args) throws IOException {
        Path inputFilePath = Paths.get("data/input.csv");
        Path outputFilePath = Paths.get("data/output.csv");
        Path sourtedOutputFilePath = Paths.get("data/sorted-output.csv");
        Path OutputWithPageFilePath = Paths.get("data/output-with-page.csv");
        List<RecieveDetail> recieveDetailList = OriginalCsv.convertCsvToRecieveDetailList(inputFilePath);
        // get printKey
        int printKey = Math.abs(new Random().nextInt());

        AtomicInteger planListNo = new AtomicInteger(1);
        List<RecievePlan> recievePlanList = new ArrayList<>();

        // outer for statement
        recieveDetailList.forEach(recieveDetail -> {
            // filter and get recievePlan
            // Optional<RecievePlan> foundRecievePlan = recievePlanList.stream()
            // .filter(generatePredicate(recieveDetail))
            // .findFirst();

            List<RecievePlan> matchingRecievePlanList = recievePlanList.stream()
                    .filter(generatePredicate(recieveDetail))
                    .collect(Collectors.toList());

            if (!matchingRecievePlanList.isEmpty()) {
                matchingRecievePlanList.stream().forEach(matchingRecievePlan -> {
                    // formalizing
                    String formalizedDateOfManufacture = OriginalFormatter.formalizeDateOfManufacture(
                            recieveDetail.getDateOfManufacture());
                    String formalizedDesignCd = OriginalFormatter.formalizeDesignCd(recieveDetail.getDesignCd());

                    // concating
                    String editedDateOfManufacture = matchingRecievePlan.getDateOfManufacture()
                            .concat(formalizedDateOfManufacture);
                    String editedDesignCd = matchingRecievePlan.getDesignCd()
                            .concat(formalizedDesignCd);

                    matchingRecievePlan.setDateOfManufacture(editedDateOfManufacture);
                    matchingRecievePlan.setDesignCd(editedDesignCd);
                });
                // if (foundRecievePlan.isPresent()) { // editing
                // String editedDateOfManufacture =
                // foundRecievePlan.get().getDateOfManufacture()
                // .concat(formalizeDate(recieveDetail.getDateOfManufacture()));

                // foundRecievePlan.get().setDateOfManufacture(editedDateOfManufacture);
            } else { // adding
                String newItemCd = recieveDetail.getItemCd();
                String newDesignCd = OriginalFormatter.formalizeDesignCd(recieveDetail.getDesignCd());
                String newDateOfManufacture = OriginalFormatter
                        .formalizeDateOfManufacture(recieveDetail.getDateOfManufacture());
                int stockNum = recieveDetail.getStockNum();
                String newOther = recieveDetail.getOther();

                RecievePlan newRecievePlan = new RecievePlan.Builder()
                        .setPrintKey(printKey)
                        .setItemCd(newItemCd)
                        .setDesignCd(newDesignCd)
                        .setDateOfManufacture(newDateOfManufacture)
                        .setHeaderOrDetail(HeaderOrDetail.Header)
                        .setOther(newOther)
                        .build();

                if (stockNum != 0) { // add stock data
                    IntStream.rangeClosed(1, stockNum).forEach(i -> {
                        RecievePlan clonedRecievePlan = newRecievePlan.clone();
                        String formalizedStockData = OriginalFormatter.formalizeStockData(i);
                        HeaderOrDetail headerOrDetail = i == 1 ? HeaderOrDetail.HeaderAndDetail : HeaderOrDetail.Detail;

                        clonedRecievePlan.setPlanListNo(planListNo.getAndIncrement());
                        clonedRecievePlan.setStockData(formalizedStockData);
                        clonedRecievePlan.setHeaderOrDetail(headerOrDetail);

                        recievePlanList.add(clonedRecievePlan);
                    });
                } else { // add no stock data
                    newRecievePlan.setPlanListNo(planListNo.getAndIncrement());

                    recievePlanList.add(newRecievePlan);
                }
            }
        });

        OriginalCsv.writeRecieveDetailsToCsv(recievePlanList, outputFilePath);

        // sorting
        List<RecievePlan> sortedRecievePlanList = sortRecievePlans(recievePlanList);
        OriginalCsv.writeRecieveDetailsToCsv(sortedRecievePlanList, sourtedOutputFilePath);

        // paging
        pagingHandle(sortedRecievePlanList);
        OriginalCsv.writeRecieveDetailsToCsv(sortedRecievePlanList, OutputWithPageFilePath);
    }

    private static void pagingHandle(List<RecievePlan> recievePlanList) {
        int headerCount = 0;
        int detailCount = 0;
        int printPageNo = 1;
        int sortKey = 1;

        for (int i = 0; i < recievePlanList.size() - 1; i++) {
            RecievePlan current = recievePlanList.get(i);
            RecievePlan next = recievePlanList.get(i + 1);

            if (headerCount == 0) {
                headerCount = current.getDateOfManufacture().length() / 10;

                // current detail
                if (!Objects.isNull(current.getStockData())) {
                    detailCount = 1;
                }

                current.setPageNo(printPageNo);
                current.setSortKey(sortKey);
            }

            // next header
            if (!current.getItemCd().equals(next.getItemCd())) {
                headerCount = headerCount + (next.getDateOfManufacture().length() / 10);
                ++sortKey;
            }

            // next detail
            if (!Objects.isNull(next.getStockData())) {
                ++detailCount;
            }

            if (headerCount + detailCount > 5) {
                headerCount = 0;
                detailCount = 0;
                ++printPageNo;
                ++sortKey;
            }

            next.setPageNo(printPageNo);
            next.setSortKey(sortKey);
        }
    }

    private static List<RecievePlan> sortRecievePlans(List<RecievePlan> recievePlanList) {
        Comparator<RecievePlan> comparator = Comparator
                .comparing(RecievePlan::getItemCd)
                .thenComparing(RecievePlan::getDateOfManufacture)
                .thenComparing(RecievePlan::getDesignCd);

        return recievePlanList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private static Predicate<RecievePlan> generatePredicate(RecieveDetail recieveDetail) {
        Predicate<RecievePlan> matchesItemCd = recievePlan -> recievePlan.getItemCd().equals(recieveDetail.getItemCd());
        // Predicate<RecievePlan> matchesItemCd2 = recievePlan ->
        // recievePlan.getDesignCd().equals(recieveDetail.getDesignCd());

        return matchesItemCd;
    }
}
