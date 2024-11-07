package com.example.hexes_nov.view.layers;

import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;

import java.awt.*;

public interface ToggleableLayer {
    boolean isEnabled();

    void toggle();

    void draw(Graphics g, MapModel model, TileModel<TerrainData> t, Polygon p);

    String getLabel();
}
