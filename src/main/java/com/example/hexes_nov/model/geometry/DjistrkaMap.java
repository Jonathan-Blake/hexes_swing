package com.example.hexes_nov.model.geometry;

import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;
import com.example.hexes_nov.model.events.MapplicationEventListener;
import com.example.hexes_nov.model.geometry.coordinates.HexPoint;

import java.util.*;

public class DjistrkaMap implements MapplicationEventListener<TileModel<TerrainData>> {

    private final MapModel model;
    private HashMap<HexPoint, DjistrkaNode> djistrkaMap;

    public DjistrkaMap(MapModel model) {
        this.model = model;
    }

    public void recalculateDjistrkaMap(List<TileModel<TerrainData>> source) {
        this.djistrkaMap = new HashMap<>();
        Set<HexPoint> open = new HashSet<>();
        for (TileModel<TerrainData> sourceTile : source) {
            this.djistrkaMap.put(sourceTile.coordinates, sourceTile.data.isPassable ? DjistrkaNode.source(sourceTile) : DjistrkaNode.impassable(sourceTile));
            for (HexPoint neighbourHexCoord : sourceTile.coordinates.neighbors()) {
                final Optional<TileModel<TerrainData>> neighbourTile = model.get(neighbourHexCoord);
                if (neighbourTile.isPresent()) {
                    if (neighbourTile.get().data.isPassable) {
                        open.add(neighbourHexCoord);
                        this.djistrkaMap.merge(
                                neighbourHexCoord,
                                DjistrkaNode.from(neighbourTile.get(), djistrkaMap.get(sourceTile.coordinates)),
                                DjistrkaNode::compare
                        );
                    } else {
                        this.djistrkaMap.put(neighbourHexCoord, DjistrkaNode.impassable(neighbourTile.get()));
                    }
                }
            }
        }
        while (!open.isEmpty()) {
            HashSet<HexPoint> nextOpen = new HashSet<>();

            for (HexPoint openPoint : open) {
                final Optional<TileModel<TerrainData>> openTile = model.get(openPoint);
                final DjistrkaNode startingCost = djistrkaMap.get(openPoint);
                final TileModel<TerrainData> openTileModel = openTile.get();
                final double movementCost = openTileModel.data.movementCost + startingCost.cost;
                for (HexPoint neighbour : openTileModel.coordinates.neighbors()) {
                    final Optional<TileModel<TerrainData>> neighbourTile = model.get(neighbour);
                    if (neighbourTile.isPresent()) {
                        if (neighbourTile.get().data.isPassable) {
                            if (djistrkaMap.containsKey(neighbour)) {
                                final double aDouble = djistrkaMap.get(neighbour).cost;
                                if (aDouble > movementCost) {
                                    this.djistrkaMap.put(
                                            neighbour,
                                            startingCost
                                    );
                                    nextOpen.add(neighbour);
                                }
                            } else {
                                this.djistrkaMap.put(
                                        neighbour,
                                        startingCost
                                );
                                nextOpen.add(neighbour);
                            }
                        } else {
                            this.djistrkaMap.put(neighbour, DjistrkaNode.impassable(openTileModel));
                        }
                    }
                }
            }
            open = nextOpen;
        }
    }

    @Override
    public void handle(TileModel<TerrainData> obj) {
        List<HexPoint> neighbours = obj.coordinates
                .neighbors()
                .stream()
                .filter(point -> djistrkaMap.containsKey(point))
                .toList();
        neighbours.stream()
                .map(neighbour -> djistrkaMap.get(neighbour))
                .min(Comparator.comparingDouble(neighbour -> neighbour.cost))
                .ifPresent(lowestNeighbour -> {
                    final DjistrkaNode newNode = DjistrkaNode.from(obj, lowestNeighbour);
                    djistrkaMap.put(
                            obj.coordinates,
                            newNode);
                    neighbours.remove(lowestNeighbour.tile.coordinates);
                    neighbours.forEach(n ->
                            djistrkaMap.merge(
                                    n,
                                    DjistrkaNode.from(djistrkaMap.get(n).tile, newNode),
                                    DjistrkaNode::compare
                            )
                    );
                });
    }

    private static class DjistrkaNode {
        private final TileModel<TerrainData> tile;
        private final DjistrkaNode origin;
        private final double cost;

        public DjistrkaNode(TileModel<TerrainData> tile, DjistrkaNode origin) {
            this.tile = tile;
            this.origin = null;
            cost = origin.cost + origin.tile.data.movementCost;
        }

        public DjistrkaNode(TileModel<TerrainData> tile) {
            //Used for Sources or impassible tiles
            this.tile = tile;
            this.origin = null;
            if (tile.data.isPassable) {
                cost = 0;
            } else {
                cost = Double.MAX_VALUE;
            }
        }

        public static DjistrkaNode source(TileModel<TerrainData> sourceTile) {
            return new DjistrkaNode(sourceTile);
        }

        public static DjistrkaNode impassable(TileModel<TerrainData> terrainDataTileModel) {
            return new DjistrkaNode(terrainDataTileModel);
        }

        public static DjistrkaNode from(TileModel<TerrainData> tile, DjistrkaNode origin) {
            return new DjistrkaNode(tile, origin);
        }

        public static DjistrkaNode compare(DjistrkaNode o, DjistrkaNode n) {
            return (o.cost > n.cost) ? n : o;
        }

    }
}
