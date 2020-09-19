#version 330 core


//Vertex attributes
layout(location = 0) in vec3 position;

//matrix uniforms
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

//output
out vec3 cmCoords;

void main()
{
    cmCoords = position;
    gl_Position = projection_matrix * view_matrix * vec4(position, 1.0f);
}