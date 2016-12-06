#version 330

in  vec2 Texcoord;
out vec4 outColor;

uniform sampler2D colorbuffer;
uniform sampler2D depthbuffer;

void main()
{
//    outColor = texture(texFramebuffer, Texcoord);
    outColor = texture(colorbuffer, Texcoord);
    //outColor.rgb = texture(texFramebuffer, Texcoord).rrr;//vec3(gl_FragCoord.z, gl_FragCoord.z, gl_FragCoord.z);//texture(texFramebuffer, Texcoord).rgb;// - vintage look
}
