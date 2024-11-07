package com.example.hexes_nov.interactor;

import com.example.hexes_nov.model.MapModel;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class AbstractMapInteractor implements Interactors {
    protected final MapModel model;
    private final String LABEL;
    protected JPanel panel;

    public AbstractMapInteractor(String label, MapModel model) {
        LABEL = label;
        this.model = model;
    }

    public void bind(JPanel panel) {
        System.out.println("Bound interactor " + getLabel());
        this.panel = panel;
        this.panel.addMouseListener(this);
        this.panel.addMouseMotionListener(this);
    }

    public void unbind() {
        if (this.panel != null) {
            this.panel.removeMouseListener(this);
            this.panel.removeMouseMotionListener(this);
        }
        this.panel = null;
    }

    @Override
    public String getLabel() {
        return LABEL;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println("Click");
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        System.out.println("Press");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        System.out.println("Release");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        System.out.println("Drag");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        System.out.println("Move");
    }
}
