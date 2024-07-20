#version 330 core

//in vec3 colour;

uniform vec3 u_colour;

out vec4 FragColor;

void main()
{
    //FragColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
    gl_FragColor = vec4(u_colour, 1.0);
} 
