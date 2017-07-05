

uniform mat4 transformation;

varying vec2 point;

void main(void){

	//gl_Position = /*transformation * */
	gl_Position = vec4(point*0.5, -0.5, 1);

}
