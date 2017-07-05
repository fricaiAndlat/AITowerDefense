package de.diavololoop.gui.GuiMenu;

import de.diavololoop.gui.GraphicUserInterface;

/**
 * Created by Chloroplast on 05.07.2017.
 */
public class GuiNode {

    public double widthMin = 0;
    public double heightMin = 0;

    public double widthPref = Double.MAX_VALUE;
    public double heightPref = Double.MAX_VALUE;

    public double width = 0;
    public double height = 0;

    public double x = 0;
    public double y = 0;

    public GuiNode parent;

    public void draw(GraphicUserInterface<?> handle){

    }

    protected void contentChanged(){

        if(parent == null){
            update();
        }else{
            parent.contentChanged();
        }

    }

    public void update(){

    }


}
