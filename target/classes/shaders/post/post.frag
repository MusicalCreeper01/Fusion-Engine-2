//#version 150
//in vec2 Texcoord;
//out vec4 outColor;
//uniform sampler2D texFramebuffer;
//void main()
//{
//    outColor = texture(texFramebuffer, Texcoord);
//}

#version 330

in  vec2 Texcoord;
out vec4 outColor;

uniform sampler2D colorbuffer;
uniform sampler2D depthbuffer;

/*float zfar = 255.0f;
float znear = 0.0f;

float linearize(float depth)
{
    return (-zfar * znear / (depth * (zfar - znear) - zfar)) / zfar;
}

out vec4 finalColor;
void main(void){
    float depth = texture2D(texFramebuffer, Texcoord).r;
    depth = linearize(depth);

    finalColor = vec4(depth, depth, depth, 1);
}*/

void main()
{
//    outColor = texture(texFramebuffer, Texcoord);
    outColor = texture(colorbuffer, Texcoord);
    //outColor.rgb = texture(texFramebuffer, Texcoord).rrr;//vec3(gl_FragCoord.z, gl_FragCoord.z, gl_FragCoord.z);//texture(texFramebuffer, Texcoord).rgb;// - vintage look
}
