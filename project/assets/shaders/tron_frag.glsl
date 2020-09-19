#version 330 core

uniform sampler2D diff;
uniform sampler2D emit;
uniform sampler2D specular;
uniform float shininess;

uniform vec3 bikepointlight_col;
uniform vec3 spotlight_dir;
uniform float spot_inner;
uniform float spot_outer;
uniform vec3 spotlight_col;
uniform vec3 bikepointlight_attenuation;
uniform vec3 spotLightAttenuation;
uniform vec3 shadingcolor;


vec3 berechnungemit(vec3 diff, vec3 color){

    vec3 ambientEmit = diff *color;
    return ambientEmit;
}

vec3 berechnungdifspec(vec3 normal, vec3 lightdir, vec3 viewdir, vec3 diff, vec3 spec, float shininess) {

    float cosa = max(dot(normal, lightdir), 0.0f);
    vec3 diffuse =cosa *diff;

    vec3 reflect = normalize(reflect(-lightdir, normal));
    float cosbeta = max(0.0f, dot(viewdir, reflect));
    float cosBetaK = pow(cosbeta, shininess);
    vec3 specr = spec * cosBetaK;

    return specr +diffuse;
}

float berechnungattentuation(float distance, vec3 attentuationvalue){
    float attenuation = 1.0f/(attentuationvalue.x + attentuationvalue.y *distance + attentuationvalue.z * (distance *distance));

    return attenuation;
}

float berechnungintensity(vec3 lightdir, vec3 splightdir, float outercutoff, float cutoff){
    float theta =dot(normalize(lightdir), normalize(splightdir));
    float intensity = clamp((theta - outercutoff) / (cutoff -outercutoff), 0.0f, 1.0f);

    return intensity;
}


//input from vertex shader
in struct VertexData
{
// float laengesl;
// float laengepl;
    vec3 norm;
    vec2 tc;
    vec3 lightdir;
    vec3 surfaceNormal;
    vec3 lightdirsp;
    vec3 viewdir;
} vertexData;

//fragment shader output
out vec4 color;


void main(){
    vec3 normale = normalize(vertexData.norm);
    vec3 tocamera = normalize(vertexData.viewdir);
    vec3 tolight = normalize(vertexData.lightdir);


    vec3 texemit = texture(emit, vertexData.tc).rgb;
    vec3 texdiff = texture(diff, vertexData.tc).rgb;
    vec3 texspec = texture(specular, vertexData.tc).rgb;

    vec3 ambientemit = berechnungemit(texemit, shadingcolor);

    vec3 diffspecpl = berechnungdifspec(vertexData.norm, vertexData.lightdir, vertexData.viewdir, texdiff, texspec, shininess);
   // vec3 diffspecsl = berechnungdifspec(vertexData.norm, vertexData.lightdirsp, vertexData.viewdir, texdiff, texspec, shininess);

    float attenuationpl =  berechnungattentuation(length(vertexData.lightdir), bikepointlight_attenuation);
   // float attenuationspl =  berechnungattentuation(vertexData.laengesl, spotLightAttenuation);

 //   float internsitysl = berechnungintensity(vertexData.lightdirsp, spotlight_dir, spot_outer, spot_inner);

  //  vec3 spotcol =  internsitysl * attenuationspl * spotlight_col;
    vec3 pointcol =  attenuationpl * bikepointlight_col;

    //vec3 result = vec3((texemit) * (vec3(0, 1, 0) + spotcol + diffspecsl) *(pointcol + diffspecpl));
    vec3 result = vec3((texemit)  *(pointcol + diffspecpl));
    //  vec3 result = vec3((texemit  ) * (vec3(0,1,0) + spotcol + diffspecsl + pointcol) );
    color = vec4(result, 1.0f);


    //color = texture(emit,vertexData.tc );
}


