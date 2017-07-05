package de.diavololoop.game.map;

import de.diavololoop.gui.GraphicUserInterface;
import de.diavololoop.gui.GuiImage;

/**
 * Created by gast2 on 20.06.17.
 */
public class CustomField extends Field{

    private final GuiImage image;

    private double x, y, nx, ny;

    public CustomField(int x, int y, int nx, int ny, GuiImage image){
        super(x, y, nx, ny);
        this.image = image;

        this.x = x;
        this.y = y;
        this.nx = nx;
        this.ny = ny;
    }

    @Override
    public GuiImage getImage() {

        return this.image;
    }

    @Override
    public void onDraw(GraphicUserInterface<?> handle, double zoom) {

        handle.drawImage((x - nx/2 + 0.5) * zoom, (y - ny/2 + 0.5) * zoom, zoom, zoom, image);


    }

}
