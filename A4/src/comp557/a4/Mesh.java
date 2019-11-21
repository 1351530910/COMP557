package comp557.a4;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Mesh extends Intersectable {
	
	/** Static map storing all meshes by name */
	public static Map<String,Mesh> meshMap = new HashMap<String,Mesh>();
	
	/**  Name for this mesh, to allow re-use of a polygon soup across Mesh objects */
	public String name = "";
	
	/**
	 * The polygon soup.
	 */
	public PolygonSoup soup;

	public Mesh() {
		super();
		this.soup=null;
	}			
		
	@Override
	public void intersect(Ray ray, IntersectResult result) {
		
		// TODO: Objective 7: ray triangle intersection for meshes
		//assume there are only triangles
		//using barycentric coordinate

		for (int[] face : soup.faceList) {

			//3 points of triangle
			Point3d p1 = soup.vertexList.get(face[0]).p;
			Point3d p2 = soup.vertexList.get(face[1]).p;
			Point3d p3 = soup.vertexList.get(face[2]).p;

			//vectors and normal, following right hand rule
			Vector3d v1 = v3d.minus(p2, p1);
			Vector3d v2 = v3d.minus(p3, p2);
			Vector3d v3 = v3d.minus(p1, p3);
			Vector3d n = v3d.cross(v1, v3d.minus(p3, p1));

			//check if facing other side
			double cosTheta = v3d.dot(n, ray.viewDirection);
			if (cosTheta>-Epsilon) continue;

			//if t not valid then discard
			double d = v3d.dot(n, p1);
			double t = (v3d.dot(n, ray.eyePoint) + d)/cosTheta;
			if (t<Epsilon||t>=result.t) continue;

			//compute the point on same plane to triangle
			Vector3d p = v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t));

			//check if p inside triangle
			
			if(v3d.dot(n, v3d.cross(v1, v3d.minus(p, p1)))<0) continue;
			if(v3d.dot(n, v3d.cross(v2, v3d.minus(p, p2)))<0) continue;
			if(v3d.dot(n, v3d.cross(v3, v3d.minus(p, p3)))<0) continue;

			result.t = t;
			result.n = v3d.normalize(n);
			result.material = material;
			result.p = new Point3d(p);
		}
	}

}
