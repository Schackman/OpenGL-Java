package com.schackteleers.projectrpg.engine.graphics;

import org.joml.Vector3f;

/**
 * @author Stijn Schack
 * @since 27/04/2017
 */
public class Camera {
    private Vector3f position;
    private Vector3f rotation;

    public Camera() {
        position = new Vector3f();
        rotation = new Vector3f();
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if (offsetX != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Camera setRotation(float rotX, float rotY, float rotZ) {
        this.rotation.set(rotX, rotY, rotZ);
        return this;
    }

    public void translate(float x, float y, float z) {
        this.position.add(x, y, z);
    }

    public void rotate(float rotX, float rotY, float rotZ) {
        this.rotation.add(rotX, rotY, rotZ);
    }
}
