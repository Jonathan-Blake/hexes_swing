package com.example.hexes_nov.model.geometry;

import com.example.hexes_nov.model.geometry.coordinates.PixelPoint;

public record BB(PixelPoint min,
                 PixelPoint dimensions) {
    public static BB centeredOn(PixelPoint middle, PixelPoint dimensions) {
        final PixelPoint halfWidth = dimensions.multiply(0.5);
        return new BB(middle.subtract(halfWidth), dimensions);
    }

    public PixelPoint max() {
        return min.add(dimensions);
    }


    public boolean contains(PixelPoint point) {
        PixelPoint max = max();
        return point.x() >= min.x() && point.x() <= max.x()
                && point.y() >= min.y() && point.y() <= max.y();
    }

    public BB add(PixelPoint shift) {
        return new BB(min.add(shift), dimensions);
    }

    public BB subtract(PixelPoint shift) {
        return new BB(min.subtract(shift), dimensions);
    }

    public PixelPoint centrePoint() {
        return min.add(dimensions.multiply(0.5));
    }
}
