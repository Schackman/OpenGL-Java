package com.schackteleers.projectrpg.engine.graphics;

import com.schackteleers.projectrpg.engine.graphics.light.PointLight;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author Stijn Schack
 * @since 27/04/2017
 */
class ShaderProgram {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    ShaderProgram() throws Exception {
        this.uniforms = new HashMap<>();
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

    void bind() {
        glUseProgram(programId);
    }

    void unbind() {
        glUseProgram(0);
    }

    void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform: " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    void setUniform(String uniformName, Matrix4f value) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        value.get(fb);
        glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
    }

    void setUniform(String uniformName, int value){
        glUniform1i(uniforms.get(uniformName), value);
    }

    void setUniform(String uniformName, Vector3f value){
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".color", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(uniformName + ".att.constant", att.getConstant());
        setUniform(uniformName + ".att.linear", att.getLinear());
        setUniform(uniformName + ".att.exponent", att.getExponent());
    }


    private void setUniform(String uniformName, Vector2f value) {
        glUniform2f(uniforms.get(uniformName), value.x, value.y);
    }

    private void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    void cleanUp() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
