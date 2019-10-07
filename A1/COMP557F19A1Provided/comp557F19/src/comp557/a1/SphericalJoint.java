package comp557.a1;

import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

public class SphericalJoint extends GraphNode {

	DoubleParameter angleX;
    DoubleParameter angleY;
    DoubleParameter angleZ;

	Vector3d translation;
		
	public SphericalJoint( String name ,Vector3d translation) {
		super(name);
        dofs.add(angleX = new DoubleParameter("angleX", 0, -180, 180));
        dofs.add(angleY = new DoubleParameter("angleY", 0, -180, 180));
        dofs.add(angleZ = new DoubleParameter("angleZ", 0, -180, 180));

		this.translation = translation;
		if (translation==null) {
			this.translation = new Vector3d(0,0,0);
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		
		gl.glTranslated(translation.x, translation.y, translation.z);
        gl.glRotated(angleX.getValue(),1,0,0);
        gl.glRotated(angleY.getValue(),0,1,0);
        gl.glRotated(angleZ.getValue(),0,0,1);
		
		super.display(drawable);
		gl.glPopMatrix();
		
	}
}
