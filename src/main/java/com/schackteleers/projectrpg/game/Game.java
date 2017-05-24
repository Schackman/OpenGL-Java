package com.schackteleers.projectrpg.game;

import com.schackteleers.projectrpg.engine.core.IGameLogic;
import com.schackteleers.projectrpg.engine.core.Window;
import com.schackteleers.projectrpg.engine.graphics.Renderer;
import com.schackteleers.projectrpg.engine.input.Keyboard;
import com.schackteleers.projectrpg.game.scene.GameScene;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * @author Stijn Schack
 * @since 21/04/2017
 */
public class Game implements IGameLogic {

    private final Renderer renderer;

    private Keyboard keyboard;

    private GameScene gameScene;


    Game() {
        this.keyboard = new Keyboard();
        this.renderer = new Renderer();
        this.gameScene = new GameScene();
    }

    @Override
    public void init(Window window) throws Exception {
        keyboard.init(window);
        renderer.init(window);
        renderer.getAmbientLight().set(.1f);
        gameScene.init();
        System.gc();
    }

    @Override
    public void input(Window window) {
        glfwSetWindowShouldClose(window.getWindowHandle(), keyboard.isKeyPressed(GLFW_KEY_ESCAPE));
        gameScene.input(keyboard);
    }

    @Override
    public void update(double interval) {
        gameScene.update();
    }

    @Override
    public void render(Window window) {
        renderer.render(window, gameScene);
    }

    @Override
    public void cleanup() {
        gameScene.cleanUp();
        renderer.cleanUp();
    }
}
