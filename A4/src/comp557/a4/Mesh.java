package comp557.a4;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

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
			Point3d p0 = soup.vertexList.get(face[0]).p;
			Point3d p1 = soup.vertexList.get(face[1]).p;
			Point3d p2 = soup.vertexList.get(face[2]).p;

			Vector3d v0v1 = v3d.minus(p1, p0);
			Vector3d v0v2 = v3d.minus(p2, p0);

			Vector3d pvec = v3d.cross(ray.viewDirection,v0v2);
			double det = v3d.dot(v0v1, pvec);

			if(Math.abs(det)<Epsilon) continue;

			double invdet = 1/det;
			Vector3d tvec = v3d.minus(ray.eyePoint,p0);
			double u = v3d.dot(tvec, pvec)*invdet;
			if(u<0||u>1) continue;
			Vector3d qvec = v3d.cross(tvec, v0v1);
			double v = v3d.dot((ray.viewDirection), qvec)*invdet;
			if(v<0||u+v>1) continue;
			double t = v3d.dot(v0v2, qvec)*invdet;
			
			if (t>Epsilon&&t<result.t) {
				result.t = t;
				result.p = new Point3d(v3d.add(ray.eyePoint, v3d.times(ray.viewDirection, t)));
				result.material = material;
				result.n = v3d.normalize(v3d.cross(v0v1, v0v2));
			}
		}
	}

}
