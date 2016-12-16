/*
#version 400

in vec3 texcoords;
uniform samplerCube cube_texture;
out vec4 frag_colour;

void main() {
  frag_colour = vec4(1, 0, 0, 1);//texture(cube_texture, texcoords);
}*/

#version 330

in vec3 texCoord;
out vec4 fragColor;
uniform samplerCube cubemap;

void main (void) {
	fragColor = texture(cubemap, texCoord);
}