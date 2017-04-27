package com.schackteleers.projectrpg.engine.graphics;

import com.schackteleers.projectrpg.engine.fileio.FileIO;
import com.schackteleers.projectrpg.engine.object.GameObject;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * @author Stijn Schack
 * @since 22/04/2017
 */
public class Renderer {
    private ShaderProgram shaderProgram;

    public Renderer() {

    }

    public void init() throws Exception {
        // init openGL
        GL.createCapabilities();
        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
        glClearColor(0, 0, 0, 1);

        // Initialize and link the Shader Program
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(FileIO.loadShaderResource("/shaders/vertexshader.glsl"));
        shaderProgram.createFragmentShader(FileIO.loadShaderResource("/shaders/fragmentshader.glsl"));
        shaderProgram.link();

    }

    public void render(List<GameObject> gameObjectList) {
        glClear(GL_COLOR_BUFFER_BIT);

        shaderProgram.bind(); // Tell GPU to use shader program

        for (GameObject gameObject : gameObjectList) {
            gameObject.getMesh().render();
        }

        shaderProgram.unbind(); // Tell GPU to stop using shader program
    }

    public void cleanUp() {
        GL.destroy();
    }
}
