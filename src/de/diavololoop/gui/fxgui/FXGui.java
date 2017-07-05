package de.diavololoop.gui.fxgui;

import de.diavololoop.gui.GraphicUserInterface;
import de.diavololoop.gui.GuiImage;
import de.diavololoop.gui.KeyListener;
import de.diavololoop.gui.MouseListener;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.InputStream;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by gast2 on 31.05.17.
 */
public class FXGui extends Application implements GraphicUserInterface<KeyCode>{

    private static FXGui INSTANCE;
    private static Object NOTIFYER = new Object();

    public synchronized static FXGui create(){


        Thread threadFX = new Thread( FXGui::launch );
        threadFX.start();

        synchronized (NOTIFYER) {

            try {
                NOTIFYER.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return INSTANCE;

    }

    public static Image loadImage(InputStream in){
        return new Image(in);
    }


    /* OBJECT FOO */

    private Stage stage;
    private javafx.scene.canvas.Canvas canvas;
    private GraphicsContext context;
    private Scene scene;

    private CopyOnWriteArrayList<Consumer<GraphicUserInterface>> drawTasks = new CopyOnWriteArrayList<Consumer<GraphicUserInterface>>();
    private CopyOnWriteArrayList<BiConsumer<Double, Double>> sizeListenerList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<MouseListener> mouseListenerList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<KeyListener> keyListenerList = new CopyOnWriteArrayList<>();

    private double width = 0;
    private double height = 0;
    private double ratio;

    private double viewportX = -1;
    private double viewportY = -1;
    private double viewportW = 2;
    private double viewportH = 2;

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        Group root = new Group();
        canvas = new Canvas(800, 800);
        root.getChildren().add(canvas);
        scene = new Scene(root, 800, 800);
        stage.setScene(scene);
        stage.show();

        context = canvas.getGraphicsContext2D();

        FXGui own = this;

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                drawTasks.forEach(task -> task.accept(own));

            }
        };
        timer.start();

        stage.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePress);
        stage.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseRelease);
        stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDrag);
        stage.addEventHandler(MouseEvent.MOUSE_MOVED, this::onMouseMove);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyPress);
        stage.addEventHandler(KeyEvent.KEY_RELEASED, this::onKeyRelease);

        stage.widthProperty().addListener((obs, o, n) -> {
            width = n.doubleValue();
            notifySizeChanged();
        });
        stage.heightProperty().addListener((obs, o, n) -> {
            height = n.doubleValue();
            notifySizeChanged();
        });


        setSize(800,800);

        Thread.sleep(1000);

        INSTANCE = this;
        synchronized (NOTIFYER) {
            NOTIFYER.notifyAll();
        }
    }
    private void onMouse(MouseEvent e, MouseListener.Action action){
        double x = 2 * e.getSceneX()/stage.getHeight() - ratio;
        double y = 2 * e.getSceneY()/stage.getHeight() - 1;

        switch(e.getButton()){
            case PRIMARY:
                mouseListenerList.forEach(l -> l.onEvent(MouseListener.Button.LEFT, action, x, y));
                break;
            case SECONDARY:
                mouseListenerList.forEach(l -> l.onEvent(MouseListener.Button.RIGHT, action, x, y));
                break;
            case MIDDLE:
                mouseListenerList.forEach(l -> l.onEvent(MouseListener.Button.MIDDLE, action, x, y));
                break;
            default:
                mouseListenerList.forEach(l -> l.onEvent(MouseListener.Button.NONE, action, x, y));

        }
    }
    private void onMousePress(MouseEvent e) {
        onMouse(e, MouseListener.Action.PRESS);
    }
    private void onMouseRelease(MouseEvent e) {
        onMouse(e, MouseListener.Action.RELEASE);
    }
    private void onMouseDrag(MouseEvent e) {
        onMouse(e, MouseListener.Action.DRAG);
    }
    private void onMouseMove(MouseEvent e) {
        onMouse(e, MouseListener.Action.MOVE);
    }
    private void onKeyPress(KeyEvent e) {
        keyListenerList.forEach(l -> l.onEvent(e.getCode(), KeyListener.Action.PRESS));
    }
    private void onKeyRelease(KeyEvent e) {
        keyListenerList.forEach(l -> l.onEvent(e.getCode(), KeyListener.Action.RELEASE));
    }



    @Override
    public void addDrawTask(Consumer<GraphicUserInterface> task) {
        drawTasks.add(task);
    }


    private void notifySizeChanged(){
        ratio = width/height;


        sizeListenerList.forEach(listener -> listener.accept(width, height));
    }

    @Override
    public void setSize(int w, int h) {
        width = w;
        height = h;


        Platform.runLater(() -> {

            canvas.setWidth(w);
            canvas.setHeight(h);

            stage.setWidth(w);
            stage.setHeight(h);

            scene.getWindow().centerOnScreen();
        });

        notifySizeChanged();

    }

    @Override
    public void setResizable(boolean state) {
        stage.setResizable(state);
    }

    @Override
    public void resizeToFull() {
        Platform.runLater(() -> {
            stage.setMaximized(true);
            width = stage.getWidth();
            height = stage.getHeight();

            canvas.setWidth(width);
            canvas.setHeight(height);

            ratio = width/height;
        });

        notifySizeChanged();

    }

    @Override
    public void setFullscreen(boolean state) {
        Platform.runLater(() -> {
            stage.setFullScreen(state);
        });
    }

    @Override
    public void setColor(double r, double g, double b) {
        context.setStroke(new Color(r, g, b, 1));
        context.setFill(new Color(r, g, b, 1));
    }

    @Override
    public void setColor(double r, double g, double b, double a) {
        context.setStroke(new Color(r, g, b, a));
        context.setFill(new Color(r, g, b, a));
    }

    @Override
    public void fillRect(double x, double y, double w, double h) {

        h *= viewportH * height * 0.25;
        w *= viewportH * height * 0.25;

        x = width  * (viewportX/(2*ratio) + 0.5) + (x + viewportW/viewportH) * viewportH * height * 0.25 - w/2;
        y = height * (viewportY/(2)       + 0.5) + (y + 1)                   * viewportH * height * 0.25 - h/2;


        context.fillRect(x, y, w, h);
    }

    @Override
    public void drawRect(double x, double y, double w, double h) {


        h *= viewportH * height * 0.25;
        w *= viewportH * height * 0.25;

        x = width  * (viewportX/(2*ratio) + 0.5) + (x + viewportW/viewportH) * viewportH * height * 0.25 - w/2;
        y = height * (viewportY/(2)       + 0.5) + (y + 1)                   * viewportH * height * 0.25 - h/2;

        context.strokeRect(x, y, w, h);
    }

    @Override
    public void fillOval(double x, double y, double w, double h) {

        h *= viewportH * height * 0.25;
        w *= viewportH * height * 0.25;

        x = width  * (viewportX/(2*ratio) + 0.5) + (x + viewportW/viewportH) * viewportH * height * 0.25 - w/2;
        y = height * (viewportY/(2)       + 0.5) + (y + 1)                   * viewportH * height * 0.25 - h/2;


        context.fillOval(x, y, w, h);
    }

    @Override
    public void drawOval(double x, double y, double w, double h) {

        h *= viewportH * height * 0.25;
        w *= viewportH * height * 0.25;

        x = width  * (viewportX/(2*ratio) + 0.5) + (x + viewportW/viewportH) * viewportH * height * 0.25 - w/2;
        y = height * (viewportY/(2)       + 0.5) + (y + 1)                   * viewportH * height * 0.25 - h/2;


        context.strokeOval(x, y, w, h);
    }

    @Override
    @Deprecated
    public void drawImage(double x, double y, GuiImage image) {

        x = width  * (viewportX/(2*ratio) + 0.5) + (x + viewportW/viewportH) * viewportH * height * 0.25;
        y = height * (viewportY/(2)       + 0.5) + (y + 1)                   * viewportH * height * 0.25;


        context.drawImage(image.getFX(), x, y);
    }

    @Override
    public void drawImage(double x, double y, double w, double h, GuiImage image) {

        h *= viewportH * height * 0.25;
        w *= viewportH * height * 0.25;

        x = width  * (viewportX/(2*ratio) + 0.5) + (x + viewportW/viewportH) * viewportH * height * 0.25 - w/2;
        y = height * (viewportY/(2)       + 0.5) + (y + 1)                   * viewportH * height * 0.25 - h/2;

        context.drawImage(image.getFX(), x, y, w, h);
    }

    @Override
    public void drawImage(double x, double y, double w, double h, double angle, GuiImage image) {

        context.save();

        h *= viewportH * height * 0.25;
        w *= viewportH * height * 0.25;

        x = width  * (viewportX/(2*ratio) + 0.5) + (x + viewportW/viewportH) * viewportH * height * 0.25;
        y = height * (viewportY/(2)       + 0.5) + (y + 1)                   * viewportH * height * 0.25;


        rotate(angle, x, y);

        x -= w/2;
        y -= h/2;

        context.drawImage(image.getFX(), x, y, w, h);

        context.restore();
    }

    private void rotate(double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        context.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    @Override
    public void clear() {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @Override
    public double getRatio() {
        return ratio;
    }

    @Override
    public void setViewPort(double x, double y, double w, double h) {
        this.viewportX = x;
        this.viewportY = y;
        this.viewportW = w;
        this.viewportH = h;
    }

    @Override
    public void addSizeListener(BiConsumer<Double, Double> sizeListener) {
        sizeListenerList.add(sizeListener);
    }

    @Override
    public void addMouseListener(MouseListener mouseListener) {
        mouseListenerList.add(mouseListener);
    }

    @Override
    public void addKeyListener(KeyListener<KeyCode> keyListener) {
        keyListenerList.add(keyListener);
    }
}
