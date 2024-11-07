package com.example.hexes_nov.view.layers.impl;

import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;
import com.example.hexes_nov.model.events.MapplicationEventListener;
import com.example.hexes_nov.model.events.NewTileEvents;
import com.example.hexes_nov.model.geometry.coordinates.PixelPoint;
import com.example.hexes_nov.view.layers.AbstractToggleableLayer;

import java.awt.*;
import java.text.DecimalFormat;

public class DjistrkaToggleLayer extends AbstractToggleableLayer implements MapplicationEventListener<TileModel<TerrainData>> {
    public DjistrkaToggleLayer(boolean defaultToggle) {
        super("Show Distance Map", defaultToggle);
        NewTileEvents.get().register(this);
    }

    @Override
    public void draw(Graphics g, MapModel model, TileModel<TerrainData> t, Polygon p) {
        g.setColor(Color.BLACK);
        Double cost = model.getDjistrkaMap().get(t.coordinates);
        final PixelPoint centrePoint = t.parent.layout.hexToPixel(t.coordinates).add(model.viewOffset);
        if (cost == null || cost == Double.MAX_VALUE) {
            g.drawString("X", (int) centrePoint.x(), (int) centrePoint.y());
        } else {
            g.drawString(new DecimalFormat("#.0#").format(cost), (int) centrePoint.x(), (int) centrePoint.y());
        }
    }

    @Override
    public void handle(TileModel<TerrainData> obj) {
//        this.d
    }
}
