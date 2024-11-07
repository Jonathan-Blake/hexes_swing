package com.example.hexes_nov.model.geometry.coordinates;

public enum Offset {
    EVEN(1), ODD(-1);

    public final int direction;

    Offset(int direction) {
        this.direction = direction;
    }
}
