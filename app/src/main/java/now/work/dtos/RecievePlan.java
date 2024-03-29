package now.work.dtos;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import now.work.annotations.CsvHeader;
import now.work.enums.HeaderOrDetail;

public class RecievePlan implements Cloneable {
    public static final String headerPrintKey = "Print Key";
    public static final String headerPlanListNo = "Plan List No";
    public static final String headerItemCd = "Item CD";
    public static final String headerDesignCd = "Design CD";
    public static final String headerDateOfManufacture = "Date of Manufacture";
    public static final String headerStockData = "Stock Data";
    public static final String headerPageNo = "Page No";
    public static final String headerSortKey = "Sort Key";
    public static final String headerHeaderOrDetail = "Header Or Detail";
    public static final String headerOther = "Other";

    @CsvHeader(headerName = headerPrintKey)
    private int printKey;

    @CsvHeader(headerName = headerPlanListNo)
    private int planListNo;

    @CsvHeader(headerName = headerItemCd)
    private String itemCd;

    @CsvHeader(headerName = headerDesignCd)
    private String designCd;

    @CsvHeader(headerName = headerDateOfManufacture)
    private String dateOfManufacture;

    @CsvHeader(headerName = headerStockData)
    private String stockData;

    @CsvHeader(headerName = headerPageNo)
    private int pageNo;

    @CsvHeader(headerName = headerSortKey)
    private int sortKey;

    @CsvHeader(headerName = headerHeaderOrDetail)
    private HeaderOrDetail headerOrDetail;

    @CsvHeader(headerName = headerOther)
    private String other;

    public static final Map<String, Function<RecievePlan, String>> getterMap = new LinkedHashMap<>();

    static {
        getterMap.put(headerPrintKey, recievePlan -> String.valueOf(recievePlan.getPrintKey()));
        getterMap.put(headerPlanListNo, recievePlan -> String.valueOf(recievePlan.getPlanListNo()));
        getterMap.put(headerItemCd, RecievePlan::getItemCd);
        getterMap.put(headerDesignCd, RecievePlan::getDesignCd);
        getterMap.put(headerDateOfManufacture, RecievePlan::getDateOfManufacture);
        getterMap.put(headerStockData, RecievePlan::getStockData);
        getterMap.put(headerPageNo, recievePlan -> String.valueOf(recievePlan.getPageNo()));
        getterMap.put(headerSortKey, recievePlan -> String.valueOf(recievePlan.getSortKey()));
        getterMap.put(headerHeaderOrDetail, recievePlan -> recievePlan.getHeaderOrDetail().getValue());
        getterMap.put(headerOther, RecievePlan::getOther);
    }

    public RecievePlan(Builder builder) {
        this.printKey = builder.printKey;
        this.planListNo = builder.planListNo;
        this.itemCd = builder.itemCd;
        this.designCd = builder.designCd;
        this.dateOfManufacture = builder.dateOfManufacture;
        this.stockData = builder.stockData;
        this.pageNo = builder.pageNo;
        this.sortKey = builder.sortKey;
        this.headerOrDetail = builder.headerOrDetail;
        this.other = builder.other;
    }

    public RecievePlan(int printKey, int planListNo, String itemCd, String designCd, String dateOfManufacture,
            String stockData, int pageNo, int sortKey, HeaderOrDetail headerOrDetail, String other) {
        this.printKey = printKey;
        this.planListNo = planListNo;
        this.itemCd = itemCd;
        this.designCd = designCd;
        this.dateOfManufacture = dateOfManufacture;
        this.stockData = stockData;
        this.pageNo = pageNo;
        this.sortKey = sortKey;
        this.headerOrDetail = headerOrDetail;
        this.other = other;
    }

    @Override
    public RecievePlan clone() {
        try {
            return (RecievePlan) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public int getPrintKey() {
        return printKey;
    }

    public void setPrintKey(int printKey) {
        this.printKey = printKey;
    }

    public int getPlanListNo() {
        return planListNo;
    }

    public void setPlanListNo(int planListNo) {
        this.planListNo = planListNo;
    }

    public String getItemCd() {
        return itemCd;
    }

    public void setItemCd(String itemCd) {
        this.itemCd = itemCd;
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

    public String getStockData() {
        return stockData;
    }

    public void setStockData(String stockData) {
        this.stockData = stockData;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getSortKey() {
        return sortKey;
    }

    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }

    public HeaderOrDetail getHeaderOrDetail() {
        return headerOrDetail;
    }

    public void setHeaderOrDetail(HeaderOrDetail headerOrDetail) {
        this.headerOrDetail = headerOrDetail;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "RecievePlan [printKey=" + printKey + ", planListNo=" + planListNo + ", itemCd=" + itemCd + ", designCd="
                + designCd + ", dateOfManufacture=" + dateOfManufacture + ", stockData=" + stockData + ", pageNo="
                + pageNo + ", sortKey=" + sortKey + ", headerOrDetail=" + headerOrDetail + ", other=" + other + "]";
    }

    public static class Builder {
        private int printKey;
        private int planListNo;
        private String itemCd;
        private String designCd;
        private String dateOfManufacture;
        private String stockData;
        private int pageNo;
        private int sortKey;
        private HeaderOrDetail headerOrDetail;
        private String other;

        public Builder setPrintKey(int printKey) {
            this.printKey = printKey;
            return this;
        }

        public Builder setPlanListNo(int planListNo) {
            this.planListNo = planListNo;
            return this;
        }

        public Builder setItemCd(String itemCd) {
            this.itemCd = itemCd;
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

        public Builder setStockData(String stockData) {
            this.stockData = stockData;
            return this;
        }

        public Builder setPageNo(int pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public Builder setSortKey(int sortKey) {
            this.sortKey = sortKey;
            return this;
        }

        public Builder setHeaderOrDetail(HeaderOrDetail headerOrDetail) {
            this.headerOrDetail = headerOrDetail;
            return this;
        }

        public Builder setOther(String other) {
            this.other = other;
            return this;
        }

        public RecievePlan build() {
            return new RecievePlan(this);
        }
    }

}
