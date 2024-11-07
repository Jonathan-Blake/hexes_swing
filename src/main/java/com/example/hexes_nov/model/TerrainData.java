package com.example.hexes_nov.model;

import com.example.hexes_nov.model.geometry.coordinates.HexPoint;
import com.example.hexes_nov.model.geometry.coordinates.PixelPoint;
import com.example.hexes_nov.model.terrain.Altitude;
import com.example.hexes_nov.model.terrain.TerrainBuilder;
import com.example.hexes_nov.utility.MathUtil;
import com.example.hexes_nov.utility.PerlinNoise;

import java.awt.*;
import java.util.Random;

public class TerrainData extends PathfindingData {
    private static final TerrainData[][] terrainLookup = new TerrainData[][]{
            new TerrainData[]{new TerrainData(Color.BLUE, false, false)},
            new TerrainData[]{new TerrainData(Color.cyan, true, true), new TerrainData(Color.GREEN, true, true), new TerrainData(Color.YELLOW, true, true)},
            new TerrainData[]{new TerrainData(Color.BLACK, false, true)}
    };
    private static final Random RANDOM = new Random();
    public Color colour;

    public TerrainData(Color colour, boolean isPassable, boolean isOpaque) {
        this.colour = colour;
        this.isPassable = isPassable;
        this.isOpaque = isOpaque;
        this.movementCost = 1;
    }

    public static TerrainData generateTerrain(HexPoint h) {

//        final TerrainData terrainData = new TerrainData();
//        terrainData.isOpaque = false;
//        final int i = RANDOM.nextInt(5);
//        terrainData.isPassable = i != 4;
//        terrainData.movementCost = terrainData.isPassable ? RANDOM.nextDouble(2) : -1;
////        double averageSize = (layout.size.x()+layout.size.y())/2;
//        double averageSize = 0.25;
//        final int r = (int) getRGBShade(averageSize, h.q(), h.s());
//        final int g = (int) getRGBShade(averageSize, h.s(), h.r());
//        final int b = (int) getRGBShade(averageSize, h.r(), h.q());
//        Orientation M = Layout.pointy;
//        double x = (M.f0 * h.q() + M.f1 * h.r()) ;
//        double y = (M.f2 * h.q() + M.f3 * h.r()) ;
////        final int r = (int) getRGBShade(averageSize, x, y);
//        final int c = (int) getRGBShade(averageSize, y, h.s());
//        final int yellow = (int) getRGBShade(averageSize, y, h.r());
//        final int w = (int) getRGBShade(averageSize, y, h.q());
////        System.out.printf("{r %s, g=%s, b=%s}%n", r, g, b);
//        SortedSet<Tuple<Integer, Color>> sortedList = new TreeSet<>(Comparator.comparingInt(Tuple::a));
//        sortedList.addAll(Arrays.asList(
////                new Tuple<>((int) getRGBShade(averageSize, x, y), Color.BLACK),
////        new Tuple<>((int) getRGBShade(averageSize, y, x), Color.WHITE)
//                new Tuple<>(r, Color.RED),
//                new Tuple<>(g, Color.GREEN),
//                new Tuple<>(b, Color.BLUE),
//                new Tuple<>(c, Color.CYAN),
//                new Tuple<>(yellow, Color.YELLOW),
//                new Tuple<>(w, Color.WHITE)
//        ));
//        int hexColor = (int) getRGBShade(averageSize, h.q(), h.r(), h.s());
//        terrainData.colour = sortedList.first().b();
////                new Color(
////                        hexColor,
////                        hexColor,
////                        hexColor
////////                r,
//////                g,
////////                b
////////                ,
//////                g,g
////        );
        double averageSize = 0.25;
        double height = (((PerlinNoise.noise2D(h.q() * averageSize * 0.121, h.r() * averageSize * 0.345)) + 1) / 2) * 3;
        System.out.println(height + "  " + (int) Math.floor(height) + "  " + terrainLookup[(int) Math.floor(height)]);
        final TerrainData[] altitudeBiomes = terrainLookup[(int) Math.floor(height)];
        if (altitudeBiomes.length == 1) {
            return altitudeBiomes[0];
        }
        int climate = (int) Math.round(PerlinNoise.noise2D((h.r() + h.q()) * averageSize * 0.121, (h.s() + h.q()) * averageSize * 0.217) + 1);
        return altitudeBiomes[climate];
    }

    public static TerrainData generateTerrain(PixelPoint hexToPixel) {
        final double averageSize = 0.05;
        final int height = (int) MathUtil.clamp((getCubeNoise(averageSize, hexToPixel.x(), hexToPixel.y()) * 256) + 122, 0, 255);
        System.out.println(height);
        TerrainBuilder b = new TerrainBuilder();
        if (height < 115) {
            b.withAltitude(Altitude.DEEP_OCEAN);
//            return terrainLookup[0][0];
        } else if (height > 129) {
            b.withAltitude(Altitude.MOUNTAIN);
//            return terrainLookup[2][0];
        } else if (height > 126) {
            b.withAltitude(Altitude.HILL);
//            return terrainLookup[1][0];
        } else {
            b.withAltitude(Altitude.FLAT);
//            return terrainLookup[1][(int) Math.floor(getZeroTo(0.005, hexToPixel.y(), hexToPixel.x(), 3))];
        }

//        return new TerrainData(
//                new Color(height, height, height),
//                true, true
//        );
        return b.build();
    }

    private static double getRGBShade(double averageSize, double q, double r, double s) {
        return (PerlinNoise.noise2D(q * averageSize * 0.121, r * averageSize * 0.345, s * 0.271) + 1) * 122;
    }

    private static double getRGBShade(double averageSize, double x, double y) {
        return (PerlinNoise.noise2D(x * averageSize * 0.121, y * averageSize * 0.345) + 1) * 122;
    }

    private static double getCubeNoise(double averageSize, double x, double y) {
        return Math.pow(PerlinNoise.noise2D(x * averageSize * 0.121, y * averageSize * 0.345), 3);
    }


    private static double getZeroTo(double averageSize, double x, double y, double maxExclusive) {
        return (PerlinNoise.noise2D(x * averageSize, y * averageSize) + 1) * (maxExclusive / 2);
    }
}
