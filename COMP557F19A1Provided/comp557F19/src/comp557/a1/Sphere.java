package comp557.a1;

import javax.vecmath.Vector3d;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * Sphere
 */
 public class Sphere extends GeometricNode {
    public Sphere(String name,Vector3d position,Vector3d orientation,Vector3d scaling) {
        super(name,position,orientation,scaling);
    }
     
    @Override
    public void drawForm() {
		
		glut.glutSolidSphere(1, 20, 20);
    }
 }