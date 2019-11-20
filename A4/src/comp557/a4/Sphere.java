package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A simple sphere class.
 */
public class Sphere extends Intersectable {
    
	/** Radius of the sphere. */
	public double radius = 1;
    
	/** Location of the sphere center. */
	public Point3d center = new Point3d( 0, 0, 0 );
    
    /**
     * Default constructor
     */
    public Sphere() {
    	super();
    }
    
    /**
     * Creates a sphere with the request radius and center. 
     * 
     * @param radius
     * @param center
     * @param material
     */
    public Sphere( double radius, Point3d center, Material material ) {
    	super();
    	this.radius = radius;
    	this.center = center;
    	this.material = material;
    }
    
    @Override
    public void intersect( Ray ray, IntersectResult result ) {
    
        // TODO: Objective 2: intersection of ray with sphere
    	Vector3d oc = v3d.minus(ray.eyePoint, center);
    	double a = v3d.dot(ray.viewDirection, ray.viewDirection);
    	double b = v3d.dot(oc, ray.viewDirection);
    	double c = v3d.dot(oc, oc)-radius*radius;
    	double discriminant = b*b-a*c;
    	if (discriminant>0) {
    		//find the two intersection
			double t1 = (-b - Math.sqrt(discriminant)) / a;
			double t2 = (-b + Math.sqrt(discriminant)) / a;
			
			//the closest
			double tmin = t1<t2 ? t1:t2;
			
			if (tmin<result.t&&tmin>mindist) {
				result.t = t1;
				result.p = new Point3d(v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t1)));
				result.n = v3d.times(v3d.minus(result.p, center),1.0/radius);
				result.material = material;
			}
		}
	
    }
    
}
