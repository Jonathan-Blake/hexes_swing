package com.example.hexes_nov.model.geometry;

import com.example.hexes_nov.model.geometry.coordinates.HexPoint;
import com.example.hexes_nov.model.geometry.coordinates.PixelPoint;

import java.util.ArrayList;
import java.util.List;

public class Layout {
    public static Orientation pointy = new Orientation(Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0, 3.0 / 2.0, Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0, 0.5);
    public static Orientation flat = new Orientation(3.0 / 2.0, 0.0, Math.sqrt(3.0) / 2.0, Math.sqrt(3.0), 2.0 / 3.0, 0.0, -1.0 / 3.0, Math.sqrt(3.0) / 3.0, 0.0);
    public final Orientation orientation;
    public final PixelPoint size;
    public final PixelPoint origin;
    private double zoom;

    public Layout(Orientation orientation, PixelPoint size, PixelPoint origin) {
        this.orientation = orientation;
        this.size = size;
        this.origin = origin;
        this.setZoom(1);
    }

    public PixelPoint hexToPixel(HexPoint h) {
        Orientation M = orientation;
        double x = (M.f0 * h.q() + M.f1 * h.r()) * getZoomedSize().x();
        double y = (M.f2 * h.q() + M.f3 * h.r()) * getZoomedSize().y();
        return new PixelPoint(x + origin.x(), y + origin.y());
    }

    public HexPoint pixelToHex(PixelPoint p) {
        Orientation M = orientation;
        PixelPoint pt = new PixelPoint((p.x() - origin.x()) / getZoomedSize().x(), (p.y() - origin.y()) / getZoomedSize().y());
        double q = M.b0 * pt.x() + M.b1 * pt.y();
        double r = M.b2 * pt.x() + M.b3 * pt.y();
        return new HexPoint(q, r, -q - r);
    }


    public PixelPoint hexCornerOffset(int corner) {
        double angle = 2.0 * Math.PI * (orientation.start_angle - corner) / 6.0;
        return new PixelPoint(getZoomedSize().x() * Math.cos(angle), getZoomedSize().y() * Math.sin(angle));
    }

    private PixelPoint getZoomedSize() {
        return size.multiply(getZoom());
    }


    public List<PixelPoint> polygonCorners(HexPoint h) {
        ArrayList<PixelPoint> corners = new ArrayList<>();
        PixelPoint center = hexToPixel(h);
        for (int i = 0; i < 6; i++) {
            PixelPoint offset = hexCornerOffset(i);
            corners.add(new PixelPoint(center.x() + offset.x(), center.y() + offset.y()));
        }
        return corners;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public PixelPoint hexToPixelUnzoomed(HexPoint h) {
        Orientation M = orientation;
        double x = (M.f0 * h.q() + M.f1 * h.r()) * size.x();
        double y = (M.f2 * h.q() + M.f3 * h.r()) * size.y();
        return new PixelPoint(x + origin.x(), y + origin.y());
    }
}
