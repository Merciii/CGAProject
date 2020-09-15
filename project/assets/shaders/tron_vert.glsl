#version 330 core

layout(location = 0) in vec3 position;
layout(location = 2) in vec3 normal;
layout(location = 1) in vec2 texcoords;


//uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;
uniform vec2 tcMultiplier ;
uniform vec3 spotlight_pos;
uniform vec3 bikepointlight_pos;
out struct VertexData
{
  //  float laengesl;
   // float laengepl;
    vec3 norm;
    vec2 tc;
    vec3 lightdir;
    vec3 surfaceNormal;
    vec3 lightdirsp;
    vec3 viewdir;
} vertexData;


void main(){


       mat4 modelview = view_matrix * model_matrix;
       vec4 pos = projection_matrix * modelview * vec4(position, 1.0f);


       gl_Position = pos;

       vertexData.norm =mat3(transpose(inverse(modelview))) * normal;
       vertexData.tc = texcoords * tcMultiplier;

       vec4 lpos = view_matrix * vec4(bikepointlight_pos,1.0f);
       vec4 p = (view_matrix * model_matrix * vec4(position,1.0f));
       vertexData.lightdir =(lpos-p).xyz;

       vec4 slpos =view_matrix *vec4(spotlight_pos,1.0f);
       vertexData.lightdirsp = normalize((slpos-p ).xyz);

       vertexData.viewdir =normalize(-p.xyz);

      // vertexData.laengesl = length(lpos.xyz - p.xyz);
       //vertexData.laengepl = length(slpos.xyz - p.xyz);


   }



