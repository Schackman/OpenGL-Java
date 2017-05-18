package com.schackteleers.projectrpg.engine.core;

/**
 * @author Stijn Schack
 * @since 21/04/2017
 */

import com.schackteleers.projectrpg.engine.fileio.ConfigFile;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class Engine implements Runnable {
    private final Thread gameLoopThread;

    private static int TARGET_FPS = 60;

    private static final int TARGET_UPS = 20;

    private final Window window;

    private final Timer timer;

    private final IGameLogic gameLogic;

    private static ConfigFile engineConfig;

    {
        try {
            engineConfig = new ConfigFile("engine");
            if (engineConfig.isEmpty()) {
                engineConfig = loadDefaultConfig();
                System.out.println("loaded default engine.ini");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Engine(IGameLogic gameLogic) {
        this.gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");

        this.timer = new Timer();

        int winWidth = Integer.parseInt(engineConfig.get("display", "iwidth"));
        int winHeight = Integer.parseInt(engineConfig.get("display", "iheight"));
        boolean winVsync = Boolean.parseBoolean(engineConfig.get("display", "bvsync"));
        this.window = new Window("ARCHON RPG", winWidth, winHeight, winVsync);
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
        TARGET_FPS = glfwGetVideoMode(glfwGetPrimaryMonitor()).refreshRate(); // Set TARGET_FPS to monitor refresh rate
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

            // this loop makes sure the game stays at the TARGET_UPS
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
            Runtime runtime = Runtime.getRuntime();
            int MB = 1048576;
            long maxMem = runtime.maxMemory() / MB;
            long freeMem = runtime.freeMemory() / MB;
            long totalMem = runtime.totalMemory() / MB;
            long usedMem = totalMem - freeMem;

            window.setTitle(String.format("%s | fps: %d | Used Mem: %d | Free Mem: %d | Tot Mem %d| Max Mem: %d", window.getTitle(), timer.getFps(), usedMem, freeMem, totalMem, maxMem));
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

    /**
     * updates the state of the game
     *
     * @param interval between updates in seconds
     */
    private void update(double interval) {
        gameLogic.update(interval);
    }

    /**
     * renders the game to the window
     */
    private void render() {
        gameLogic.render(window);
        window.update();
    }

    /**
     * Yields the thread to limit fps to TARGET_FPS. This way, no unnecessary resources are used
     */
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            Thread.yield();
        }
    }


    private ConfigFile loadDefaultConfig() throws IOException {
        Ini ini = new Ini();
        ini.put("display", "iwidth", 1280);
        ini.put("display", "iheight", 720);
        ini.put("display", "bvsync", false);

        return new ConfigFile("engine", ini);
    }
}
