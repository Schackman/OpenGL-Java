package com.schackteleers.projectrpg.engine.graphics;

import com.schackteleers.projectrpg.engine.gameobjects.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author Stijn Schack
 * @since 27/04/2017
 */
public class Transformation {
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f modelViewMatrix;

    public Transformation() {
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.modelViewMatrix = new Matrix4f();
    }

    public Matrix4f getProjectionMatrix(int width, int height) {
        return projectionMatrix.setOrtho2D(-width / 2, width / 2, -height / 2, height / 2).scale(32);
    }

    public Matrix4f getViewMatrix(Camera camera){
        Vector3f camPos = camera.getPosition();
        float camRot = camera.getRotation();

        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camRot), new Vector3f(0, 0, 1));
        viewMatrix.translate(-camPos.x, -camPos.y, -camPos.z);
        return viewMatrix;
    }

    public final Matrix4f getModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
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
