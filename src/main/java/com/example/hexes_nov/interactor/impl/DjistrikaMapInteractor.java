package com.example.hexes_nov.interactor.impl;

import com.example.hexes_nov.interactor.AbstractMapInteractor;
import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class DjistrikaMapInteractor extends AbstractMapInteractor {

    public DjistrikaMapInteractor(MapModel model) {
        super("PathfindingControls", model);
    }

    @Override
    public void bind(JPanel panel) {
        super.bind(panel);
        model.recalculateDjistrkaMap(model.getSelected());
        panel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final TileModel<TerrainData> polygonAt = model.getPolygonAt(e.getPoint());
        polygonAt.data.isPassable = !polygonAt.data.isPassable;
        model.getSelected().remove(polygonAt);
        model.recalculateDjistrkaMap(model.getSelected());
        panel.repaint();
    }
}
