package com.example.hexes_nov.interactor.impl;

import com.example.hexes_nov.interactor.AbstractMapInteractor;
import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;

import java.awt.event.MouseEvent;

public class SelectionMapInteractor extends AbstractMapInteractor {
    public SelectionMapInteractor(MapModel model) {
        super("Selection", model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final TileModel<TerrainData> polygonAt = model.getPolygonAt(e.getPoint());
        if (!polygonAt.data.isPassable) {
            return;
        }
        if (model.getSelected().contains(polygonAt)) {
            model.getSelected().remove(polygonAt);
        } else {
            model.getSelected().add(polygonAt);
        }
        model.recalculateDjistrkaMap(model.getSelected());
        panel.repaint();
    }
}
