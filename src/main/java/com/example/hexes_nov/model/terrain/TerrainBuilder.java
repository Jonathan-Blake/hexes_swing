package com.example.hexes_nov.model.terrain;

import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.utility.RandUtil;

import java.awt.*;
import java.util.EnumSet;

public class TerrainBuilder {

    private Altitude altitude;
    private Moisture moisture;
    private Temperature temperature;

    public TerrainBuilder withAltitude(Altitude altitude) {
        this.altitude = altitude;
        return this;
    }

    public TerrainBuilder withMoisture(Moisture moisture) {
        this.moisture = moisture;
        return this;
    }

    public TerrainBuilder withTemperature(Temperature temperature) {
        this.temperature = temperature;
        return this;
    }

    public TerrainData build() {
        Color color;
        boolean isPassable = true;
        boolean isOpaque = true;
        switch (getAltitude()) {
            case DEEP_OCEAN -> {
                isPassable = false;
                color = Color.BLUE;
            }
            case SHALLOW_OCEAN -> {
                isPassable = false;
                color = Color.CYAN.darker();
            }
            case FLAT -> {
                color = new Color(0, 175, 0);
            }
            case HILL -> {
                color = new Color(0, 125, 0);
            }
            case MOUNTAIN -> {
                color = Color.DARK_GRAY;
                isPassable = false;
                isOpaque = false;
                return new TerrainData(color, isPassable, isOpaque);
            }
            default -> throw new RuntimeException("Undefined Altitude");
        }
        float[] colorArray = new float[4];
        switch (getMoisture()) {
            case WET -> {
                colorArray = color.getRGBComponents(colorArray);
                color = new Color(colorArray[0], incrementRGBColor(1, colorArray), incrementRGBColor(2, colorArray));
            }
            case DRY -> {
                colorArray = color.getRGBComponents(colorArray);
                color = new Color(incrementRGBColor(0, colorArray), colorArray[1], colorArray[2]);
            }
        }
        switch (this.getTemperature()) {

            case HOT -> {
                colorArray = color.getRGBComponents(colorArray);
                color = new Color(incrementRGBColor(0, colorArray), colorArray[1], colorArray[2]).darker();
            }
            case TEMPERATURE -> {
                colorArray = color.getRGBComponents(colorArray);
                color = new Color(colorArray[0], incrementRGBColor(1, colorArray), colorArray[2]);

            }
            case COLD -> {
                color = color.brighter();
//                color = new Color(colorArray[0], colorArray[1], incrementRGBColor(2, colorArray));
            }
        }

        return new TerrainData(color, isPassable, isOpaque);
    }

    private float incrementRGBColor(int i, float[] colorArray) {
        return Math.min(colorArray[i] + 0.2f, 1);
    }

    public Altitude getAltitude() {
        if (altitude == null) {
            altitude = RandUtil.pick(EnumSet.allOf(Altitude.class));
        }
        return altitude;
    }

    public Moisture getMoisture() {
        if (moisture == null) {
            moisture = RandUtil.pick(EnumSet.allOf(Moisture.class));
        }
        return moisture;
    }

    public Temperature getTemperature() {
        if (temperature == null) {
            temperature = RandUtil.pick(EnumSet.allOf(Temperature.class));
        }
        return temperature;
    }
}
