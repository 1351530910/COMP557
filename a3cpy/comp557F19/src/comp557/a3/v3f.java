package comp557.a3;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
/**
 * v3f
 */
public class v3f {
    public static Vector3d add(Tuple3d a,Tuple3d b){
        return new Vector3d(a.x+b.x, a.y+b.y, a.z+b.z);
    }
    public static Vector3d minus(Vertex a,Vertex b){
        return minus(a.p, b.p);
    }
    public static Vector3d minus(Tuple3d a,Tuple3d b){
        return new Vector3d(a.x-b.x, a.y-b.y, a.z-b.z);
    }
    public static Vector3d times(Tuple3d a,float b){
        return new Vector3d(a.x*b, a.y*b, a.z*b);
    }
    public static double dot(Vector3d a,Vector3d b){
        return a.dot(b);
    }
}