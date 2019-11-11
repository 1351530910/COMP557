#version 330 core

in vec3 camSpacePosition;
in vec3 camSpaceNormal;
in float utv;
in float phiv;

uniform vec3 lightCamSpacePosition;
uniform vec3 lightColor;
uniform vec3 materialDiffuse;
uniform float shininess;

void main(void) {
	
	vec3 v = normalize(-camSpacePosition);
	vec3 n = normalize(camSpaceNormal);
	vec3 l = normalize(lightCamSpacePosition - camSpacePosition);

	// TODO: 4, 11 Implement your GLSL per fragement lighting, heat colouring, and distance stripes here!
	vec3 diffuse =  materialDiffuse*lightColor*max(0,dot(n,l));
	vec3 specular = lightColor * pow(max(0,dot(n,normalize(v+l))),shininess);
	// can use this to initially visualize the normal	
	

    gl_FragColor = vec4(diffuse+specular,1);
	
}
