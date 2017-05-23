#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texture_coord;
layout (location=2) in vec3 vertex_normal;

out vec2 ex_texture_coord;
out vec3 mv_vertex_pos;
out vec3 mv_vertex_normal;

uniform mat4 projectionmatrix;
uniform mat4 modelviewmatrix;

void main()
{
    vec4 mv_position = modelviewmatrix * vec4(position, 1.0);
    gl_Position = projectionmatrix * mv_position;
    ex_texture_coord = texture_coord;
    mv_vertex_normal = normalize(modelviewmatrix * vec4(vertex_normal, 0.0)).xyz;
    mv_vertex_pos = mv_position.xyz;
}