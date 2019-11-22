package comp557.a4;

import java.util.List;
import java.util.Random;

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Light {
	Random rnd = new Random(System.currentTimeMillis());
	/** Light name */
    public String name = "";
    
    /** Light colour, default is white */
    public Color4f color = new Color4f(1,1,1,1);
    
    /** Light position, default is the origin */
    public Point3d from = new Point3d(0,0,0);
    
    /** Light intensity, I, combined with colour is used in shading */
    public double power = 1.0;
    
    /** Type of light, default is a point light */
    public String type = "point";

    public double radius = 0;

    /**
     * Default constructor 
     */
    public Light() {
    	// do nothing
    }
    public Vector3d randomPoint(){
        Vector3d v;
        do {
            v = new Vector3d(rnd.nextDouble()*1.2-0.6,rnd.nextDouble()*1.2-0.6,rnd.nextDouble()*1.2-0.6);
        } while (v.lengthSquared()>=1&&v.lengthSquared()<0.7);
        return v3d.add(v3d.times(v,radius*2), from);
    }
}
