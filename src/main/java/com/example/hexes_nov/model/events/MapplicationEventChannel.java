package com.example.hexes_nov.model.events;

public interface MapplicationEventChannel<T> {
    //methods to register and unregister observers
    boolean register(MapplicationEventListener<T> obj);

    boolean unregister(MapplicationEventListener<T> obj);

    //method to get updates from subject
    void post(T obj);
}
