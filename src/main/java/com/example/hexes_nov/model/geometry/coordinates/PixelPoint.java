package com.example.hexes_nov.model.geometry.coordinates;

public record PixelPoint(double x, double y) {
    public static final PixelPoint ORIGIN = new PixelPoint(0, 0);

    public PixelPoint add(PixelPoint other) {
        return new PixelPoint(x + other.x(), y + other.y());
    }

    public PixelPoint subtract(PixelPoint other) {
        return new PixelPoint(x - other.x(), y - other.y());
    }

    public PixelPoint multiply(double scalar) {
        return new PixelPoint(x * scalar, y * scalar);
    }
}
