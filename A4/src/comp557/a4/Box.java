package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A simple box class. A box is defined by it's lower (@see min) and upper (@see max) corner. 
 */
public class Box extends Intersectable {

	public Point3d max;
	public Point3d min;
	
    /**
     * Default constructor. Creates a 2x2x2 box centered at (0,0,0)
     */
    public Box() {
    	super();
    	this.max = new Point3d( 1, 1, 1 );
    	this.min = new Point3d( -1, -1, -1 );
    }	

	@Override
	public void intersect(Ray ray, IntersectResult result) {
		// TODO: Objective 6: intersection of Ray with axis aligned box

		//assume that ray cannot hit a surface with normal same direction than ray
		

		double ty = ray.viewDirection.y>0?(min.y - ray.eyePoint.y)/ray.viewDirection.y:(max.y - ray.eyePoint.y)/ray.viewDirection.y;
		if (ty>mindist&&ty<result.t) {
			Vector3d vy = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, ty));
			if (vy.y<max.y&&vy.y>min.y&&vy.z<max.z&&vy.z>min.z) {
				result.t = ty;
				result.material = material;
				result.n = ray.viewDirection.y>0?new Vector3d(-1, 0, 0):new Vector3d(1, 0, 0);
				result.p = new Point3d(vy);
			}
		}
		double tx = ray.viewDirection.x>0?(min.x - ray.eyePoint.x)/ray.viewDirection.x:(max.x - ray.eyePoint.x)/ray.viewDirection.x;
		if (tx>mindist&&tx<result.t) {
			Vector3d vx = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, tx));
			if (vx.y<max.y&&vx.y>min.y&&vx.z<max.z&&vx.z>min.z) {
				result.t = tx;
				result.material = material;
				result.n = ray.viewDirection.x>0?new Vector3d(-1, 0, 0):new Vector3d(1, 0, 0);
				result.p = new Point3d(vx);
			}
		}
		double tz = ray.viewDirection.z>0?(min.z - ray.eyePoint.z)/ray.viewDirection.z:(max.z - ray.eyePoint.z)/ray.viewDirection.z;
		if (tz>mindist&&tz<result.t) {
			Vector3d vz = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, tz));
			if (vz.y<max.y&&vz.y>min.y&&vz.z<max.z&&vz.z>min.z) {
				result.t = tz;
				result.material = material;
				result.n = ray.viewDirection.z>0?new Vector3d(-1, 0, 0):new Vector3d(1, 0, 0);
				result.p = new Point3d(vz);
			}
		}





	}	

}
