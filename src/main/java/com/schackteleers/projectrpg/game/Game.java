package com.schackteleers.projectrpg.game;

import com.schackteleers.projectrpg.engine.core.IGameLogic;
import com.schackteleers.projectrpg.engine.core.Window;
import com.schackteleers.projectrpg.engine.gameobjects.GameObject;
import com.schackteleers.projectrpg.engine.graphics.Camera;
import com.schackteleers.projectrpg.engine.graphics.Renderer;
import com.schackteleers.projectrpg.engine.graphics.light.PointLight;
import com.schackteleers.projectrpg.engine.input.Keyboard;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Stijn Schack
 * @since 21/04/2017
 */
public class Game implements IGameLogic {

    private final Renderer renderer;
    private Camera camera;
    private List<GameObject> gameObjectList;
    private List<PointLight> pointLightList;

    private Keyboard keyboard;

    private float moveCameraX;
    private float moveCameraY;
    private float rotateCamera;
    private float ambientLightChange;

    Game() {
        this.keyboard = new Keyboard();
        this.renderer = new Renderer();
        this.camera = new Camera();
        this.gameObjectList = new ArrayList<>();
        this.pointLightList = new ArrayList<>();
    }

    @Override
    public void init(Window window) throws Exception {
        keyboard.init(window);
        renderer.init(window);
        renderer.getAmbientLight().set(0);

        int max_i = 50;
        int max_j = 50;
        for (int i = 0; i < max_i; i++) {
            for (int j = 0; j < max_j; j++) {
                gameObjectList.add(new GameObject().setPosition((i-max_i/2), (j-max_j/2)));
            }
        }

        pointLightList.add(new PointLight(new Vector3f(0.37f, 0.25f, 0.05f), new Vector2f(), 0.1f));

        System.gc();
    }

    @Override
    public void input(Window window) {
        if (keyboard.isKeyPressed(GLFW_KEY_ESCAPE))glfwSetWindowShouldClose(window.getWindowHandle(), true);

        if (keyboard.isKeyPressed(GLFW_KEY_LEFT)) moveCameraX = -0.1f;
        else if (keyboard.isKeyPressed(GLFW_KEY_RIGHT)) moveCameraX = +0.1f;
        else moveCameraX = 0;

        if (keyboard.isKeyPressed(GLFW_KEY_UP)) moveCameraY = 0.1f;
        else if (keyboard.isKeyPressed(GLFW_KEY_DOWN)) moveCameraY = -0.1f;
        else moveCameraY = 0;

        if (keyboard.isKeyPressed(GLFW_KEY_Q)) rotateCamera = -0.5f*10;
        else if (keyboard.isKeyPressed(GLFW_KEY_E)) rotateCamera = +0.5f*10;
        else rotateCamera = 0;

        if (keyboard.isKeyPressed(GLFW_KEY_O)) ambientLightChange = -.05f;
        else if (keyboard.isKeyPressed(GLFW_KEY_P)) ambientLightChange = 0.05f;
        else ambientLightChange = 0;
    }

    @Override
    public void update(double interval) {
        camera.translate(moveCameraX, moveCameraY);
        camera.rotate(rotateCamera);
        //renderer.getAmbientLight().add(ambientLightChange, ambientLightChange, ambientLightChange);
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameObjectList, pointLightList);
    }

    @Override
    public void cleanup() {
        for (GameObject gameObject :
                gameObjectList) {
            gameObject.cleanUp();
        }
        renderer.cleanUp();
    }
}
