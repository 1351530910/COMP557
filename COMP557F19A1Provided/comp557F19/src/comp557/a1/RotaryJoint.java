package comp557.a1;

import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

public class RotaryJoint extends GraphNode {

	DoubleParameter angle;

	Vector3d axis;
	Vector3d translation;
		
	public RotaryJoint( String name ,Vector3d axis,Vector3d translation) {
		super(name);
		dofs.add( angle = new DoubleParameter("angle", 0, -180, 180));
		this.axis = axis;
		this.axis.normalize();
		this.translation = translation;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		
		gl.glTranslated(translation.x, translation.y, translation.z);
		gl.glRotated(angle.getValue(),axis.x,axis.y,axis.z);
		
		super.display(drawable);
		gl.glPopMatrix();
		
	}
}
