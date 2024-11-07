package com.example.hexes_nov.view.layers.impl;

import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;
import com.example.hexes_nov.view.layers.AbstractToggleableLayer;

import java.awt.*;

public class ImpassableLayer extends AbstractToggleableLayer {
    public ImpassableLayer(boolean defaultToggle) {
        super("Show Impassable", defaultToggle);
    }

    @Override
    public void draw(Graphics g, MapModel model, TileModel<TerrainData> t, Polygon p) {
        if (!t.data.isPassable) {
            g.setColor(Color.BLACK);
            g.fillPolygon(p);
        }
    }
}
