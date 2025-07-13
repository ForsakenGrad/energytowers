package grad.energytowers.common.enums;

public enum VoltageType {
    LOW(1000, "Small Insulator", 3),
    MEDIUM(5000, "Medium Insulator", 5),
    HIGH(10000, "Long Insulator", 8);

    private final int capacity;
    private final String displayName;
    private final int ceramicDiscs;

    VoltageType(int capacity, String displayName, int ceramicDiscs) {
        this.capacity = capacity;
        this.displayName = displayName;
        this.ceramicDiscs = ceramicDiscs;
    }

    public int getCapacity() { return capacity; }
    public String getDisplayName() { return displayName; }
    public int getCeramicDiscs() { return ceramicDiscs; }
}
