package com.schackteleers.projectrpg.engine.input;

import com.schackteleers.projectrpg.engine.core.Window;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Stijn Schack
 * @since 27/04/2017
 */
public class Keyboard {
    private GLFWKeyCallback keyCallback;
    private Window window;

    public void init(Window window){
        this.window = window;
        glfwSetKeyCallback(window.getWindowHandle(), keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {

            }
        });
    }

    public boolean isKeyPressed(int key){
        return glfwGetKey(window.getWindowHandle(), key) == GLFW_PRESS;
    }

    public float setOnKeyPressed(int key, float val){
        return  glfwGetKey(window.getWindowHandle(), key) == GLFW_PRESS ? val : 0;
    }

    public float setOnKeyPressed(int key,int keyOpposite, float val, float otherVal){
        if (isKeyPressed(key)) return val;
        else if (isKeyPressed(keyOpposite)) return otherVal;
        else return 0;
    }
}
