package com.schackteleers.projectrpg.engine.graphics;

import com.schackteleers.projectrpg.engine.core.Window;
import com.schackteleers.projectrpg.engine.fileio.FileIO;
import com.schackteleers.projectrpg.engine.gameobjects.GameObject;
import com.schackteleers.projectrpg.engine.graphics.light.PointLight;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Stijn Schack
 * @since 22/04/2017
 */
public class Renderer {
    private static final String UNIFORM_PROJECTION_MATRIX = "projectionmatrix";
    private static final String UNIFORM_MODEL_VIEW_MATRIX = "modelviewmatrix";
    private static final String UNIFORM_TEXTURE_SAMPLER = "texture_sampler";
    private static final String UNIFORM_AMBIENT_LIGHT = "ambient_light";
    private static final String UNIFORM_POINT_LIGHT = "point_light";

    private ShaderProgram shaderProgram;
    private Transformation transformation;
    private Vector3f ambientLight;

    public Renderer() {
        this.transformation = new Transformation();
        this.ambientLight = new Vector3f(.5f, .5f, .5f);
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
        shaderProgram.createUniform(UNIFORM_PROJECTION_MATRIX);
        shaderProgram.createUniform(UNIFORM_MODEL_VIEW_MATRIX);
        shaderProgram.createUniform(UNIFORM_TEXTURE_SAMPLER);
        shaderProgram.createUniform(UNIFORM_AMBIENT_LIGHT);
        shaderProgram.createPointLightUniform(UNIFORM_POINT_LIGHT);
    }

    public void render(Window window, Camera camera, List<GameObject> gameObjectList, List<PointLight> pointLightList) {
        glClear(GL_COLOR_BUFFER_BIT); //Clear the frame buffer so a new frame can be rendered

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind(); // Tell GPU to use shader program

        shaderProgram.setUniform(UNIFORM_PROJECTION_MATRIX, transformation.getProjectionMatrix(window.getWidth(), window.getHeight())); // Update projection matrix
        shaderProgram.setUniform(UNIFORM_TEXTURE_SAMPLER, 0);
        shaderProgram.setUniform(UNIFORM_AMBIENT_LIGHT, ambientLight);

        Matrix4f viewMatrix = transformation.getViewMatrix(camera); // Update view matrix

        for (PointLight pointLight : pointLightList) {
            PointLight currPointLight = new PointLight(pointLight);
            Vector2f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 0, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            shaderProgram.setUniform(UNIFORM_POINT_LIGHT, currPointLight);
        }

        // Render all game objects
        for (GameObject gameObject : gameObjectList) {
            Mesh2D mesh = gameObject.getMesh();
            // set modelview matrix for this gameobjects
            shaderProgram.setUniform(UNIFORM_MODEL_VIEW_MATRIX, transformation.getModelViewMatrix(gameObject, viewMatrix));
            mesh.render();
        }

        shaderProgram.unbind(); // Tell GPU to stop using shader program
    }

    public void cleanUp() {
        shaderProgram.cleanUp();
        GL.destroy();
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public void addAmbientLight(Vector3f ambientLightChange){
        this.ambientLight.add(ambientLightChange);
    }

    public void addAmbientLight(float r, float g, float b){
        this.ambientLight.add(r, g, b);
    }

    public void addAmbientLight(float rgb){
        this.ambientLight.add(rgb, rgb, rgb);
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }
}
