#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 normal;
    vec2 texKoords;
    vec3 toCamera;
    vec3 toPointLight;
} vertexData;

//uniforms!
//material
uniform sampler2D emit_tex;
uniform sampler2D diff_tex;
uniform sampler2D spec_tex;
uniform float shininess;
//pointlight
uniform vec3 point_color;
uniform vec3 point_attenuation;

//fragment shader output
out vec4 color;

vec3 phong(vec3 n, vec3 l, vec3 v, vec3 diff_c, vec3 spec_c, float shine)
{
    float cosa = max(dot(n, l), 0.0f);
    float cosb = max(dot(reflect(-l, n), v), 0.0f);
    return diff_c * cosa + spec_c * pow(cosb, shine);
}

float attenuation(float distance, vec3 att_params)
{
    return 1.0f / (att_params.x + att_params.y * distance + att_params.z * distance * distance);
}

void main(){
    //Normalisierter Scheißdreck
    vec3 N = normalize(vertexData.normal);
    vec3 V = normalize(vertexData.toCamera);
    float DP = length(vertexData.toPointLight);
    vec3 LP = vertexData.toPointLight/DP;
    //Farbwerte der Texturen
    vec3 emit_c = texture(emit_tex, vertexData.texKoords).rgb;
    vec3 diff_c = texture(diff_tex, vertexData.texKoords).rgb;
    vec3 spec_c = texture(spec_tex, vertexData.texKoords).rgb;
    //Ankommende Lichtintensität
    vec3 LIP = point_color * attenuation(DP, point_attenuation);
    //Emmissiver Part
    vec3 res = emit_c;
    //Ambient Part
    res += point_color * 0.1f * diff_c;
    //Lichter
    res += phong(N, LP, V, diff_c, spec_c, shininess) * LIP;
    color = vec4(floor(res[0]*10)/10, floor(res[1]*10)/10, floor(res[2]*10)/10, 1.0f);
    //color = vec4(res, 1.0f);
}



/*uniform vec3 lightDir;
varying vec3 normal;

void main()
{
    float intensity;
    vec4 color;
    intensity = dot(lightDir,normal);

    if (intensity > 0.95)
    color = vec4(1.0,0.5,0.5,1.0);
    else if (intensity > 0.5)
    color = vec4(0.6,0.3,0.3,1.0);
    else if (intensity > 0.25)
    color = vec4(0.4,0.2,0.2,1.0);
    else
    color = vec4(0.2,0.1,0.1,1.0);
    gl_FragColor = color;

}*/