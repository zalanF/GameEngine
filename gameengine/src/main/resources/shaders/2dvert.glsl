#version 330 core

layout (location = 0) in vec2 aPos; //aPos = attribute position
//layout (location = 1) in vec3 aColour; 

//out vec3 colour;
//vec3 pos;
//uniform mat3 translate;
uniform vec2 u_translate;
uniform mat2 u_transform;

void main()
{
    //gl_Position = vec4(aPos, 1.0);
    //pos = translate * vec3(aPos, 1.0);
    //pos.z = 0;
    gl_Position = vec4(u_transform * (aPos+u_translate), 0.0, 1.0);
}