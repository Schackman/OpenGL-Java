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

vec4 calcPointLight(PointLight light, vec3 vertex_position, vec3 normal)
 {
     // Diffuse Light
     vec3 light_direction = light.position - vertex_position;
     vec3 to_light_source = normalize(light_direction);
     float diffuseFactor = max(dot(normal, to_light_source), 0.0);
     vec4 diffuseColour = vec4(light.color, 1.0) * light.intensity;

     // Attenuation
     float distance = length(light_direction);
     float attenuationInv = light.att.constant + light.att.linear * distance +
         light.att.exponent * distance * distance;

     return diffuseColour / attenuationInv;
 }
void main()
{
    vec4 pointLight = calcPointLight(point_light, mv_vertex_pos, mv_vertex_normal);
    frag_color = texture(texture_sampler, ex_texture_coord) * (vec4(ambient_light, 1) + pointLight);
}