/*
#version 400

in vec3 vp;
uniform mat4 modelViewMatrix, projectionMatrix;
out vec3 texcoords;

void main() {
  texcoords = vp;

  vec4 mvPos = modelViewMatrix * vec4(vp, 1.0);
  gl_Position = projectionMatrix * mvPos;

//  gl_Position = projectionMatrix * modelViewMatrix * vec4(vp, 1.0);
}*/

#version 330

#define M_PI 3.1415926535897932384626433832795

in vec3 vertex;
out vec3 texCoord;

uniform mat4 modelViewMatrix, projectionMatrix;
uniform float timef;
uniform int maxtime;

float map(float OldValue, float OldMin, float OldMax, float NewMin, float NewMax)
{
    return (((OldValue - OldMin) * (NewMax - NewMin)) / (OldMax - OldMin)) + NewMin;
}

void main() {

    /*float angle = map(timef, 0, maxtime, 0, 360) * (M_PI/180);

    float s = sin(angle);
    float c = cos(angle);

    mat4 rotMatix = mat4 (1, 0, 0,  0,
                          0, c, -s, 0,
                          0, s,  c, 0,
                          0, 0, 0,  1);

	gl_Position = projectionMatrix * modelViewMatrix * rotMatix * vec4(vertex, 1.0);
	texCoord = vertex;*/
	gl_Position = projectionMatrix * modelViewMatrix * vec4(vertex, 1.0);
    	texCoord = vertex;
}