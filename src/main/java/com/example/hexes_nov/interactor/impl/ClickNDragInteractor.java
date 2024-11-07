package com.example.hexes_nov.interactor.impl;

import com.example.hexes_nov.interactor.AbstractMapInteractor;
import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.geometry.coordinates.PixelPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ClickNDragInteractor extends AbstractMapInteractor implements MouseWheelListener {
    private Point previousPosition;

    public ClickNDragInteractor(MapModel model) {
        super("Click and Drag", model);
        previousPosition = null;
    }

    @Override
    public void bind(JPanel panel) {
        super.bind(panel);
        panel.addMouseWheelListener(this);
    }

    @Override
    public void unbind() {
        if (this.panel != null) {
            panel.removeMouseWheelListener(this);
        }
        super.unbind();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        previousPosition = e.getLocationOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseClicked(e);
        previousPosition = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point currentPos = e.getLocationOnScreen();
        if (this.previousPosition == null) {
            this.previousPosition = e.getLocationOnScreen();
        } else {
            double dx = currentPos.getX() - previousPosition.getX();
            double dy = currentPos.getY() - previousPosition.getY();

            model.viewOffset = model.viewOffset.add(new PixelPoint(dx, dy));

            this.previousPosition = currentPos;
            this.panel.repaint();
        }
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
//        negative values if the mouse wheel was rotated up/away from the user, and positive values if the mouse wheel was rotated down/ towards the user
        if (model.adjustZoom(e.getWheelRotation())) {
            this.panel.repaint();
        }
    }
}
