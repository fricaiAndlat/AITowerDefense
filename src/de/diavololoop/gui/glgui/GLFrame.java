package de.diavololoop.gui.glgui;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Chloroplast on 30.05.2017.
 */
public class GLFrame {

    private final static boolean DEBUG = true;

    private long windowID;

    private int windowWidth;
    private int windowHeight;
    private String title;

    private CopyOnWriteArrayList<GLElement> elements = new CopyOnWriteArrayList<GLElement>();
    private LinkedList<Runnable> tasks = new LinkedList<Runnable>();

    private Thread loopThread;

    public GLFrame(int width, int height, String title){

        this.windowWidth  = width;
        this.windowHeight = height;
        this.title = title;

        loopThread = new Thread(this::loop);
        loopThread.start();

    }

    private void init(){

        if(DEBUG) System.out.println("LWJGL Version " + Version.getVersion());

        if (!GLFW.glfwInit()) {
            System.err.println("Error initializing GLFW");
            System.exit(1);
        }

        windowID = GLFW.glfwCreateWindow(windowWidth, windowHeight, title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (windowID == MemoryUtil.NULL) {
            System.err.println("Error creating a window");
            System.exit(1);
        }

        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

        glfwMakeContextCurrent(windowID);
        glfwSwapInterval(1);
        GL.createCapabilities();

        glfwSetFramebufferSizeCallback(windowID, (window, w, h) -> onWindowSizeChange(w, h));
        glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> onKeyEvent(key, action, mods));

        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
    }

    private void loop(){
        if(DEBUG) System.out.println("GLFrame thread start working");

        init();

        while( !Thread.currentThread().isInterrupted() && !glfwWindowShouldClose(windowID) ){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            for(GLElement element: elements){
                element.draw();
            }

            glfwPollEvents();
            glfwSwapBuffers(windowID);

            if(!tasks.isEmpty()){
                tasks.pollFirst().run();
            }
        }

        if(DEBUG) System.out.println("GLFrame thread stopped working");

    }

    private void onWindowSizeChange(int width, int height){
        this.windowWidth  = width;
        this.windowHeight = height;

        for(GLElement element: elements){
            element.sizeChangeEvent(width, height);
        }
    }

    private void onKeyEvent(int key, int action, int mods){
        for(GLElement element: elements){
            element.keyEvent(key, action, mods);
        }
    }

    public void addTask(Runnable r){
        tasks.addLast(r);
    }

    public void addElement(GLElement element){
        elements.add(element);
    }
    public void removeElement(GLElement element){
        elements.remove(elements);
    }

}
