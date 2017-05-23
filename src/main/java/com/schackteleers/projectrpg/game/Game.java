package com.schackteleers.projectrpg.game;

import com.schackteleers.projectrpg.engine.core.IGameLogic;
import com.schackteleers.projectrpg.engine.core.Window;
import com.schackteleers.projectrpg.engine.gameobjects.GameObject;
import com.schackteleers.projectrpg.engine.graphics.Camera;
import com.schackteleers.projectrpg.engine.graphics.Mesh;
import com.schackteleers.projectrpg.engine.graphics.Renderer;
import com.schackteleers.projectrpg.engine.graphics.Texture;
import com.schackteleers.projectrpg.engine.graphics.light.PointLight;
import com.schackteleers.projectrpg.engine.input.Keyboard;
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
    private float moveCameraZ;
    private float rotCameraX;
    private float rotCameraY;
    private float rotCameraZ;
    private float ambientLightChange;

    Game() {
        this.keyboard = new Keyboard();
        this.renderer = new Renderer();
        this.camera = new Camera(7f, 6, 7, 38, 317, 0);
        this.gameObjectList = new ArrayList<>();
        this.pointLightList = new ArrayList<>();
    }

    @Override
    public void init(Window window) throws Exception {
        keyboard.init(window);
        renderer.init(window);
        renderer.getAmbientLight().set(.1f);

        final int MAX_X = 10;
        final int MAX_Z = 10;
        for (int x = 0; x < MAX_X; x++) {
            for (int z = 0; z < MAX_Z; z++) {
                gameObjectList.add(new GameObject(new Mesh(new Texture("grassblock"))).setPosition(x - MAX_X / 2, 0, z - MAX_Z / 2));
            }
        }
        gameObjectList.add(new GameObject(new Mesh(new Texture("grassblock"))).setPosition(0, 1, 0));
        pointLightList.add(new PointLight(new Vector3f(1, 1, 1), new Vector3f(0, 2, 0), .3f));
        System.gc();
    }

    @Override
    public void input(Window window) {
        glfwSetWindowShouldClose(window.getWindowHandle(), keyboard.isKeyPressed(GLFW_KEY_ESCAPE));

        moveCameraX = keyboard.setOnKeyPressed(GLFW_KEY_A, GLFW_KEY_D, -0.1f, 0.1f);
        moveCameraZ = keyboard.setOnKeyPressed(GLFW_KEY_S, GLFW_KEY_W, 0.1f, -0.1f);
        moveCameraY = keyboard.setOnKeyPressed(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_LEFT_CONTROL, 0.1f, -0.1f);
        rotCameraY = keyboard.setOnKeyPressed(GLFW_KEY_Q, GLFW_KEY_E, -1, 1);
        rotCameraX = keyboard.setOnKeyPressed(GLFW_KEY_UP, GLFW_KEY_DOWN, -1, 1);
        rotCameraZ = keyboard.setOnKeyPressed(GLFW_KEY_LEFT, GLFW_KEY_RIGHT, -1, 1);
    }

    @Override
    public void update(double interval) {
        camera.move(moveCameraX, moveCameraY, moveCameraZ);
        camera.rotate(rotCameraX, rotCameraY, rotCameraZ);
        renderer.addAmbientLight(ambientLightChange);
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
