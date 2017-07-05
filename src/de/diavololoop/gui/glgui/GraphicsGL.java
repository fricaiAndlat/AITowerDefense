package de.diavololoop.gui.glgui;

import de.diavololoop.util.IOUtil;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Chloroplast on 30.05.2017.
 */
public class GraphicsGL {

    private static boolean INIT = false;
    private static int VBO_RECT;
    private static GLProgram PRGM_BASIC_COLOR;

    private float colorR = 0;
    private float colorG = 0;
    private float colorB = 0;
    private float colorA = 1;

    public GraphicsGL(){

        init();

    }

    private static synchronized void init() {
        if(INIT){
            return;
        }


        PRGM_BASIC_COLOR = new GLProgram(
                IOUtil.getStringFromStreamNE(GraphicsGL.class.getResourceAsStream("../../../shader/basic_color.vertex.shader")),
                IOUtil.getStringFromStreamNE(GraphicsGL.class.getResourceAsStream("../../../shader/basic_color.fragment.shader")));

        FloatBuffer vertices = BufferUtils.createFloatBuffer(12);
        vertices.put(-1).put(-1);
        vertices.put( 1).put(-1);
        vertices.put( 1).put( 1);

        vertices.put(-1).put(-1);
        vertices.put( 1).put( 1);
        vertices.put(-1).put( 1);



        vertices.flip();

        VBO_RECT = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, VBO_RECT);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, VBO_RECT);
        int posCoord = glGetAttribLocation(PRGM_BASIC_COLOR.getID(), "point");
        //glEnableVertexAttribArray(posCoord);
        glVertexAttribPointer(posCoord, 2, GL_FLOAT, false, 0, 0);

        INIT = true;
    }


    public void setColor(Color color){
        colorR = color.getRed()   /255f;
        colorG = color.getGreen() /255f;
        colorB = color.getBlue()  /255f;
        colorA = color.getAlpha() /255f;
    }
    public void setColor(float r, float g, float b){
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        this.colorA = 1;
    }
    public void setColor(float r, float g, float b, float a){
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        this.colorA = a;
    }

    public void fillRect(float x, float y, float w, float h){
       fillBasic(x, y, w, h, VBO_RECT);


    }

    private void fillBasic(float x, float y, float w, float h, int vbo){

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        PRGM_BASIC_COLOR.use();

        int colorPos = glGetAttribLocation(PRGM_BASIC_COLOR.getID(), "color_in");
        glUniform4f(colorPos, colorR, colorG, colorB, colorA);

        Matrix4f transformation = new Matrix4f();
        //transformation.scale(w, h,1).translate(x, y, 0);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        transformation.get(fb);

        int transPos = glGetUniformLocation(PRGM_BASIC_COLOR.getID(), "transformation");
        glUniformMatrix4fv(transPos, false, fb);

        glDrawArrays(GL_TRIANGLES, 0, 12);
    }

}
