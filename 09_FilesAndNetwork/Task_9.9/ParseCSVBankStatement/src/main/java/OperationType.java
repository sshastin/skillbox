public enum OperationType {

    INCOME("income"),
    OUTCOME("outcome");

    private final String type;

    OperationType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}