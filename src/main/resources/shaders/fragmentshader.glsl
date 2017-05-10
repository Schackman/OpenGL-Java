#version 330

in vec2 ex_texture_coord;
out vec4 frag_color;

uniform sampler2D texture_sampler;

void main()
{
    frag_color = texture(texture_sampler, ex_texture_coord);
}