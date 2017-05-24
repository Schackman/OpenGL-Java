package com.schackteleers.projectrpg.engine.graphics;

import com.schackteleers.projectrpg.engine.fileio.FileIO;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * @author Stijn Schack
 * @since 5/3/2017
 */
public class Texture {

    private final int id;

    private static final Map<String, Integer> loadedTextures = new HashMap<>();

    public Texture(String fileName) throws IOException {
        fileName = "/textures/" + fileName + ".png";
        this.id = loadTexture(fileName);
    }

    private static int loadTexture(String fileName) throws IOException {
        // Don't load texture if already loaded
        if (loadedTextures.containsKey(fileName))
            return loadedTextures.get(fileName);

        int textureId = glGenTextures();
        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer av = stack.mallocInt(1);

            ByteBuffer decodedImage = stbi_load_from_memory(FileIO.loadResourceToByteBuffer(fileName, 1024), w, h, av, 4);

            glBindTexture(GL_TEXTURE_2D, textureId);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(), h.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, decodedImage);
            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D);

        }
        loadedTextures.put(fileName, textureId);
        return textureId;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    int getId() {
        return id;
    }

    void cleanup() {
        glDeleteTextures(id);
    }
}
