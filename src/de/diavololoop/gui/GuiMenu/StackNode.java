package de.diavololoop.gui.GuiMenu;

import de.diavololoop.gui.GraphicUserInterface;

import java.util.LinkedList;

/**
 * Created by Chloroplast on 05.07.2017.
 */
public class StackNode extends GuiNode{

    private LinkedList<GuiNode> child = new LinkedList<GuiNode>();

    /*
    @Override
    public void draw(GraphicUserInterface<?> handle){

        if(child != null){
           // child.draw(handle);
        }

    }

    public void setChild(GuiNode node){
        if(child != null){
            child.parent = null;
        }

        child = node;

        if(node != null){
            node.parent = this;
        }

        super.contentChanged();


    }

    @Override
    public void update(){

        if(child == null){
            return;
        }

        if(child.widthPref < this.width){
            child.width = child.widthPref;
        }else{
            child.width = this.width;
        }

        if(child.heightPref < this.height){
            child.height = child.heightPref;
        }else{
            child.height = this.height;
        }

        child.x = this.x;
        child.y = this.y;

        child.update();

    }
*/

}
