package comp557.a4;

import javax.vecmath.Color4f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3d;
/**
 * v3f
 */
public class v3d {
    public static Vector3d add(Tuple3d a,Tuple3d b){
        return new Vector3d(a.x+b.x, a.y+b.y, a.z+b.z);
    }
    
    public static Vector3d minus(Tuple3d a,Tuple3d b){
        return new Vector3d(a.x-b.x, a.y-b.y, a.z-b.z);
	}
	public static Vector3d times(Tuple3d a,Tuple3d b){
        return new Vector3d(a.x*b.x, a.y*b.y, a.z*b.z);
	}
	public static Vector3d times(Tuple4f a,Tuple3d b){
        return new Vector3d(a.x*b.x, a.y*b.y, a.z*b.z);
	}
	public static Vector3d times(Tuple4f a,Tuple3f b){
        return new Vector3d(a.x*b.x, a.y*b.y, a.z*b.z);
    }
    public static Vector3d times(Color4f a,double b){
        return new Vector3d((a.x*b), (a.y*b), (a.z*b));
    }
    public static Vector3d times(Tuple3d a,double b){
        return new Vector3d(a.x*b, a.y*b, a.z*b);
	}
	
    public static Vector3d times(Matrix4d m,Tuple3d t) {
    	Vector3d v= new Vector3d(t);
    	m.transform(v);
    	return v;
    }
    public static Vector3d times(Tuple3d t,Matrix4d m) {
    	return times(m, t);
    }
    public static double dot(Vector3d a,Vector3d b){
        return a.dot(b);
    }
    public static double dot(Tuple3d a,Tuple3d b) {
    	return dot(new Vector3d(a), new Vector3d(b));
    }
    public static Vector3d cross(Tuple3d a,Tuple3d b) {
    	Vector3d aa = new Vector3d(a);
    	Vector3d bb = new Vector3d(b);
    	aa.cross(aa, bb);
    	return aa;
    }
    public static Vector3d normalize(Vector3d v) {
    	Vector3d cpy = new Vector3d(v);
    	cpy.normalize();
    	return cpy;
    }
    public static Matrix4d lookat(Tuple3d o,Tuple3d lookat,Tuple3d up) {
    	Vector3d f = normalize(minus(lookat, o));
    	Vector3d s = normalize(cross(f,up));
    	Vector3d u = normalize(cross(s, f));
    	Matrix4d m = new Matrix4d();
    	m.setIdentity();
    	m.m00 = s.x;
    	m.m10 = s.y;
    	m.m20 = s.z;
    	m.m01 = u.x;
    	m.m11 = u.y;
    	m.m21 = u.z;
    	m.m02 = -f.x;
    	m.m12 = -f.y;
		m.m22 = -f.z;
		m.transpose();
    	return m;
    }
    public static Matrix4d inverse(Matrix4d m) {
    	Matrix4d inv = new Matrix4d();
    	inv.invert(m);
    	return inv;
    }
}
