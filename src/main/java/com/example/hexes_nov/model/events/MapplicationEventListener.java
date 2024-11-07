package com.example.hexes_nov.model.events;

public interface MapplicationEventListener<T> {
    void handle(T obj);
}
