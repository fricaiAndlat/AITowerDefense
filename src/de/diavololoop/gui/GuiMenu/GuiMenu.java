package de.diavololoop.gui.GuiMenu;

import de.diavololoop.gui.GraphicUserInterface;

/**
 * Created by Chloroplast on 05.07.2017.
 */
public class GuiMenu {


    private boolean isActive = true;

    private double x;
    private double y;
    private double w;
    private double h;

    GuiPanel root = new GuiPanel();

    public GuiMenu(GraphicUserInterface<?> handle, double x, double y, double w, double h){
        handle.addDrawTask( this::draw );

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        root.width = w;
        root.height = h;

        root.x = x;
        root.y = y;

    }

    public GuiPanel getRoot(){
        return root;
    }

    private void draw(GraphicUserInterface<?> handle) {

        if(!isActive){
            return;
        }

        root.draw( handle );

    }


}
