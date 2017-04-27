package com.schackteleers.projectrpg.game;

import com.schackteleers.projectrpg.engine.core.IGameLogic;
import com.schackteleers.projectrpg.engine.core.Window;
import com.schackteleers.projectrpg.engine.graphics.Camera;
import com.schackteleers.projectrpg.engine.graphics.Renderer;
import com.schackteleers.projectrpg.engine.input.Keyboard;
import com.schackteleers.projectrpg.engine.object.GameObject;

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

    private Keyboard keyboard;

    private float moveCameraX;
    private float moveCameraY;
    private float rotateCamera;

    Game() {
        this.keyboard = new Keyboard();
        this.renderer = new Renderer();
        this.camera = new Camera();
        this.gameObjectList = new ArrayList<>();
    }

    @Override
    public void init(Window window) throws Exception {
        keyboard.init(window);
        renderer.init(window);

        gameObjectList.add(new GameObject());
        gameObjectList.add(new GameObject().setPosition(1, 1));
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
    }

    @Override
    public void update(double interval) {
        camera.translate(moveCameraX, moveCameraY);
        camera.rotate(rotateCamera);
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameObjectList);
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
