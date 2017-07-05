package de.diavololoop.gui;

/**
 * Created by gast2 on 31.05.17.
 */
public interface KeyListener<E> {

    public static enum Action{PRESS, RELEASE}

    public void onEvent(E key, Action action);

}
