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

uniform sampler2D texFramebuffer;
uniform sampler2D gui_texture;

void main()
{
    vec4 gui = texture(gui_texture, Texcoord);
    vec4 frame = texture(texFramebuffer, Texcoord);
    outColor = vec4(mix(frame.rgb, gui.rgb, gui.a), gui.a);//texture(texFramebuffer, Texcoord) + texture(gui_texture, Texcoord) * texture(gui_texture, Texcoord).a;
}