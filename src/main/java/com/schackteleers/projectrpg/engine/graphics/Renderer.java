package com.schackteleers.projectrpg.engine.graphics;

import com.schackteleers.projectrpg.engine.object.GameObject;
import org.lwjgl.opengl.GL;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Stijn Schack
 * @since 22/04/2017
 */
public class Renderer {

    public Renderer() {

    }

    public void init() {
        // init openGL
        GL.createCapabilities();
        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));

        glClearColor(0, 0, 0, 1);
    }

    public void render(List<GameObject> gameObjectList) {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void cleanUp() {
        GL.destroy();
    }
}
