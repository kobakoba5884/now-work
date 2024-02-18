package now.work.utils.gen;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import now.work.dtos.RecieveDetail;

public class GenerateData {
    public static List<RecieveDetail> generateRecieveDetailList(int size) {
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
