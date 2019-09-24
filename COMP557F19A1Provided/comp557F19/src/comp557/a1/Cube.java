package comp557.a1;

import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * Cube
 */
public class Cube extends GeometricNode{

    public Cube(String name,Vector3d position,Vector3d orientation, Vector3d scaling,Vector3d color) {
      super(name,position,orientation,scaling,color);
    }


    @Override
    public void drawForm() {
		
		  glut.glutSolidCube(1);
    }
}