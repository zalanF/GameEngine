#version 330 core

in vec3 colour;

out vec4 FragColor;

void main()
{
    //FragColor = vec4(1.0f, 1.0f, 1.0f, 1.0f);
    FragColor = vec4(colour, 1.0);
} 
