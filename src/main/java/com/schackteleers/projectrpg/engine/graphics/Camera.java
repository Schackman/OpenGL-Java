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

    public Camera(float posX, float posY, float posZ, float rotX, float rotY, float rotZ) {
        position = new Vector3f(posX, posY, posZ);
        rotation = new Vector3f(rotX, rotY, rotZ);
    }

    public void move(float offsetX, float offsetY, float offsetZ) {
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

    Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    Vector3f getRotation() {
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
        if      (rotation.x < 0)    rotation.x += 360;
        else if (rotation.x > 360)  rotation.x -= 360;
        if      (rotation.y < 0)    rotation.y += 360;
        else if (rotation.y > 360)  rotation.y -= 360;
        else if (rotation.z < 0)    rotation.z += 360;
        else if (rotation.z > 360)  rotation.z -= 360;
    }
}
