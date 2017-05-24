package com.schackteleers.projectrpg.engine.scene;

import com.schackteleers.projectrpg.engine.gameobjects.GameObject;
import com.schackteleers.projectrpg.engine.graphics.Camera;
import com.schackteleers.projectrpg.engine.graphics.light.PointLight;
import com.schackteleers.projectrpg.engine.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stijn Schack
 * @since 24/05/2017
 */
public abstract class Scene {
    protected Camera camera;
    protected List<GameObject> gameObjectList;
    protected List<PointLight> pointLightList;

    protected Scene() {
        this.camera = new Camera();
        this.gameObjectList = new ArrayList<>();
        this.pointLightList = new ArrayList<>();
    }

    protected abstract void init();

    protected abstract void input(Keyboard keyboard);

    protected abstract void update();

    protected abstract void renderHud();

    public abstract void cleanUp();

    public Camera getCamera() {
        return camera;
    }

    public List<GameObject> getGameObjectList() {
        return gameObjectList;
    }

    public List<PointLight> getPointLightList() {
        return pointLightList;
    }
}
