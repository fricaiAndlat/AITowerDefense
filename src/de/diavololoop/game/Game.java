package de.diavololoop.game;

import de.diavololoop.game.map.Map;
import de.diavololoop.gui.GraphicUserInterface;
import de.diavololoop.gui.KeyListener;
import de.diavololoop.gui.MouseListener;
import de.diavololoop.gui.fxgui.FXGui;
import javafx.scene.input.KeyCode;

/**
 * Created by gast2 on 20.06.17.
 */
public class Game {


    public static void main(String[] args){
        Game game = new Game();
    }

    private FXGui gui;
    private Resource resource;
    private Map map;

    private boolean scoutMode = false;

    public Game(){
        gui = FXGui.create();
        gui.setSize(1000, 1000);
        resource = new Resource();

        gui.addDrawTask(LoadGui::visualizeLoad);


        resource.init();

        map = new Map();

        gui.addDrawTask(this::gameDraw);

        gui.addKeyListener(this::onKeyChange);
        gui.addMouseListener(this::onMouseEvent);


    }

    private void onMouseEvent(MouseListener.Button button, MouseListener.Action action, double x, double y) {

        if(action == MouseListener.Action.MOVE){

            if(scoutMode){
                map.showWay(x, y);
            }

        }

    }

    private void onKeyChange(KeyCode keyCode, KeyListener.Action action) {

        if(keyCode == KeyCode.F2){
            if(action == KeyListener.Action.PRESS){
                map.toggleDebug();
            }
        }else if(keyCode == KeyCode.F3){
            if(action == KeyListener.Action.PRESS){
                map.toggleHelp();
            }
        }else if(keyCode == KeyCode.S){
            if(action == KeyListener.Action.PRESS){
                scoutMode = !scoutMode;
            }
        }


    }

    long tSum = 0;
    long tLast = System.currentTimeMillis();
    int cc = 0;

    private void gameDraw(GraphicUserInterface<?> handle) {

        if(++cc > 60){
            cc = 0;


            System.out.println(60 * 1000 / tSum);
            tSum = 0;

        }

        tSum += System.currentTimeMillis() - tLast;
        tLast = System.currentTimeMillis();

        handle.clear();

        map.draw(handle);

        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
