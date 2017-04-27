package com.schackteleers.projectrpg.engine.object;

import com.schackteleers.projectrpg.engine.graphics.Mesh2D;
import org.joml.Vector2f;

/**
 * @author Stijn Schack
 * @since 25/04/2017
 */
public class GameObject {
    private Mesh2D mesh;
    private Vector2f position;
    private Vector2f rotation;
    private float scale;

    public GameObject(){
        this(new Mesh2D());
    }

    public GameObject(Mesh2D mesh) {
        this.mesh = mesh;
        this.position = new Vector2f();
        this.rotation = new Vector2f();
        this.scale = 1;
    }

    public GameObject setPosition(float x, float y) {
        this.position.set(x, y);
        return this;
    }

    public GameObject setRotation(float x, float y) {
        this.rotation.set(x, y);
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

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }
}
