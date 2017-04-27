package com.schackteleers.projectrpg.game;

import com.schackteleers.projectrpg.engine.core.IGameLogic;
import com.schackteleers.projectrpg.engine.core.Window;
import com.schackteleers.projectrpg.engine.graphics.Renderer;
import com.schackteleers.projectrpg.engine.object.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stijn Schack
 * @since 21/04/2017
 */
public class Game implements IGameLogic {

    private final Renderer renderer;
    private List<GameObject> gameObjectList;

    Game() {
        this.renderer = new Renderer();
        this.gameObjectList = new ArrayList<>();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init();

        gameObjectList.add(new GameObject());
    }

    @Override
    public void input(Window window) {

    }

    @Override
    public void update(double interval) {

    }

    @Override
    public void render(Window window) {
        renderer.render(gameObjectList);
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
