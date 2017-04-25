package com.schackteleers.projectrpg.engine.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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

    private final int vertexCount;

    private static final int VBO_NONE = 0;
    private static final int VBO_POSITIONS = 1;
    private static final int VBO_TEXTURE_COORDS = 2;

    public Mesh2D(final float[] positions, final float[] texCoords, final int[] indices){
        vertexCount = indices.length;
        vboIdList = new ArrayList<>();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Postitions VBO
        int vboId = GL15.glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer posBuffer = BufferUtils.createFloatBuffer(positions.length);
        posBuffer.put(positions).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(VBO_POSITIONS, 3, GL_FLOAT, false, 0, 0);

        // Texture coordinates VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer textCoordsBuffer = BufferUtils.createFloatBuffer(texCoords.length);
        textCoordsBuffer.put(texCoords).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(VBO_TEXTURE_COORDS, 2, GL_FLOAT, false, 0, 0);

        // Index VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render(){
        // Draw Mesh
        glBindVertexArray(this.vaoId);
        glEnableVertexAttribArray(VBO_POSITIONS);
        glEnableVertexAttribArray(VBO_TEXTURE_COORDS);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        // Restore State
        glDisableVertexAttribArray(VBO_POSITIONS);
        glDisableVertexAttribArray(VBO_TEXTURE_COORDS);
        glBindVertexArray(0);
    }

    public void cleanUp(){
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
