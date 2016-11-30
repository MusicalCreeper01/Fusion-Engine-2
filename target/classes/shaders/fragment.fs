#version 330

in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sample;

void main()
{
    fragColor = texture(texture_sample, outTexCoord);
}
