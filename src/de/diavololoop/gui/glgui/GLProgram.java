package de.diavololoop.gui.glgui;

import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Peer on 25.01.2017.
 */
public class GLProgram {

    private int programID;

    public GLProgram(String vertex, String fragment) {

        this.programID = makeShader(vertex, fragment);

    }

    private int makeShader(String vertex, String fragment){
        int programID = glCreateProgram();

        int vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertexShaderID, vertex);
        glShaderSource(fragmentShaderID, fragment);

        glCompileShader(vertexShaderID);
        glCompileShader(fragmentShaderID);

        if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error creating vertex shader\n"
                    + glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)));

        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error creating fragment shader\n"
                    + glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));

        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);

        glLinkProgram(programID);

        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);

        return programID;

    }

    public int getID(){
        return programID;
    }

    public void use(){
        glUseProgram(programID);
    }

    @Override
    public void finalize(){
        GL20.glDeleteProgram(programID);
    }

}