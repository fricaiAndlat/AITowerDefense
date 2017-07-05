package de.diavololoop.gui.GuiMenu;

import de.diavololoop.gui.GraphicUserInterface;
import de.diavololoop.gui.GuiImage;

/**
 * Created by Chloroplast on 05.07.2017.
 */
public class ImageNode extends GuiNode{

    private GuiImage image;

    public ImageNode(GuiImage image){
        this.image = image;
    }

    @Override
    public void draw(GraphicUserInterface<?> handle){
        handle.drawImage(super.x, super.y, super.width, super.height, image);
    }


}
