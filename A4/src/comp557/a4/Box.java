package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A simple box class. A box is defined by it's lower (@see min) and upper (@see max) corner. 
 */
public class Box extends Intersectable {
	
	Vector3d min,max;
    /**
     * Default constructor. Creates a 2x2x2 box centered at (0,0,0)
     */
    public Box() {
    	super();
    	this.max = new Vector3d( 1, 1, 1 );
    	this.min = new Vector3d( -1, -1, -1 );
    }	

	public boolean testIntersect(Ray ray){
		double t = (min.x - ray.eyePoint.x)/ray.viewDirection.x;
		t = Math.abs(t);
		Vector3d v = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t));
			if (v.y<=max.y&&v.y>=min.y&&v.z<=max.z&&v.z>=min.z) {
				return true;
			}

		t = (max.x - ray.eyePoint.x)/ray.viewDirection.x;
		t = Math.abs(t);
		v = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t));
		if (v.y<=max.y&&v.y>=min.y&&v.z<=max.z&&v.z>=min.z) {
			return true;
		}
		

		t = (min.y - ray.eyePoint.y)/ray.viewDirection.y;
		t = Math.abs(t);
		v = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t));
		if (v.x<=max.x&&v.x>=min.x&&v.z<=max.z&&v.z>=min.z) {
			return true;
		}
		

		t = (max.y - ray.eyePoint.y)/ray.viewDirection.y;
		t = Math.abs(t);
		v = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t));
			if (v.x<=max.x&&v.x>=min.x&&v.z<=max.z&&v.z>=min.z) {
				return true;
			}
		
		
		t = (min.z - ray.eyePoint.z)/ray.viewDirection.z;
		t = Math.abs(t);
		v = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t));
			if (v.y<=max.y&&v.y>=min.y&&v.x<=max.x&&v.x>=min.x) {
				return true;
			}
		

		t = (max.z - ray.eyePoint.z)/ray.viewDirection.z;
		t = Math.abs(t);
		v = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t));
			if (v.y<=max.y&&v.y>=min.y&&v.x<=max.x&&v.x>=min.x) {
				return true;
			}
		return false;
	}
	@Override
	public void intersect(Ray ray, IntersectResult result) {
		// TODO: Objective 6: intersection of Ray with axis aligned box

		//assume that ray cannot hit a surface with normal same direction than ray
		double tx = (min.x - ray.eyePoint.x)/ray.viewDirection.x;
		if (tx>Epsilon&&tx<result.t) {
			Vector3d vx = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, tx));
			if (vx.y<=max.y&&vx.y>=min.y&&vx.z<=max.z&&vx.z>=min.z) {
				result.t = tx;
				result.material = material;
				result.n = new Vector3d(-1, 0, 0);
				result.p = new Point3d(vx);
			}
		}

		tx = (max.x - ray.eyePoint.x)/ray.viewDirection.x;
		if (tx>Epsilon&&tx<result.t) {
			Vector3d vx = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, tx));
			if (vx.y<=max.y&&vx.y>=min.y&&vx.z<=max.z&&vx.z>=min.z) {
				result.t = tx;
				result.material = material;
				result.n = new Vector3d(1, 0, 0);
				result.p = new Point3d(vx);
			}
		}

		double ty = (min.y - ray.eyePoint.y)/ray.viewDirection.y;
		if (ty>Epsilon&&ty<result.t) {
			Vector3d vy = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, ty));
			if (vy.x<=max.x&&vy.x>=min.x&&vy.z<=max.z&&vy.z>=min.z) {
				result.t = ty;
				result.material = material;
				result.n = new Vector3d(0, -1, 0);
				result.p = new Point3d(vy);
			}
		}

		ty = (max.y - ray.eyePoint.y)/ray.viewDirection.y;
		if (ty>Epsilon&&ty<result.t) {
			Vector3d vy = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, ty));
			if (vy.x<=max.x&&vy.x>=min.x&&vy.z<=max.z&&vy.z>=min.z) {
				result.t = ty;
				result.material = material;
				result.n = new Vector3d(0, 1, 0);
				result.p = new Point3d(vy);
			}
		}
		
		double tz = (min.z - ray.eyePoint.z)/ray.viewDirection.z;
		if (tz>Epsilon&&tz<result.t) {
			Vector3d vz = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, tz));
			if (vz.y<=max.y&&vz.y>=min.y&&vz.x<=max.x&&vz.x>=min.x) {
				result.t = tz;
				result.material = material;
				result.n = new Vector3d(0, 0, -1);
				result.p = new Point3d(vz);
			}
		}

		tz = (max.z - ray.eyePoint.z)/ray.viewDirection.z;
		if (tz>Epsilon&&tz<result.t) {
			Vector3d vz = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, tz));
			if (vz.y<=max.y&&vz.y>=min.y&&vz.x<=max.x&&vz.x>=min.x) {
				result.t = tz;
				result.material = material;
				result.n = new Vector3d(0, 0, 1);
				result.p = new Point3d(vz);
			}
		}
	}

}
