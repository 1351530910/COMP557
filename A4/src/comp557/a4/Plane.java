package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Class for a plane at y=0.
 * 
 * This surface can have two materials.  If both are defined, a 1x1 tile checker 
 * board pattern should be generated on the plane using the two materials.
 */
public class Plane extends Intersectable {
    
	/** The second material, if non-null is used to produce a checker board pattern. */
	Material material2;
	
	/** The plane normal is the y direction */
	public static final Vector3d n = new Vector3d( 0, 1, 0 );
    
    /**
     * Default constructor
     */
    public Plane() {
    	super();
    }

        
    @Override
    public void intersect( Ray ray, IntersectResult result ) {
    
        // TODO: Objective 4: intersection of ray with plane
    	
    	double t = -ray.eyePoint.y/ray.viewDirection.y;
    	
    	if (t>Epsilon&&t<result.t) {
			result.n = n;
			result.t = t;
			result.p = new Point3d(v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t)));
			int x = result.p.x>=0 ? (int)result.p.x:(int)result.p.x+1;
			int z = result.p.z+0.5>=0 ? (int)(result.p.z+0.5):(int)(result.p.z-0.5);
			if (Math.abs(x%2) == Math.abs(z%2)) {
				result.material = material;
			}else {
				result.material = material2;
			}
			
			
		}
    }
    
}
