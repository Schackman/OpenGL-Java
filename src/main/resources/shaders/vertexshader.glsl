#version 330

layout (location=0) in vec2 position;
layout (location=1) in vec3 color;

out vec3 ex_color;

uniform mat4 projectionmatrix;
uniform mat4 modelviewmatrix;

void main()
{
    vec4 mv_position = modelviewmatrix * vec4(position, 0, 1);
    gl_Position = projectionmatrix * mv_position;

    ex_color = color;
}