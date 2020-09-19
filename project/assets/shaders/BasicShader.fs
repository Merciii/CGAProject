#version 330 core

//input from vertex shader
in struct VertexData
{
	vec3 pos;
	vec2 uv;
	vec3 normal;
	mat4 model_view_matrix;
} vertexdata;

//material
#define MAX_TEXTURES 8
struct Material {
    vec4 diffusecolor;
    vec4 specularcolor;
    float shininess;
    float ambient;
    sampler2D tex[MAX_TEXTURES]; // 0: diffuse // 1: spec  // 2: emit
    int texcount;
};

uniform Material material;
uniform float uvMultiplier;

//lights
struct DirectionalLight
{
    vec4 lightColor;
    vec3 direction;
};

uniform DirectionalLight dirLight;


//fragment shader output
out vec4 color;

void main()
{
    vec4 diffcolor = texture(material.tex[0], vertexdata.uv * uvMultiplier);
    float specfactor = texture(material.tex[1], vertexdata.uv * uvMultiplier).r;
    vec4 emitcolor = texture(material.tex[2], vertexdata.uv * uvMultiplier);

    vec4 lightAccum = vec4(0.0f, 0.0f, 0.0f, 1.0f);
    //ambient
    lightAccum += material.ambient * dirLight.lightColor;

    //emit
    lightAccum += emitcolor;

    //diffuse
    lightAccum += max(dot(-normalize(mat3(vertexdata.model_view_matrix) * dirLight.direction), vertexdata.normal), 0.0f) * dirLight.lightColor;

    //specular
    vec3 L = -normalize(mat3(vertexdata.model_view_matrix) * dirLight.direction);
    vec3 N = vertexdata.normal;
    vec3 V = normalize(vec3(0.0f, 0.0f, 0.0f) - vertexdata.pos);
    vec3 H = normalize(V + L);

    lightAccum += pow(max(dot(N, H), 0.0f), material.shininess) * specfactor * dirLight.lightColor;


    color = lightAccum * diffcolor;
}