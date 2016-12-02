#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform vec4 xColor;
uniform float textured;
uniform sampler2D xTexture;

void main()
{
   /* vec4 textureSample = texture(xTexture, xTexCoord);
    fragColor = vec4(exColour, 1.0);*/
    if( textured == 1.0){
        fragColor = texture(xTexture, outTexCoord);
    }else{
         fragColor = xColor;
    }

}