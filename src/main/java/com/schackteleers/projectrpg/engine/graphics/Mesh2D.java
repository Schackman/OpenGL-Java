package com.schackteleers.projectrpg.engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
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
    private static final int VBO_COLORS = 1;

    private final int vertexCount;

    private static float[] vertices = {
            -0.5f, 0.5f, // Top Left
            -0.5f, -0.5f, // Bottom Left
            0.5f, 0.5f, // Top Right
            0.5f, -0.5f // Bottom Right
    };

    private static float[] colors = {
            1.0f, 1.0f, 1.0f, //White
            1.0f, 0.0f, 0.0f, //Red
            1.0f, 0.0f, 0.0f, //Red
            1.0f, 0.0f, 0.0f //Red
    };

    private static int[] indices = {0, 1, 2, 2, 1, 3};


    /**
     * Creates a basic rectangle
     */
    public Mesh2D() {
        this(vertices, colors, indices);
    }


    private Mesh2D(final float[] vertices, final float[] colors, final int[] indices) {
        vboIdList = new ArrayList<>();
        vertexCount = indices.length;
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

        //Color VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer colorsBuffer = memAllocFloat(colors.length);
        colorsBuffer.put(colors).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(VBO_COLORS, 3, GL_FLOAT, false, 0, 0);


        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    void render() {
        // Bind VAO
        glBindVertexArray(this.vaoId);

        // Enable VBO's
        glEnableVertexAttribArray(VBO_POSITIONS);
        glEnableVertexAttribArray(VBO_COLORS);

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
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
