package comp557.a4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Simple scene loader based on XML file format.
 */
public class Scene {
    
    /** List of surfaces in the scene */
    public List<Intersectable> surfaceList = new ArrayList<Intersectable>();
	
	/** All scene lights */
	public Map<String,Light> lights = new HashMap<String,Light>();
	
	public static final double maxT = 999999999;

    /** Contains information about how to render the scene */
    public Render render;
    
    /** The ambient light colour */
    public Color3f ambient = new Color3f();
    
    public static Matrix4d lookat = null;
    /** 
     * Default constructor.
     */
    public Scene() {
    	this.render = new Render();
    }
    public static int getcolor(Color3f clr,double intensity) {
    	Color3f c = new Color3f(clr);
    	int r = (int)(intensity*c.x);
        int g = (int)(intensity*c.y);
        int b = (int)(intensity*c.z);
        int a = 255;
        int argb = (a<<24 | r<<16 | g<<8 | b); 
        return argb;
    }
    public static int getcolor(Vector3d c,double intensity) {
    	int r = (int)(intensity*c.x);
        int g = (int)(intensity*c.y);
        int b = (int)(intensity*c.z);
        int a = 255;
        int argb = (a<<24 | r<<16 | g<<8 | b); 
        return argb;
    }
    
    /**
     * renders the scene
     */
    public void render(boolean showPanel) {
 
        Camera cam = render.camera; 
        int w = cam.imageSize.width;
        int h = cam.imageSize.height;
        
        render.init(w, h, showPanel);
        
        lookat = null;
        
        for ( int j = 0; j < h && !render.isDone(); j++ ) {
            for ( int i = 0; i < w && !render.isDone(); i++ ) {
				if (i==170&&j==300) {
					int pfda = 124123;
				}
                // TODO: Objective 1: generate a ray (use the generateRay method)
            	Ray ray = new Ray();
            	double[] offset = new double[2];
            	offset[0] = 0;
            	offset[1] = 0;
            	generateRay(i, j, offset, cam, ray);
                // TODO: Objective 2: test for intersection with scene surfaces
            	IntersectResult info = new IntersectResult();
            	for (Intersectable surface : surfaceList) {
					surface.intersect(ray, info);
				}
            	
                // TODO: Objective 3: compute the shaded result for the intersection point (perhaps requiring shadow rays)
                if (info.t==Double.POSITIVE_INFINITY) {
                	//no collision then black
                	render.setPixel(i, j, getcolor(render.bgcolor,255));
				}else {
					Vector3d color = new Vector3d();
					
					for (Light light : lights.values()) {
						Vector3d wi = v3d.normalize(v3d.minus(light.from, info.p));
						Vector3d wo = v3d.normalize(v3d.minus(cam.from, info.p));
						
						Vector3d n = v3d.normalize(info.n);
						Vector3d bisector = v3d.normalize(v3d.add(v3d.normalize(wi), v3d.normalize(wo)));

						IntersectResult r = new IntersectResult();
						//if in shadow then ignore that light's contribution
						//if (inShadow(null, light, surfaceList, r, new Ray(info.p,v3d.normalize(v3d.minus(light.from, info.p))))) continue;

						//assume the I term in the light formula in obtained by lightcolor*lightpower
						//specular using blinn phong
						
						
						color = v3d.add(color,v3d.times(light.color,v3d.times(info.material.specular, light.power*Math.pow(Math.max(0, v3d.dot(n, bisector)),info.material.shinyness))));
						//diffuse
						color = v3d.add(color,v3d.times(light.color,v3d.times(info.material.diffuse, Math.max(0, v3d.dot(wi, info.n))*light.power)));
					}
					color = v3d.add(color, v3d.times(info.material.diffuse, ambient));
					color.x = Math.min(1, color.x);
					color.y = Math.min(1, color.y);
					color.z = Math.min(1, color.z);
					render.setPixel(i, j, getcolor(color, 255));
				}
            	
            }
        }
        
        // save the final render image
        render.save();
        
        // wait for render viewer to close
        render.waitDone();
        
    }
    
    /**
     * Generate a ray through pixel (i,j).
     * 
     * @param i The pixel row.
     * @param j The pixel column.
     * @param offset The offset from the center of the pixel, in the range [-0.5,+0.5] for each coordinate. 
     * @param cam The camera.
     * @param ray Contains the generated ray.
     */
	public static void generateRay(final int i, final int j, final double[] offset, final Camera cam, Ray ray) {
		
		// TODO: Objective 1: generate rays given the provided parmeters
		if (lookat==null) {
			//generate the lookat matrix if there is none
			lookat = v3d.inverse(v3d.lookat(cam.from, cam.to, cam.up));
		}

		ray.eyePoint = cam.from;

		double width = cam.imageSize.getWidth();
		double height = cam.imageSize.getHeight();
		
		
		double d = 1/Math.tan(Math.toRadians(cam.fovy/2));	//distance to film
		double variation = 2.0/cam.imageSize.getHeight();	//size of pixel, since pixel is square, horizontal and vertical uses the same
		
		//coordinate of pixel
		double px = -(i/height-0.5*width/height)*2;	//it seems that the image is reversed left-right
		double py = (2.0*j/height-1.0);
		
		//offset the pixel
		double dirx = offset[0] * (variation+0.5) + px;
		double diry = offset[1] * (variation+0.5) + py;
		
		//convert to world space
		ray.viewDirection = v3d.times(v3d.normalize(v3d.times(lookat, new Vector3d(dirx, diry, d))),-1);
	}

	/**
	 * Shoot a shadow ray in the scene and get the result.
	 * 
	 * @param result Intersection result from raytracing. 
	 * @param light The light to check for visibility.
	 * @param root The scene node.
	 * @param shadowResult Contains the result of a shadow ray test.
	 * @param shadowRay Contains the shadow ray used to test for visibility.
	 * 
	 * @return True if a point is in shadow, false otherwise. 
	 */
	public static boolean inShadow(final IntersectResult result, final Light light, final List<Intersectable> root, IntersectResult shadowResult, Ray shadowRay) {
		
		// TODO: Objective 5: check for shdows and use it in your lighting computation
		for (Intersectable obj : root) {
			obj.intersect(shadowRay, shadowResult);
			if (shadowResult.t<Double.POSITIVE_INFINITY) {
				return true;
			}
		}
		return false;
	}    
}
