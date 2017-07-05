package de.diavololoop.gui;

import java.util.HashMap;

/**
 * Created by Chloroplast on 21.06.2017.
 */
public class KeyState<T> {


    private HashMap<T, KeyListener.Action> states = new HashMap<T, KeyListener.Action>();

    public KeyState(GraphicUserInterface<T> gui){
        gui.addKeyListener(this::listener);
    }

    private void listener(T key, KeyListener.Action action) {

        states.put(key, action);

    }

    public KeyListener.Action getState(T key){
        KeyListener.Action result = states.get(key);
        if(result == null){
            return KeyListener.Action.RELEASE;
        }
        return result;
    }

}
