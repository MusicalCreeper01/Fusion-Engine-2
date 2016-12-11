#version 150 core

in vec3 vcolor;

//in vec4 vPosition;
//in vec3 vNormal;

out vec4 outColor;
out vec4 outDepthNormal;

void main()
{
    outColor.rgb = vec3((gl_FragCoord.z / gl_FragCoord.w)*10);
    outColor.a = 1.0;
    //outColor = gl_color; //vec4( vPosition.z, vPosition.z, vPosition.z, 1.0);
    //outDepthNormal = vec4( vPosition.z, normalize( vNormal ));
}