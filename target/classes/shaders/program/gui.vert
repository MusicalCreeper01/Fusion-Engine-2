#version 330

in vec3 position;

//uniform mat4 projectionMatrix;

void main()
{
//    gl_Position = projectionMatrix * vec4(position, 0);
    gl_Position = vec4(position, 0);
}