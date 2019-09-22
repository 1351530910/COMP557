package comp557.a1;

import javax.vecmath.Vector3d;

import com.jogamp.opengl.*;
/**
 * for geometry node
 */
public abstract class GeometricNode extends GraphNode {

    Vector3d position;
    Vector3d color;
    Vector3d scaling;

    public GeometricNode(String name,Vector3d position, Vector3d scaling,Vector3d color) {
        super(name);
        
        this.position = position;
        this.color = color;
        this.scaling = scaling;
        
        if(position==null)
        this.position = new Vector3d(0,0,0);
        if (color==null) {
            this.color = new Vector3d(255,0,0);
        }
        if (scaling==null) {
            this.scaling = new Vector3d(1,1,1);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();

        gl.glPushMatrix();

        gl.glTranslated(position.x, position.y, position.z);
        gl.glScaled(scaling.x,scaling.y,scaling.z);
        gl.glColor3d(color.x,color.y,color.z);
        
        drawForm();

        super.display(drawable);

        gl.glPopMatrix();
    }

    public void drawForm(){
        
    }
}
