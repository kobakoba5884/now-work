package now.work.enums;

public enum HeaderOrDetail {
    Header("header"),
    Detail("detail"),
    HeaderAndDetail("header/detail");

    private String value;

    HeaderOrDetail(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
