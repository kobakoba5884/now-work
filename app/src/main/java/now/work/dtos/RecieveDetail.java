package now.work.dtos;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import now.work.annotations.CsvHeader;

public class RecieveDetail {
    public static final String headerRecieveKey = "Recieve Key";
    public static final String headerRecieveDetailKey = "Recieve Detail Key";
    public static final String headerItemCd = "Item CD";
    public static final String headerDesignCd = "Design CD";
    public static final String headerDateOfManufacture = "Date of Manufacture";
    public static final String headerOther = "Other";

    @CsvHeader(headerName = headerRecieveKey)
    private int recieveKey;

    @CsvHeader(headerName = headerRecieveDetailKey)
    private int recieveDetailKey;

    @CsvHeader(headerName = headerItemCd)
    private String itemCd;

    @CsvHeader(headerName = headerDesignCd)
    private String designCd;

    @CsvHeader(headerName = headerDateOfManufacture)
    private String dateOfManufacture;

    @CsvHeader(headerName = headerOther)
    private String other;

    public static final Map<String, BiConsumer<RecieveDetail, String>> setterMap = new HashMap<>();

    static {
        setterMap.put(headerRecieveKey, (detail, value) -> detail.setRecieveKey(Integer.parseInt(value)));
        setterMap.put(headerRecieveDetailKey, (detail, value) -> detail.setRecieveDetailKey(Integer.parseInt(value)));
        setterMap.put(headerItemCd, RecieveDetail::setItemCd);
        setterMap.put(headerDesignCd, RecieveDetail::setDesignCd);
        setterMap.put(headerDateOfManufacture, RecieveDetail::setDateOfManufacture);
        setterMap.put(headerOther, RecieveDetail::setOther);
    }

    public RecieveDetail() {

    }

    public RecieveDetail(Builder builder) {
        this.recieveKey = builder.recieveKey;
        this.recieveDetailKey = builder.recieveDetailKey;
        this.itemCd = builder.itemCd;
        this.designCd = builder.designCd;
        this.dateOfManufacture = builder.dateOfManufacture;
        this.other = builder.other;
    }

    public RecieveDetail(int recieveKey, int recieveDetailKey, String itemCd, String designCd, String dateOfManufacture,
            String other) {
        this.recieveKey = recieveKey;
        this.recieveDetailKey = recieveDetailKey;
        this.itemCd = itemCd;
        this.designCd = designCd;
        this.dateOfManufacture = dateOfManufacture;
        this.other = other;
    }

    public int getRecieveKey() {
        return recieveKey;
    }

    public void setRecieveKey(int recieveKey) {
        this.recieveKey = recieveKey;
    }

    public int getRecieveDetailKey() {
        return recieveDetailKey;
    }

    public void setRecieveDetailKey(int recieveDetailKey) {
        this.recieveDetailKey = recieveDetailKey;
    }

    public String getItemCd() {
        return itemCd;
    }

    public void setItemCd(String itemCd) {
        this.itemCd = itemCd;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getDesignCd() {
        return designCd;
    }

    public void setDesignCd(String designCd) {
        this.designCd = designCd;
    }

    public String getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(String dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    @Override
    public String toString() {
        return "RecieveDetail [recieveKey=" + recieveKey + ", recieveDetailKey=" + recieveDetailKey + ", itemCd="
                + itemCd + ", designCd=" + designCd + ", dateOfManufacture=" + dateOfManufacture + ", other=" + other
                + "]";
    }

    public static class Builder {
        private int recieveKey;
        private int recieveDetailKey;
        private String itemCd;
        private String designCd;
        private String dateOfManufacture;
        private String other;

        public Builder setRecieveKey(int recieveKey) {
            this.recieveKey = recieveKey;
            return this;
        }

        public Builder setRecieveDetailKey(int recieveDetailKey) {
            this.recieveDetailKey = recieveDetailKey;
            return this;
        }

        public Builder setItemCd(String itemCd) {
            this.itemCd = itemCd;
            return this;
        }

        public Builder setOther(String other) {
            this.other = other;
            return this;
        }

        public Builder setDesignCd(String designCd) {
            this.designCd = designCd;
            return this;
        }

        public Builder setDateOfManufacture(String dateOfManufacture) {
            this.dateOfManufacture = dateOfManufacture;
            return this;
        }

        public RecieveDetail build() {
            return new RecieveDetail(this);
        }
    }

}
