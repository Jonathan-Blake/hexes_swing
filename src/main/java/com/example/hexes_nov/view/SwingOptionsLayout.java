package com.example.hexes_nov.view;

import com.example.hexes_nov.interactor.Interactors;
import com.example.hexes_nov.interactor.impl.ClickNDragInteractor;
import com.example.hexes_nov.interactor.impl.DjistrikaMapInteractor;
import com.example.hexes_nov.interactor.impl.SelectionMapInteractor;
import com.example.hexes_nov.model.MapModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class SwingOptionsLayout extends JPanel {
    private final List<Interactors> interactors;

    public SwingOptionsLayout(MapModel model, SwingHexMap mapComponent) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Label placeHolder = new Label("Options Placeholder");
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(placeHolder);

        model.drawingLayers.forEach(layer -> {
            JToggleButton toggle = new JToggleButton(layer.getLabel(), layer.isEnabled());
            toggle.addActionListener(event -> layer.toggle());
            add(toggle);
        });


        this.interactors = List.of(
                new ClickNDragInteractor(model),
                new SelectionMapInteractor(model),
                new DjistrikaMapInteractor(model));

//        buttonFlowContainer =
        ButtonGroup buttons = new ButtonGroup();
        this.interactors.forEach(interactor -> {
            final JRadioButton radioButton = new JRadioButton();
            final JLabel label = new JLabel(interactor.getLabel());
            label.setLabelFor(radioButton);
            radioButton.addActionListener(turnOn(interactor, mapComponent));
            add(radioButton);
            add(label);
            buttons.add(radioButton);
        });

        buttons.getElements().nextElement().doClick();
        this.add(Box.createVerticalGlue());
    }

    private ActionListener turnOn(Interactors interactor, SwingHexMap mapComponent) {
        return e -> {
            interactors.forEach(Interactors::unbind);
            interactor.bind(mapComponent);
        };
    }
}
