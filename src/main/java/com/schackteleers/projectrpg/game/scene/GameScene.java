package com.schackteleers.projectrpg.game.scene;

import com.schackteleers.projectrpg.engine.fileio.OBJLoader;
import com.schackteleers.projectrpg.engine.gameobjects.GameObject;
import com.schackteleers.projectrpg.engine.graphics.Material;
import com.schackteleers.projectrpg.engine.graphics.Mesh;
import com.schackteleers.projectrpg.engine.graphics.Texture;
import com.schackteleers.projectrpg.engine.graphics.light.PointLight;
import com.schackteleers.projectrpg.engine.input.Keyboard;
import com.schackteleers.projectrpg.engine.scene.Scene;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Stijn Schack
 * @since 24/05/2017
 */
public class GameScene extends Scene {

    private float moveCameraX;
    private float moveCameraY;
    private float moveCameraZ;
    private float rotCameraX;
    private float rotCameraY;
    private float rotCameraZ;
    private float ambientLightChange;

    public GameScene() {
        super();
    }

    @Override
    public void init() {
        Mesh cube = null;
        try {
            cube = OBJLoader.loadMesh("cube").setMaterial(new Material(new Texture("grassblock"), 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final int MAX_X = 10;
        final int MAX_Z = 10;
        for (int x = 0; x < MAX_X; x++) {
            for (int z = 0; z < MAX_Z; z++) {
                gameObjectList.add(new GameObject(cube).setPosition(x - MAX_X / 2, 0, z - MAX_Z / 2));
            }
        }
        gameObjectList.add(new GameObject(cube).setPosition(0, 1, 0));
        pointLightList.add(new PointLight(new Vector3f(1, 1, 1), new Vector3f(0, 2, 0), .3f));
    }

    @Override
    public void input(Keyboard keyboard) {
        moveCameraX = keyboard.setOnKeyPressed(GLFW_KEY_A, GLFW_KEY_D, -0.1f, 0.1f);
        moveCameraZ = keyboard.setOnKeyPressed(GLFW_KEY_S, GLFW_KEY_W, 0.1f, -0.1f);
        moveCameraY = keyboard.setOnKeyPressed(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_LEFT_CONTROL, 0.1f, -0.1f);
        rotCameraY = keyboard.setOnKeyPressed(GLFW_KEY_Q, GLFW_KEY_E, -1, 1);
        rotCameraX = keyboard.setOnKeyPressed(GLFW_KEY_UP, GLFW_KEY_DOWN, -1, 1);
        rotCameraZ = keyboard.setOnKeyPressed(GLFW_KEY_LEFT, GLFW_KEY_RIGHT, -1, 1);
    }

    @Override
    public void update() {
        camera.move(moveCameraX, moveCameraY, moveCameraZ);
        camera.rotate(rotCameraX, rotCameraY, rotCameraZ);
    }

    @Override
    public void renderHud() {

    }

    @Override
    public void cleanUp() {
        gameObjectList.forEach(GameObject::cleanUp);
    }
}
