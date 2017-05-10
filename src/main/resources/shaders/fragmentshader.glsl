#version 330

in vec2 ex_texture_coord;
out vec4 frag_color;

uniform sampler2D texture_sampler;
uniform vec3 ambient_light;

/*
struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 colour;
    // Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};
*/

void main()
{
    frag_color = vec4(ambient_light, 1) * texture(texture_sampler, ex_texture_coord);
}