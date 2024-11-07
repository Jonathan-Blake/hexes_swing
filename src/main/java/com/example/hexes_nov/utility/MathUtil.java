package com.example.hexes_nov.utility;

public class MathUtil {
    public static double clamp(double number, double min, double max) {
        return Math.max(min, Math.min(number, max));
    }
}
