package com.schackteleers.projectrpg.engine.graphics;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author Stijn Schack
 * @since 27/04/2017
 */
class ShaderProgram {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    ShaderProgram() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not Create Shader");
        }
    }

    private int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    void link() throws Exception {
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Could not link shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }

        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    void bind(){
        glUseProgram(programId);
    }

    void unbind(){
        glUseProgram(0);
    }

    void cleanUp(){
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
