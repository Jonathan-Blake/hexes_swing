package com.example.hexes_nov.view.layers;

public abstract class AbstractToggleableLayer implements ToggleableLayer {
    private final String label;
    private boolean setting;

    public AbstractToggleableLayer(String label, boolean defaultToggle) {
        this.setting = defaultToggle;
        this.label = label;
    }

    @Override
    public boolean isEnabled() {
        return setting;
    }

    @Override
    public void toggle() {
        this.setting = !setting;
    }

    @Override
    public String getLabel() {
        return this.label;
    }
}
