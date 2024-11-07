package com.example.hexes_nov.utility;

import com.example.hexes_nov.model.geometry.coordinates.HexPoint;
import com.example.hexes_nov.model.geometry.coordinates.PixelPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PerlinNoise {
    static final Random R = new Random(100);
    static final List<Integer> PERMUTATION = makePermutation();

    public static List<Integer> makePermutation() {
        List<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            permutation.add(i);
        }

        Collections.shuffle(permutation, R);

        return permutation;
    }

    static PixelPoint getConstantVector(int v) {
        // v is the value from the permutation table
        int h = v & 3;
        if (h == 0)
            return new PixelPoint(1.0, 1.0);
        else if (h == 1)
            return new PixelPoint(-1.0, 1.0);
        else if (h == 2)
            return new PixelPoint(-1.0, -1.0);
        else
            return new PixelPoint(1.0, -1.0);
    }

    private static HexPoint getConstantHex(Integer permutation) {
        return HexPoint.directions.get(permutation % 6);
    }

    static double fade(double t) {
        return ((6 * t - 15) * t + 10) * t * t * t;
    }

    static double lerp(double t, double a1, double a2) {
        return a1 + t * (a2 - a1);
    }


    //Returns between -1 and 1
    public static double noise2D(double x, double y) {
        final int size = PERMUTATION.size();
        int xIndex = (int) Math.floor(x) & size - 1;
        int yIndex = (int) Math.floor(y) & size - 1;

        double xf = (x - Math.floor(x));
        double yf = (y - Math.floor(y));

        PixelPoint topRight = new PixelPoint(xf - 1.0, yf - 1.0);
        PixelPoint topLeft = new PixelPoint(xf, yf - 1.0);
        PixelPoint bottomRight = new PixelPoint(xf - 1.0, yf);
        PixelPoint bottomLeft = new PixelPoint(xf, yf);

        // Select a value from the permutation array for each of the 4 corners
        int valueTopRight = PERMUTATION.get((PERMUTATION.get((xIndex + 1) % size) + yIndex + 1) % size);
        int valueTopLeft = PERMUTATION.get((PERMUTATION.get(xIndex % size) + yIndex + 1) % size);
        int valueBottomRight = PERMUTATION.get((PERMUTATION.get((xIndex + 1) % size) + yIndex) % size);
        int valueBottomLeft = PERMUTATION.get((PERMUTATION.get(xIndex % size) + yIndex) % size);

        double dotTopRight = dot(topRight, getConstantVector(valueTopRight));
        double dotTopLeft = dot(topLeft, getConstantVector(valueTopLeft));
        double dotBottomRight = dot(bottomRight, getConstantVector(valueBottomRight));
        double dotBottomLeft = dot(bottomLeft, getConstantVector(valueBottomLeft));

        double u = fade(xf);
        double v = fade(yf);

        double lerp = lerp(u,
                lerp(v, dotBottomLeft, dotTopLeft),
                lerp(v, dotBottomRight, dotTopRight)
        );
//        lerp += 1;
//        lerp *= 122;
        return lerp;
    }

    private static double dot(PixelPoint a, PixelPoint b) {
        return a.x() * b.x() + a.y() * b.y();
    }

    private static double dot(HexPoint a, HexPoint b) {
        return a.q() * b.q() + a.s() * b.s() + a.r() * b.r();
    }

    public static double noise2D(double q, double r, double s) {
        final int size = PERMUTATION.size();
        int qIndex = (int) Math.floor(q) & size - 1;
        int rIndex = (int) Math.floor(r) & size - 1;
        int sIndex = (int) Math.floor(s) & size - 1;

        double qf = (q - Math.floor(q));
        double rf = (r - Math.floor(r));
        double sf = (s - Math.floor(s));

//        PixelPoint topRight = new PixelPoint(xf - 1.0, yf - 1.0);
//        PixelPoint topLeft = new PixelPoint(xf, yf - 1.0);
//        PixelPoint bottomRight = new PixelPoint(xf - 1.0, yf);
//        PixelPoint bottomLeft = new PixelPoint(xf, yf);
        List<HexPoint> corners = new HexPoint(qf, rf, sf).neighbors();

        // Select a value from the permutation array for each of the 6 corners
//        int valueTopRight = PERMUTATION.get((PERMUTATION.get((xIndex + 1) % size) + yIndex + 1) % size);
//        int valueTopLeft = PERMUTATION.get((PERMUTATION.get(xIndex % size) + yIndex + 1) % size);
//        int valueBottomRight = PERMUTATION.get((PERMUTATION.get((xIndex + 1) % size) + yIndex) % size);
//        int valueBottomLeft = PERMUTATION.get((PERMUTATION.get(xIndex % size) + yIndex) % size);
        List<Integer> permValues = new ArrayList<>(6);
        for (HexPoint hex : corners) {
            Integer integer = PERMUTATION.get((int) (
                    (PERMUTATION.get((int) ((hex.q() + 1) % size))
                            + hex.r() + 1) % size)
            );
            permValues.add(integer);
        }

//        double dotTopRight = dot(topRight, getConstantVector(valueTopRight));
//        double dotTopLeft = dot(topLeft, getConstantVector(valueTopLeft));
//        double dotBottomRight = dot(bottomRight, getConstantVector(valueBottomRight));
//        double dotBottomLeft = dot(bottomLeft, getConstantVector(valueBottomLeft));
        List<Double> dotValues = new ArrayList<>(6);
        for (int i = 0; i < corners.size(); i++) {
            HexPoint hex = corners.get(i);
            Integer permutation = permValues.get(i);
            dotValues.add(dot(hex, getConstantHex(permutation)));
        }


        double u = fade(qf);
        double z = fade(rf);
        double v = fade(sf);

        double lerp = lerp(u, z, v
//                lerp(v, dotBottomLeft, dotTopLeft),
//                lerp(v, dotBottomRight, dotTopRight)
        );
//        lerp += 1;
//        lerp *= 122;
        return lerp;
    }

}
