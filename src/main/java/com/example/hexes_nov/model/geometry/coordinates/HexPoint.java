package com.example.hexes_nov.model.geometry.coordinates;

import java.util.ArrayList;
import java.util.List;

public final class HexPoint {

    public static final List<HexPoint> directions = List.of(
            new HexPoint(1, 0, -1), new HexPoint(1, -1, 0), new HexPoint(0, -1, 1), new HexPoint(-1, 0, 1), new HexPoint(-1, 1, 0), new HexPoint(0, 1, -1)
    );
    private final double q;
    private final double r;
    private final double s;

    public HexPoint(double q, double r, double s) {
        if (Double.isNaN(q) || q == Double.NEGATIVE_INFINITY || q == Double.POSITIVE_INFINITY)
            throw new AssertionError();
        this.q = q;
        if (Double.isNaN(r) || r == Double.NEGATIVE_INFINITY || r == Double.POSITIVE_INFINITY)
            throw new AssertionError();
        this.r = r;
        if (Double.isNaN(s) || s == Double.NEGATIVE_INFINITY || s == Double.POSITIVE_INFINITY)
            throw new AssertionError();
        this.s = s;
    }

    public static List<HexPoint> hexLinedraw(HexPoint start, HexPoint end) {
        int N = start.distance(end);
        HexPoint a_nudge = new HexPoint(start.q + 1e-06, start.r + 1e-06, start.s - 2e-06);
        HexPoint b_nudge = new HexPoint(end.q + 1e-06, end.r + 1e-06, end.s - 2e-06);
        ArrayList<HexPoint> results = new ArrayList<>();
        double step = 1.0 / Math.max(N, 1);
        for (int i = 0; i <= N; i++) {
            results.add(a_nudge.hexLerp(b_nudge, step * i).round());
        }
        return results;
    }

    public HexPoint hexLerp(HexPoint b, double t) {
        return new HexPoint(q * (1.0 - t) + b.q * t, r * (1.0 - t) + b.r * t, s * (1.0 - t) + b.s * t);
    }

    public HexPoint round() {
        int qi = (int) (Math.round(q));
        int ri = (int) (Math.round(r));
        int si = (int) (Math.round(s));
        double qDiff = Math.abs(qi - q);
        double rDiff = Math.abs(ri - r);
        double sDiff = Math.abs(si - s);
        if (qDiff > rDiff && qDiff > sDiff) {
            qi = -ri - si;
        } else if (rDiff > sDiff) {
            ri = -qi - si;
        } else {
            si = -qi - ri;
        }
        return new HexPoint(qi, ri, si);
    }

    public HexPoint add(HexPoint b) {
        return new HexPoint(q + b.q, r + b.r, s + b.s);
    }

    public HexPoint subtract(HexPoint b) {
        return new HexPoint(q - b.q, r - b.r, s - b.s);
    }

    public HexPoint scale(int k) {
        return new HexPoint(q * k, r * k, s * k);
    }

    public HexPoint rotateLeft() {
        return new HexPoint(-s, -q, -r);
    }

    public HexPoint rotateRight() {
        return new HexPoint(-r, -s, -q);
    }

    public int length() {
        return (int) ((Math.abs(q) + Math.abs(r) + Math.abs(s)) / 2);
    }

    public int distance(HexPoint b) {
        return subtract(b).length();
    }

    public HexPoint neighbor(int i) {
        return add(directions.get(i));
    }

    public List<HexPoint> neighbors() {
        ArrayList<HexPoint> ret = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            ret.add(neighbor(i));
        }
        return ret;
    }

    public HexPoint floor() {
        int qi = (int) (Math.floor(q));
        int ri = (int) (Math.floor(r));
        int si = (int) (Math.floor(s));
        double qDiff = Math.abs(qi - q);
        double rDiff = Math.abs(ri - r);
        double sDiff = Math.abs(si - s);
        if (qDiff > rDiff && qDiff > sDiff) {
            qi = -ri - si;
        } else if (rDiff > sDiff) {
            ri = -qi - si;
        } else {
            si = -qi - ri;
        }
        return new HexPoint(qi, ri, si);
    }

    public HexPoint ceil() {
        int qi = (int) (Math.ceil(q));
        int ri = (int) (Math.ceil(r));
        int si = (int) (Math.ceil(s));
        double qDiff = Math.abs(qi - q);
        double rDiff = Math.abs(ri - r);
        double sDiff = Math.abs(si - s);
        if (qDiff > rDiff && qDiff > sDiff) {
            qi = -ri - si;
        } else if (rDiff > sDiff) {
            ri = -qi - si;
        } else {
            si = -qi - ri;
        }
        return new HexPoint(qi, ri, si);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HexPoint hexPoint = (HexPoint) o;

        if (q != hexPoint.q) return false;
        if (hexPoint.r != r) return false;
        return hexPoint.s == s;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(q);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(r);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(s);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public double q() {
        return q;
    }

    public double r() {
        return r;
    }

    public double s() {
        return s;
    }

    @Override
    public String toString() {
        return "HexPoint[" +
                "q=" + q + ", " +
                "r=" + r + ", " +
                "s=" + s + ']';
    }
}
