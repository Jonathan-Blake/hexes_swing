package com.example.hexes_nov.model.geometry.coordinates;

public record OffsetPoint(double column, double row) {

    public static OffsetPoint qoffsetFromCube(Offset offset, HexPoint h) {
        double col = (int) h.q();
        double row = (int) (h.r() + (int) ((h.q() + (offset.direction) * ((int) h.q() & 1)) / 2));
        return new OffsetPoint(col, row);
    }

    public static HexPoint qoffsetToCube(Offset offset, OffsetPoint h) {
        int q = (int) h.column();
        int r = (int) (h.row - (int) ((h.column() + (offset.direction) * ((int) h.column() & 1)) / 2));
        int s = -(int) q - r;
        return new HexPoint(q, r, s);
    }

    @Override
    public String toString() {
        return "OffsetPoint{column=%s, row=%s}".formatted(column, row);
    }

    public OffsetPoint add(OffsetPoint other) {
        return new OffsetPoint(column + other.column(), row + other.row());
    }
}
