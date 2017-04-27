package com.schackteleers.projectrpg.engine.graphics;

import com.schackteleers.projectrpg.engine.core.Window;
import com.schackteleers.projectrpg.engine.fileio.FileIO;
import com.schackteleers.projectrpg.engine.object.GameObject;
import com.schackteleers.projectrpg.engine.object.Transformation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Stijn Schack
 * @since 22/04/2017
 */
public class Renderer {
    private ShaderProgram shaderProgram;
    private Transformation transformation;

    public Renderer() {
        this.transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        // init openGL
        GL.createCapabilities();
        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
        glClearColor(0, 0, 0, 1);

        // Initialize and link the Shader Program
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(FileIO.loadShaderResource("/shaders/vertexshader.glsl"));
        shaderProgram.createFragmentShader(FileIO.loadShaderResource("/shaders/fragmentshader.glsl"));
        shaderProgram.link();

        // Create Uniforms
        shaderProgram.createUniform("projectionmatrix");
        shaderProgram.createUniform("modelviewmatrix");
    }

    public void render(Window window, Camera camera, List<GameObject> gameObjectList) {
        glClear(GL_COLOR_BUFFER_BIT); //Clear the frame buffer so a new frame can be rendered

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind(); // Tell GPU to use shader program

        shaderProgram.setUniform("projectionmatrix", transformation.getProjectionMatrix(window.getWidth(), window.getHeight())); // Update projection matrix

        Matrix4f viewMatrix = transformation.getViewMatrix(camera); // Update view matrix

        // Render all game objects
        for (GameObject gameObject : gameObjectList) {
            Mesh2D mesh = gameObject.getMesh();
            // set modelview matrix for this object
            shaderProgram.setUniform("modelviewmatrix", transformation.getModelViewMatrix(gameObject, viewMatrix));
            mesh.render();
        }

        shaderProgram.unbind(); // Tell GPU to stop using shader program
    }

    public void cleanUp() {
        shaderProgram.cleanUp();
        GL.destroy();
    }
}
