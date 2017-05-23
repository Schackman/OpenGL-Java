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
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Basic 2D Mesh class.
 * contains core data and functionality to render any 2D shape from triangles
 *
 * @author Stijn Schack
 * @since 25/04/2017
 */
public class Mesh {
    private final int vaoId;
    private final List<Integer> vboIdList;

    private static final int VBO_POSITIONS = 0;
    private static final int VBO_TEXTURE_COORDS = 1;

    private final int vertexCount;

    private Texture texture;

    private static float[] vertices = {
            -0.5f, 0.50f, 0.5f,    // 0 Front Top Left sf
            -0.5f, -0.5f, 0.5f,    // 1 Front Bottom Left sf
            0.50f, 0.50f, 0.5f,    // 2 Front Top Right sf
            0.50f, -0.5f, 0.5f,    // 3 Front Bottom Right sf
            -0.5f, 0.50f, -0.50f,    // 4 Back Top Left tf
            0.50f, 0.50f, -0.50f,    // 5 Back Top Right sf
            0.50f, -0.5f, -0.50f,    // 6 Back Bottom Right sf
            -0.5f, 0.50f, 0.5f,    // 7 Front Top Left tf
            0.50f, 0.50f, -0.50f,    // 8 Back Top Right tf
            0.50f, 0.50f, 0.5f     // 9 Front Top Right tf
    };

    private static float[] textureCoords = {
            0.0f, 0.0f, //V0
            0.0f, 1.0f, //V1
            0.5f, 0.0f, //V2
            0.5f, 1.0f, //V3
            0.5f, 0.0f, //V4
            0.0f, 0.0f, //V5
            0.0f, 1.0f, //V6
            0.5f, 1.0f, //V7
            1.0f, 0.0f, //V8
            1.0f, 1.0f  //V9
    };

    private static int[] indices = {
            0, 1, 2, 2, 1, 3, // FTL, FBL, FTR, FTR, FBL, FBR (Front Face)
            2, 3, 5, 5, 3, 6,  // FTR, FBR, BTR, BTR, FBR, BBR (Right Face)
            4, 7, 8, 8, 7, 9 // BTL, FTL, BTR, BTR, FTL, FTR (Top Face)
    };


    /**
     * Creates a basic rectangle
     */
    public Mesh() throws IOException {
        this(new Texture("placeholder"));
    }

    public Mesh(Texture texture) throws IOException {
        this(vertices, textureCoords, indices, texture);
    }

    private Mesh(final float[] vertices, final float[] textureCoords, final int[] indices, Texture texture) {
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
        glVertexAttribPointer(VBO_POSITIONS, 3, GL_FLOAT, false, 0, 0);
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
