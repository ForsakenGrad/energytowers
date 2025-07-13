package grad.energytowers.common.enums;

public enum LatticeShape {
    CUBIC_1X1("Cubic 1x1", 1, 1),
    RECTANGULAR_2X1("Rectangular 2x1", 2, 1),
    RECTANGULAR_3X1("Rectangular 3x1", 3, 1),
    TRIANGULAR_1X1("Triangular 1x1", 1, 1),
    TRIANGULAR_2X1("Triangular 2x1", 2, 1),
    TRIANGULAR_3X1("Triangular 3x1", 3, 1),
    PYRAMIDAL_1X1("Pyramidal 1x1", 1, 1),
    PYRAMIDAL_2X1("Pyramidal 2x1", 2, 1),
    PYRAMIDAL_3X1("Pyramidal 3x1", 3, 1),
    TRUNCATED_RECTANGLE("Truncated Rectangle", 1, 1);

    private final String displayName;
    private final int width;
    private final int depth;

    LatticeShape(String displayName, int width, int depth) {
        this.displayName = displayName;
        this.width = width;
        this.depth = depth;
    }

    public String getDisplayName() { return displayName; }
    public int getWidth() { return width; }
    public int getDepth() { return depth; }
}
