#version 120
varying vec4 Vertex_UV;
uniform mat4 gxl3d_ModelViewProjectionMatrix;
void main()
{
    //gl_Position = gxl3d_ModelViewProjectionMatrix * gl_Vertex;
    gl_Position = pos;
    Vertex_UV = gl_MultiTexCoord0;
}