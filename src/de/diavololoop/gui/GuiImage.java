package de.diavololoop.gui;

import de.diavololoop.gui.fxgui.FXGui;
import de.diavololoop.gui.glgui.GLTexture;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Chloroplast on 01.06.2017.
 */
public class GuiImage {

    public final static GuiImage EMPTY = new GuiImage(null, null);

    private static enum Infrastructure {OPENGL, JFX}
    private final static Infrastructure CURRENT = Infrastructure.JFX;

    public static GuiImage create(InputStream in){
        switch (CURRENT){
            case OPENGL:
                try {
                    BufferedImage image = ImageIO.read(in);
                    return new GuiImage(  new GLTexture(image) , image.getWidth(), image.getHeight() );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case JFX:
                Image image = FXGui.loadImage(in);
                return new GuiImage( image, (int)image.getWidth(), (int)image.getHeight() );
        }

        return null;
    }

    private final Image imageFX;
    private final GLTexture imageGL;

    private final int width;
    private final int height;

    public GuiImage(Image imageFX, GLTexture imageGL){
        this.imageFX = imageFX;
        this.imageGL = imageGL;
        this.width = 0;
        this.height = 0;
    }

    public GuiImage(Image imageFX, int width, int height){
        this.imageFX = imageFX;
        this.imageGL = null;
        this.width = width;
        this.height = height;
    }

    public GuiImage(GLTexture imageGL, int width, int height){
        this.imageFX = null;
        this.imageGL = imageGL;
        this.width = width;
        this.height = height;
    }

    public Image getFX(){
        return imageFX;
    }

    public GLTexture getGL(){
        return imageGL;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight(){
        return height;
    }


}
