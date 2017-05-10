package com.schackteleers.projectrpg.engine.graphics;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * Basic 2D Mesh class.
 * contains core data and functionality to render any 2D shape from triangles
 *
 * @author Stijn Schack
 * @since 25/04/2017
 */
public class Mesh2D {
    private final int vaoId;
    private final List<Integer> vboIdList;

    private static final int VBO_POSITIONS = 0;
    private static final int VBO_TEXTURE_COORDS = 1;

    private final int vertexCount;

    private Texture texture;

    private static float[] vertices = {
            -0.5f, 0.5f, // Top Left
            -0.5f, -0.5f, // Bottom Left
            0.5f, 0.5f, // Top Right
            0.5f, -0.5f // Bottom Right
    };

    private static float[] textureCoords = {
            0.0f, 0.0f, //V0 Top Left
            0.0f, 1.0f, //V1 Bottom Left
            1.0f, 0.0f, //V2 Top Right
            1.0f, 1.0f  //V3 Bottom Right
    };

    private static int[] indices = {0, 1, 2, 2, 1, 3};


    /**
     * Creates a basic rectangle
     */
    public Mesh2D() throws IOException {
        this(vertices, textureCoords, indices, new Texture("/textures/mario.png"));
    }

    private Mesh2D(final float[] vertices, final float[] textureCoords, final int[] indices, Texture texture) {
        vboIdList = new ArrayList<>();
        vertexCount = indices.length;
        this.texture = texture;

        //Create VAO and bind
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Vertices VBO
        int vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer verticesBuffer = memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(VBO_POSITIONS, 2, GL_FLOAT, false, 0, 0);
        memFree(verticesBuffer);

        //Indices VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        IntBuffer indicesBuffer = memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        memFree(indicesBuffer);

        //Texture VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer textureCoordBuffer = memAllocFloat(textureCoords.length);
        textureCoordBuffer.put(textureCoords).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, textureCoordBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(VBO_TEXTURE_COORDS, 2, GL_FLOAT, false, 0, 0);
        memFree(textureCoordBuffer);


        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    void render() {
        // Bind VAO
        glBindVertexArray(this.vaoId);

        // Enable VBO's
        glEnableVertexAttribArray(VBO_POSITIONS);
        glEnableVertexAttribArray(VBO_TEXTURE_COORDS);

        // Activate texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getId());

        // Draw vertices
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        // Restore State
        glDisableVertexAttribArray(VBO_POSITIONS);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete VBO's
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);

        // Delete Texture
        texture.cleanup();
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Texture getTexture() {
        return texture;
    }
}
