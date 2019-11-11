#version 330 core

in vec3 camSpacePosition;
in vec3 camSpaceNormal;
in float utv;
in float phiv;

uniform vec3 lightCamSpacePosition;
uniform vec3 lightColor;
uniform vec3 materialDiffuse;
uniform vec3 phong;
uniform float s;

#define PhongExp 64

void main(void) {
	
	vec3 v = normalize(-camSpacePosition);
	vec3 n = normalize(camSpaceNormal);
	vec3 l = normalize(lightCamSpacePosition - camSpacePosition);

	// TODO: 4, 11 Implement your GLSL per fragement lighting, heat colouring, and distance stripes here!
	vec3 bisector = normalize(v+l);


	// can use this to initially visualize the normal	
    gl_FragColor = vec4(vec3(1,0,0)*utv+ vec3(0,0,1)*(1-utv)+materialDiffuse+phong*lightColor*pow(max(0,dot(n,bisector)),s), 1 );
	//gl_FragColor = vec4(utv,utv,utv,1);
}
