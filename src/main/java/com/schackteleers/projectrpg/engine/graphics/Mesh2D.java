package com.schackteleers.projectrpg.engine.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * @author Stijn Schack
 * @since 25/04/2017
 */
public class Mesh2D {
    private final int vaoId;
    private final List<Integer> vboIdList;

    private static final int VBO_POSITIONS = 0;
    private static final int VBO_TEXTURE_COORDS = 1;

    private static float[] vertices = {
            -0.5f, 0.5f, // Top Left
            -0.5f, -0.5f, // Bottom Left
            0.5f, 0.5f, // Top Right
            0.5f, 0.5f, // Top Right
            -0.5f, -0.5f, // Bottom Left
            0.5f, -0.5f // Bottom Right
    };

    private static float[] texCoords = {1};

    private static int[] indices = {0, 1, 2, 2, 1, 3};

    public Mesh2D() {
        this(vertices, texCoords, indices);
    }


    private Mesh2D(final float[] vertices, final float[] texCoords, final int[] indices) {
        vboIdList = new ArrayList<>();

        //Create VAO and bind
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Vertices VBO
        int vboId = GL15.glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(VBO_POSITIONS, 2, GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);

        if (verticesBuffer!=null){
            MemoryUtil.memFree(verticesBuffer);
        }
    }

    public void render() {
        // Bind VAO
        glBindVertexArray(this.vaoId);
        glEnableVertexAttribArray(VBO_POSITIONS);

        // Draw vertices
        glDrawArrays(GL_TRIANGLES, 0, vertices.length/2);

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
}
