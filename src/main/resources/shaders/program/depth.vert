#version 150 core
#extension GL_ARB_explicit_attrib_location : enable

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

layout (location = 0) in vec4 aPosition;
layout (location = 2) in vec3 aNormal;

out vec4 vPosition;
out vec3 vNormal;

// Vertex shader.
void main()
{
    vec4 mvPos = modelViewMatrix * aPosition;
    gl_Position = projectionMatrix * mvPos;

    vPosition = modelViewMatrix * aPosition;
    vNormal = mat3( modelViewMatrix ) * aNormal;
}
