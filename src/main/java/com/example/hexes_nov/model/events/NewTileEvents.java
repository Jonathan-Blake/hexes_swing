package com.example.hexes_nov.model.events;

import com.example.hexes_nov.model.TerrainData;
import com.example.hexes_nov.model.TileModel;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class NewTileEvents implements MapplicationEventChannel<TileModel<TerrainData>> {
    private static final NewTileEvents singleton = new NewTileEvents();
    private final Set<MapplicationEventListener<TileModel<TerrainData>>> register;

    private NewTileEvents() {
        register = Collections.newSetFromMap(new WeakHashMap<>());
    }

    public static NewTileEvents get() {
        return singleton;
    }

    @Override
    public boolean register(MapplicationEventListener<TileModel<TerrainData>> obj) {
        return this.register.add(obj);
    }

    @Override
    public boolean unregister(MapplicationEventListener<TileModel<TerrainData>> obj) {
        return this.register.remove(obj);
    }

    @Override
    public void post(TileModel<TerrainData> obj) {
        register.forEach(listener -> listener.handle(obj));
    }
}
