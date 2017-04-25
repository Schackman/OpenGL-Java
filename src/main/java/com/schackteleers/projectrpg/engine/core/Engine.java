package com.schackteleers.projectrpg.engine.core;

/**
 * @author Stijn Schack
 * @since 21/04/2017
 */

import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.system.Configuration;

import static org.lwjgl.glfw.GLFW.*;

public class Engine implements Runnable {
    private final Thread gameLoopThread;

    private static int TARGET_FPS = 60;

    private static final int TARGET_UPS = 20;

    private final Window window;

    private final Timer timer;

    private final IGameLogic gameLogic;

    public Engine(IGameLogic gameLogic) {
        this.gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        this.timer = new Timer();
        this.window = new Window("RPG", 1280, 720, false);
        this.gameLogic = gameLogic;
    }

    /**
     * Start game
     */
    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    /**
     * Start new Thread, different method for mac
     */
    public void start() {
        String osName = System.getProperty("os.name");
        System.out.println("OS: " + osName +
                "\nOS Version: " + System.getProperty("os.version"));
        if (osName.contains("Mac")) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    private void init() throws Exception {
        window.init();
        TARGET_FPS = glfwGetVideoMode(glfwGetPrimaryMonitor()).refreshRate();
        System.out.println("Monitor Refresh Rate: " + TARGET_FPS + "Hz");
        timer.init();
        gameLogic.init(window);
    }

    private void gameLoop() {
        double counter = 0d;
        double interval = 1d / TARGET_UPS;

        while (!window.windowShouldClose()) {
            counter += timer.getElapsedTime();

            input();

            while (counter >= interval) {
                update(interval);
                counter -= interval;
                timer.updateUpsCount();
            }

            render();
            timer.updateFpsCount();

            if (!window.shouldVSync() && TARGET_FPS != 0) {
                sync();
            }

            timer.update();

            window.setTitle(String.format("%s | FPS: %d", window.getTitle(), timer.getFps()));
        }
    }

    private void cleanUp() {
        gameLogic.cleanup();
        window.cleanUp();
        glfwTerminate();
    }

    private void input() {
        glfwPollEvents();
        gameLogic.input(window);
    }

    private void update(double interval) {
        gameLogic.update(interval);
    }

    private void render() {
        gameLogic.render(window);
        window.update();
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            Thread.yield();
        }
    }
}
