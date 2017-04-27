package com.schackteleers.projectrpg.engine.core;

/**
 * Interface for the main Game class.
 * @author Stijn Schack
 * @since 21/04/2017
 */
public interface IGameLogic {

    void init(Window window) throws Exception;

    void input(Window window);

    void update(double interval);

    void render(Window window);

    void cleanup();
}