#version 330

in  vec2 Texcoord;
out vec4 outColor;

uniform sampler2D colorbuffer;
uniform sampler2D depthbuffer;

uniform float timef;
uniform int maxtime;

float map(float OldValue, float OldMin, float OldMax, float NewMin, float NewMax)
{
    return (((OldValue - OldMin) * (NewMax - NewMin)) / (OldMax - OldMin)) + NewMin;
}

void main()
{

    /*if(texture(depthbuffer, Texcoord).r < 1){
        outColor.rgb = texture(colorbuffer, Texcoord).rgb * map(timef, 0, maxtime, 0.3, 1.0);//0.3;
    }else{
        outColor.rgb = texture(colorbuffer, Texcoord).rgb;
    }*/


    outColor.rgb = texture(colorbuffer, Texcoord).rgb;
    //gamma correction
    /*float gamma = 2.2;
    outColor.rgb = pow(outColor.rgb, vec3(1.0/gamma));*/



}
