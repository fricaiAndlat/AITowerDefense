package de.diavololoop.game.effect;

import de.diavololoop.gui.GraphicUserInterface;

/**
 * Created by gast1 on 03.07.17.
 */
public interface Effect {

    public void draw(GraphicUserInterface handle, double zoom);
    public boolean isAlive();

}
