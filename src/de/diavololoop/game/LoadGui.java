package de.diavololoop.game;

import de.diavololoop.gui.GraphicUserInterface;

/**
 * Created by gast2 on 20.06.17.
 */
public class LoadGui {

    private static double BAR_HEIGHT = 0.2;
    private static double BAR_WIDTH = 0.9;


    public static void visualizeLoad(GraphicUserInterface handle){

        Resource res = Resource.current();

        if(res == null || res.progress() >= 1){
            return;
        }

        handle.setColor(0, 1, 0);

        handle.drawRect((float)-BAR_WIDTH, (float)-BAR_HEIGHT, (float)BAR_WIDTH*2, (float)BAR_HEIGHT*2);

        handle.fillRect((float)-BAR_WIDTH, (float)-BAR_HEIGHT, (float)(BAR_WIDTH * 2 * res.progress()), (float)BAR_HEIGHT*2);



    }

}
