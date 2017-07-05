package de.diavololoop.gui;

/**
 * Created by gast2 on 31.05.17.
 */
public interface MouseListener {

    public static enum Action {PRESS, RELEASE, DRAG, MOVE}
    public static enum Button {NONE, LEFT, RIGHT, MIDDLE}

    public void onEvent(Button button, Action action, double x, double y);

}
