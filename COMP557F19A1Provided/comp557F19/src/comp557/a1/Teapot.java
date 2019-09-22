package comp557.a1;

import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * Teapot
 */
public class Teapot extends GeometricNode{

    public Teapot(String name,Vector3d position,Vector3d orientation,Vector3d scaling) {
        super(name,position,orientation,scaling);
    }

    @Override
    public void drawForm() {

		glut.glutSolidTeapot(1);
    }
}