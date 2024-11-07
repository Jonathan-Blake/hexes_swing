package com.example.hexes_nov.view;

import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;
import com.example.hexes_nov.model.geometry.BB;
import com.example.hexes_nov.model.geometry.coordinates.PixelPoint;
import com.example.hexes_nov.view.layers.ToggleableLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.stream.Stream;

public class SwingHexMap extends JPanel {
    private final MapModel model;
    private final BB boundingBox;
//    private final Canvas canvas;

    public SwingHexMap(MapModel model) {
        boundingBox = new BB(PixelPoint.ORIGIN, new PixelPoint(500, 500));
        this.model = model;
        final Dimension size = new Dimension(500, 500);
        this.setMinimumSize(size);
        setSize(size);
        setPreferredSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        final List<ToggleableLayer> enabledLayers = model.drawingLayers.stream()
                .filter(ToggleableLayer::isEnabled)
                .toList();
        model.getView(this.boundingBox).forEach(tile -> drawTile(
                tile,
                g2,
                enabledLayers.stream()));
    }

    private void drawTile(TileModel<TerrainData> t, Graphics2D g, Stream<ToggleableLayer> toggleableLayerStream) {
//        Graphics g = this.canvas.getGraphics();
        Polygon p = tileToPolygon(t);
        toggleableLayerStream
                .forEach(layer -> layer.draw(g, model, t, p));

        g.setColor(Color.BLACK);
        g.drawPolygon(p);
    }

    private Polygon tileToPolygon(TileModel<TerrainData> t) {
        List<PixelPoint> corners = t.parent.layout.polygonCorners(t.coordinates);
        int[] x = new int[6];
        int[] y = new int[6];
        int bound = corners.size();
        for (int i = 0; i < bound; i++) {
            PixelPoint corner = corners.get(i).add(model.viewOffset);
            x[i] = (int) corner.x();
            y[i] = (int) corner.y();
        }

        return new Polygon(
                x, y, 6
        );
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        System.out.println("Listener " + l + " was added");
    }
}
