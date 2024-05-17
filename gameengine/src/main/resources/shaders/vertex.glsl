#version 330 core

layout (location = 0) in vec3 aPos;   //attribute position
layout (location = 1) in vec3 aColor; //attribute color
layout (location = 2) in vec2 aTexCoord; //attribute texture coordinates

out vec3 ourColor;
out vec2 TexCoord;

//uniform vec3 offset;

//uniform mat4 transform; //not using this right now
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    ourColor = aColor;
    TexCoord = aTexCoord;
}