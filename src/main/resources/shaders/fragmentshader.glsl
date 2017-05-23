#version 330

in vec2 ex_texture_coord;
in vec3 mv_vertex_pos;
in vec3 mv_vertex_normal;
out vec4 frag_color;

struct Material
{
    vec3 color;
    int isTextured;
    float reflectance;
};

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

const int MAX_POINT_LIGHTS = 32;

uniform Material material;
uniform sampler2D texture_sampler;
uniform vec3 ambient_light;
uniform PointLight point_light_list[MAX_POINT_LIGHTS];
uniform float specular_power;

vec4 calcLightColor(vec3 light_color, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal)
{
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColor = vec4(light_color, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specular_power);
    specColor = light_intensity  * specularFactor * material.reflectance * vec4(light_color, 1.0);

    return (diffuseColor + specColor);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_color = calcLightColor(light.color, light.intensity, position, to_light_dir, normal);

    // Apply Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
        light.att.exponent * distance * distance;
    return light_color / attenuationInv;
}
void main()
{
    vec4 base_color;
    if ( material.isTextured != 1 )
    {
        base_color = vec4(material.color, 1);
    }
    else
    {
        base_color = texture(texture_sampler, ex_texture_coord);
    }

    vec4 totalLight = vec4(0, 0, 0, 1);
    for (int i=0; i<MAX_POINT_LIGHTS;i++){
        if(point_light_list[i].intensity > 0){
            totalLight += calcPointLight(point_light_list[i], mv_vertex_pos, mv_vertex_normal);
        }
    }
    frag_color = base_color * (vec4(ambient_light, 1) + totalLight);
}