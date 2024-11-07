package com.example.hexes_nov.model;

import com.example.hexes_nov.model.geometry.coordinates.HexPoint;
import com.example.hexes_nov.model.geometry.coordinates.OffsetPoint;

public class TileModel<D> {
    public final MapModel parent;
    public final HexPoint coordinates;
    public D data;
//    public PixelPoint offsetPos;

    public TileModel(HexPoint coordinates, D data, MapModel parent) {
        this.coordinates = coordinates;
        this.parent = parent;
        this.data = data;
    }

    public OffsetPoint getOffset() {
        return OffsetPoint.qoffsetFromCube(parent.offset, coordinates);
    }
}
