package com.example.hexes_nov.interactor;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

public interface Interactors extends MouseInputListener {
    void bind(JPanel panel);

    void unbind();

    String getLabel();
}
