package com.schackteleers.projectrpg.engine.graphics;

import org.joml.Vector3f;

/**
 * @author Stijn Schack
 * @since 27/04/2017
 */
public class Camera {
    private Vector3f position;
    private float rotation;

    public Camera() {
        position = new Vector3f();
        rotation = 0;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void translate(float x, float y, float z) {
        this.position.add(x, y, z);
    }

    public void rotate(float rotation){
        this.rotation+=rotation;
    }
}
