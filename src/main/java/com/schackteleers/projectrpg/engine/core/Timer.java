package com.schackteleers.projectrpg.engine.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * @author Stijn Schack
 * @since 21/04/2017
 */
class Timer {

    private double lastLoopTime;
    private int fps;
    private int fpsCount;
    private int ups;
    private int upsCount;
    private float timeCount;


    void init() {
        lastLoopTime = getTime();
    }

    /**
     * @return Time in seconds (double) since glfwinit(). Unless timer was reset with SetTime().
     */
    double getTime() {
        return glfwGetTime();
    }

    /**
     * @return Time elapsed since previous loop ended.
     */
    double getElapsedTime() {
        double time = getTime();
        double elapsedTime = time - lastLoopTime;
        lastLoopTime = time;
        this.timeCount += elapsedTime;
        return elapsedTime;
    }

    /**
     * @return Exact time since glfwinit() that the previous loop ended;
     */
    double getLastLoopTime() {
        return lastLoopTime;
    }

    /**
     * Increments fpsCount by 1. Used to calculate how many frames were rendered in the last second.
     */
    void updateFpsCount() {
        fpsCount++;
    }

    /**
     * Increments upsCount by 1. Used to calculate how many times the game updated in the last second.
     */
    void updateUpsCount() {
        upsCount++;
    }

    /**
     * Used to count when 1 second has passed. Also stores fps and ups
     */
    void update() {
        if (this.timeCount > 1f) {
            fps = fpsCount;
            fpsCount = 0;

            ups = upsCount;
            upsCount = 0;

            timeCount--;
        }
    }

    int getFps() {
        return fps;
    }

    int getUps() {
        return ups;
    }
}