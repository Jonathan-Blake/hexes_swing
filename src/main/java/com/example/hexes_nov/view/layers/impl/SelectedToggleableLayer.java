package com.example.hexes_nov.view.layers.impl;

import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;
import com.example.hexes_nov.view.layers.AbstractToggleableLayer;

import java.awt.*;

public class SelectedToggleableLayer extends AbstractToggleableLayer {

    public SelectedToggleableLayer(boolean defaultToggle) {
        super("Show Selected Hexes", defaultToggle);
    }

    @Override
    public void draw(Graphics g, MapModel model, TileModel<TerrainData> t, Polygon p) {
        if (model.getSelected().contains(t)) {
            g.setColor(Color.RED);
            g.fillPolygon(p);
        }
    }
}
