package com.example.hexes_nov;

import com.example.hexes_nov.model.MapModel;
import com.example.hexes_nov.model.geometry.Layout;
import com.example.hexes_nov.model.geometry.coordinates.Offset;
import com.example.hexes_nov.model.geometry.coordinates.PixelPoint;
import com.example.hexes_nov.view.SwingHexMap;
import com.example.hexes_nov.view.SwingOptionsLayout;

import javax.swing.*;
import java.awt.*;

public class Mapplication {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
//        EventQueue queue = new EventQueue()
//        {
//            protected void dispatchEvent(AWTEvent event)
//            {
//                System.out.println(event);
//                super.dispatchEvent(event);
//            }
//        };
//
//        Toolkit.getDefaultToolkit().getSystemEventQueue().push(queue);
//        Toolkit.getDefaultToolkit().addAWTEventListener( new AWTEventListener()
//        {
//            public void eventDispatched(AWTEvent e)
//            {
//                System.out.println(e.getID());
//            }
//        }, AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK);
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
//        SpringLayout layout = new SpringLayout();
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Hex Map");
        frame.getContentPane().add(label, BorderLayout.PAGE_START);

        final MapModel model = new MapModel(new Layout(Layout.pointy, new PixelPoint(10, 10), PixelPoint.ORIGIN), Offset.EVEN);
        final SwingHexMap swingHexMap = new SwingHexMap(model);
        frame.getContentPane().add(swingHexMap, BorderLayout.CENTER);
        frame.getContentPane().add(new SwingOptionsLayout(model, swingHexMap), BorderLayout.EAST);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(Mapplication::createAndShowGUI);
    }
}
