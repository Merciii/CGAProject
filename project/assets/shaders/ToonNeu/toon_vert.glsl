#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textur_koords;
layout(location = 2) in vec3 normale;

//uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;
uniform vec2 tcMultiplier;
uniform vec3 point_position;

out struct VertexData
{
    vec3 normal;
    vec2 texKoords;
    vec3 toCamera;
    vec3 toPointLight;
} vertexData;

void main()
{
    mat4 model_view = view_matrix * model_matrix;
    vec4 pos = model_view * vec4(position, 1.0f);
    gl_Position = projection_matrix * pos;
    vertexData.normal = (inverse(transpose(model_view)) * vec4(normale, 0.0)).xyz;

    vertexData.toCamera= -pos.xyz;

    vec3 vpoint = (view_matrix * vec4(point_position, 1.0f)).xyz;
    vertexData.toPointLight = vpoint - pos.xyz;

    vertexData.texKoords = textur_koords * tcMultiplier;
}
