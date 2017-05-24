package com.schackteleers.projectrpg.engine.graphics;

import com.schackteleers.projectrpg.engine.gameobjects.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author Stijn Schack
 * @since 27/04/2017
 */
class Transformation {
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f modelViewMatrix;

    Transformation() {
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.modelViewMatrix = new Matrix4f();
    }

    Matrix4f getProjectionMatrix(int width, int height, double dpi) {
        return projectionMatrix.setPerspective((float) Math.toRadians(60), (float)width/height, 0.001f, 1000);
    }

    final Matrix4f getViewMatrix(Camera camera) {
        Vector3f camPos = camera.getPosition();
        Vector3f camRot = camera.getRotation();

        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camRot.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(camRot.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(camRot.z), new Vector3f(0, 0, 1));
        viewMatrix.translate(-camPos.x, -camPos.y, -camPos.z);
        return viewMatrix;
    }

    final Matrix4f getModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
        Vector3f rotation = gameObject.getRotation();
        modelViewMatrix.identity().translate(gameObject.getPosition())
                .rotateX((float) Math.toRadians(-rotation.x))
                .rotateY((float) Math.toRadians(-rotation.y))
                .rotateZ((float) Math.toRadians(-rotation.z))
                .scale(gameObject.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }
}
