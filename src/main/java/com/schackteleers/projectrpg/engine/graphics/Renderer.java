package com.schackteleers.projectrpg.engine.graphics;

import com.schackteleers.projectrpg.engine.fileio.FileIO;
import com.schackteleers.projectrpg.engine.object.GameObject;
import org.lwjgl.opengl.GL;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

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
        glClear(GL_COLOR_BUFFER_BIT); //Clear the frame buffer so a new frame can be rendered

        shaderProgram.bind(); // Tell GPU to use shader program

        // Render all objects
        for (GameObject gameObject : gameObjectList) {
            gameObject.getMesh().render();
        }

        shaderProgram.unbind(); // Tell GPU to stop using shader program
    }

    public void cleanUp() {
        shaderProgram.cleanUp();
        GL.destroy();
    }
}
