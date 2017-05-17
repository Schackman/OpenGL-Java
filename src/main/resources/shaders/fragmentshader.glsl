#version 330

in vec2 ex_texture_coord;
in vec3 mv_vertex_pos;
in vec3 mv_vertex_normal;
out vec4 frag_color;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
    vec3 position;
    float intensity;
    Attenuation att;
};

uniform sampler2D texture_sampler;
uniform vec3 ambient_light;
uniform PointLight point_light;

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
 {
     // Attenuation
     vec3 light_direction = light.position - position;
     float distance = length(light_direction);
     float attenuationInv = light.att.constant + light.att.linear * distance +
         light.att.exponent * distance * distance;
     return vec4(light.color * light.intensity, 1.0) / attenuationInv;
 }
void main()
{
    vec4 diffuseSpecularComp = calcPointLight(point_light, mv_vertex_pos, mv_vertex_normal);
    frag_color = (vec4(ambient_light, 1.0)+ diffuseSpecularComp) * texture(texture_sampler, ex_texture_coord);
}