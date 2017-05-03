#version 330

layout (location=0) in vec2 position;
layout (location=1) in vec2 texture_coord;

out vec2 ex_texture_coord;

uniform mat4 projectionmatrix;
uniform mat4 modelviewmatrix;

void main()
{
    vec4 mv_position = modelviewmatrix * vec4(position, 0.0, 1.0);
    gl_Position = projectionmatrix * mv_position;
    ex_texture_coord = texture_coord;
}