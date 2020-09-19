#version 330 core


//Vertex attributes
layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec3 normal;

//matrix uniforms
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

//Vertex shader output

out struct VertexData
{
	vec3 pos;
	vec2 uv;
	vec3 normal;
	mat4 model_view_matrix;
} vertexdata;

void main()
{
    gl_Position = projection_matrix * view_matrix * model_matrix * vec4(position, 1.0f);

    mat4 model_view = view_matrix * model_matrix;

    vertexdata.pos = (model_view * vec4(position, 1.0f)).xyz; //position in view space
    vertexdata.uv = uv;
    vertexdata.normal = mat3(transpose(inverse(model_view))) * normal;
    vertexdata.model_view_matrix = model_view;
}