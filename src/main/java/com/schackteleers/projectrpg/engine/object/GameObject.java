package com.schackteleers.projectrpg.engine.object;

import com.schackteleers.projectrpg.engine.graphics.Mesh2D;
import com.schackteleers.projectrpg.engine.graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;

/**
 * Contains a Mesh and transformation(position, rotation, scale)
 *
 * @author Stijn Schack
 * @since 25/04/2017
 */
public class GameObject {
    private Mesh2D mesh;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public GameObject() throws IOException {
        this(new Mesh2D());
    }

    public GameObject(Mesh2D mesh) {
        this.mesh = mesh;
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.scale = 1;
    }

    public GameObject setPosition(float x, float y) {
        this.position.set(x, y, 0);
        return this;
    }

    public GameObject setRotation(float x, float y, float z) {
        this.rotation.set(x, y, z);
        return this;
    }

    public GameObject setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public GameObject scale(float scale){
        this.scale += scale;
        return this;
    }

    public Mesh2D getMesh() {
        return mesh;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public void cleanUp(){
        mesh.cleanUp();
    }
}
