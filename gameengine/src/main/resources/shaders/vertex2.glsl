#version 330 core

layout (location = 0) in vec3 aPos; //aPos = attribute position
//layout (location = 1) in vec3 aColour; 

out vec3 colour;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    colour = vec3(gl_Position.x, gl_Position.y, gl_Position.z);
}
