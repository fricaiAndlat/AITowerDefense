package de.diavololoop.gui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by gast2 on 31.05.17.
 */
public interface GraphicUserInterface<I> {

    // I -> KeyCode


    public void addDrawTask(Consumer<GraphicUserInterface> handle);
    public void setSize(int w, int h);
    public void setResizable(boolean state);
    public void resizeToFull();
    public void setFullscreen(boolean state);

    public void setColor(double r, double g, double b);
    public void setColor(double r, double g, double b, double a);

    public void fillRect(double x, double y, double w, double h);
    public void drawRect(double x, double y, double w, double h);

    public void fillOval(double x, double y, double w, double h);
    public void drawOval(double x, double y, double w, double h);

    public void drawImage(double x, double y, GuiImage image);
    public void drawImage(double x, double y, double w, double h, GuiImage image);
    public void drawImage(double x, double y, double w, double h, double angle, GuiImage image);
    
    public void clear();
    
    public double getRatio();
    public void setViewPort(double x, double y, double w, double h);

    public void addSizeListener(BiConsumer<Double, Double> sizeListener);
    public void addMouseListener(MouseListener mouseListener);
    public void addKeyListener(KeyListener<I> keyListener);


}
