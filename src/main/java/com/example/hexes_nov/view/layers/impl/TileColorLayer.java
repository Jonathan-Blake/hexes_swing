package com.example.hexes_nov.view.layers.impl;

import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;
import com.example.hexes_nov.view.layers.AbstractToggleableLayer;

import java.awt.*;

public class TileColorLayer extends AbstractToggleableLayer {
    public TileColorLayer(boolean defaultToggle) {
        super("Tile Color", defaultToggle);
    }

    @Override
    public void draw(Graphics g, MapModel model, TileModel<TerrainData> t, Polygon p) {
        g.setColor(t.data.colour);
        g.fillPolygon(p);
    }
}
