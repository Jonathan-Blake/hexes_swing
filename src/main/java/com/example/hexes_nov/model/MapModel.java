package com.example.hexes_nov.model;

import com.example.hexes_nov.model.events.NewTileEvents;
import com.example.hexes_nov.model.geometry.BB;
import com.example.hexes_nov.model.geometry.Layout;
import com.example.hexes_nov.model.geometry.coordinates.HexPoint;
import com.example.hexes_nov.model.geometry.coordinates.Offset;
import com.example.hexes_nov.model.geometry.coordinates.PixelPoint;
import com.example.hexes_nov.view.layers.ToggleableLayer;
import com.example.hexes_nov.view.layers.impl.DjistrkaToggleLayer;
import com.example.hexes_nov.view.layers.impl.ImpassableLayer;
import com.example.hexes_nov.view.layers.impl.SelectedToggleableLayer;
import com.example.hexes_nov.view.layers.impl.TileColorLayer;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class MapModel {
    private static final Random RANDOM = new Random();
    public final Layout layout;
    public final Offset offset;
    private final Map<HexPoint, TileModel<TerrainData>> modelMap;
    private final List<TileModel<TerrainData>> selected;
    private final int maxZoom;
    private final double minZoom;
    public List<ToggleableLayer> drawingLayers;
    public PixelPoint viewOffset;
    private HashMap<HexPoint, Double> djistrkaMap;
    private double zoom;

    public MapModel(Layout layout, Offset offset) {
        this.layout = layout;
        this.offset = offset;
        this.drawingLayers = List.of(
                new TileColorLayer(true),
                new SelectedToggleableLayer(true),
                new ImpassableLayer(false),
                new DjistrkaToggleLayer(false)
        );
        modelMap = new HashMap<>();
        djistrkaMap = new HashMap<>();
        selected = new ArrayList<>();
        viewOffset = PixelPoint.ORIGIN;
        maxZoom = 5;
        minZoom = 0.5;
    }

    public List<TileModel<TerrainData>> getView(final BB region) {
        BB offsetRegion = region.subtract(viewOffset);
        final LinkedList<TileModel<TerrainData>> ret = new LinkedList<>();

        final Set<HexPoint> explored = new HashSet<>();
        final Queue<HexPoint> unexplored = new LinkedList<>(List.of(layout.pixelToHex(offsetRegion.min())));

        final List<HexPoint> expansionDirections = List.of(
                HexPoint.directions.get(0),
                HexPoint.directions.get(5),
                HexPoint.directions.get(4)
        );

        while (!unexplored.isEmpty()) {
            final HexPoint next = unexplored.poll();
            explored.add(next);
            if (unexplored.contains(next)) {
                continue;
            }
            if (layout.polygonCorners(next).stream().anyMatch(offsetRegion::contains)) {
                ret.add(getOrInit(next));
                for (HexPoint direction : expansionDirections) {
                    final HexPoint adjacent = next.add(direction);
                    if (!explored.contains(adjacent) && !unexplored.contains(adjacent)) {
                        unexplored.add(adjacent);
                    }
                }
            }
        }
//        System.out.println(Arrays.toString(ret.stream().map(r -> r.coordinates).toArray()));

//        System.out.println(ret.size()+" "+ (ret.stream().map(r -> r.coordinates).collect(Collectors.toSet())).size());
        return ret;
    }

    private TileModel<TerrainData> getOrInit(final HexPoint hexPoint) {
        return this.modelMap.computeIfAbsent(hexPoint.round(), this::init);
    }

    public Optional<TileModel<TerrainData>> get(HexPoint position) {
        return Optional.ofNullable(this.modelMap.get(position.round()));
    }

    private TileModel<TerrainData> init(final HexPoint hexPoint) {
        final TileModel<TerrainData> model = new TileModel<>(hexPoint, generateRandomData(hexPoint), this);
        NewTileEvents.get().post(model);
        return model;
    }

    public List<TileModel<TerrainData>> getSelected() {
        return selected;
    }

    public Map<HexPoint, Double> getDjistrkaMap() {
        return djistrkaMap;
    }

    private TerrainData generateRandomData(HexPoint hexPoint) {
        return TerrainData.generateTerrain(//hexPoint,
                layout.hexToPixelUnzoomed(hexPoint));
    }

    public TileModel<TerrainData> getPolygonAt(Point point) {
        return getPolygonAt(new PixelPoint(point.getX(), point.getY()));
    }

    public TileModel<TerrainData> getPolygonAt(PixelPoint point) {
        return getOrInit(layout.pixelToHex(point.subtract(viewOffset)));
    }

    public void recalculateDjistrkaMap(List<TileModel<TerrainData>> source) {
        this.djistrkaMap = new HashMap<>();
        Set<HexPoint> open = new HashSet<>();
        for (TileModel<TerrainData> sourceTile : source) {
            this.getDjistrkaMap().put(sourceTile.coordinates, sourceTile.data.isPassable ? 0.0 : Double.MAX_VALUE);
            for (HexPoint neighbour : sourceTile.coordinates.neighbors()) {
                final Optional<TileModel<TerrainData>> neighbourTile = get(neighbour);
                if (neighbourTile.isPresent()) {
                    if (neighbourTile.get().data.isPassable) {
                        open.add(neighbour);
                        this.getDjistrkaMap().merge(
                                neighbour,
                                sourceTile.data.movementCost,
                                Math::min
                        );
                    } else {
                        this.getDjistrkaMap().put(neighbour, Double.MAX_VALUE);
                    }
                }
            }
        }
        while (!open.isEmpty()) {
            HashSet<HexPoint> nextOpen = new HashSet<>();

            for (HexPoint openPoint : open) {
                final Optional<TileModel<TerrainData>> openTile = get(openPoint);
                final Double startingCost = getDjistrkaMap().get(openPoint);
                final TileModel<TerrainData> openTileModel = openTile.get();
                final double movementCost = openTileModel.data.movementCost + startingCost;
                for (HexPoint neighbour : openTileModel.coordinates.neighbors()) {
                    final Optional<TileModel<TerrainData>> neighbourTile = get(neighbour);
                    if (neighbourTile.isPresent()) {
                        if (neighbourTile.get().data.isPassable) {
//                            System.out.println(neighbour);
                            if (getDjistrkaMap().containsKey(neighbour)) {
                                final Double aDouble = getDjistrkaMap().get(neighbour);
//                                System.out.println(aDouble+" "+movementCost);
                                if (aDouble > movementCost) {
                                    this.getDjistrkaMap().put(
                                            neighbour,
                                            movementCost
                                    );
                                    nextOpen.add(neighbour);
                                }
                            } else {
                                this.getDjistrkaMap().put(
                                        neighbour,
                                        movementCost
                                );
                                nextOpen.add(neighbour);
                            }
                        } else {
                            this.getDjistrkaMap().put(neighbour, Double.MAX_VALUE);
                        }
                    }
                }
            }


            open = nextOpen;
        }
    }

    public boolean adjustZoom(int change) {
        double original = this.zoom;
        this.zoom = Math.max(minZoom, Math.min(maxZoom, this.zoom + change * 0.1));
        layout.setZoom(this.zoom);
        return this.zoom != original;
    }
}
