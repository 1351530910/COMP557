package comp557.a3;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;
import java.util.TreeMap;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * Half edge data structure.
 * Maintains a list of faces (i.e., one half edge of each) to allow
 * for easy display of geometry.
 */
public class HEDS {
	
    /** List of faces */
    ArrayList<Face> faces = new ArrayList<Face>();
    
    /** List of vertices */
    public static ArrayList<Vertex> vertices;

    /** Convenience member for keeping track of half edges you make or need */
    Map<String,HalfEdge> halfEdges = new TreeMap<String,HalfEdge>();
    public static int facePerVertex[];
    /**
     * Builds a half edge data structure from the polygon soup   
     * @param soup
     */
    public HEDS( PolygonSoup soup ) {
        vertices = soup.vertexList;
        for ( int[] f : soup.faceList ) {        
        	// TODO: 2 Build the half edge data structure from the polygon soup, triangulating non-triangular faces

            //triangulation
            int[][] faceList = new int[f.length-2][3];
            for (int i = 0; i < faceList.length; i++) {
                faceList[i][0] = f[0];
                faceList[i][1] = f[i+1];
                faceList[i][2] = f[i+2];
            }

            for (int[] face : faceList) {
                int i = face[face.length - 1];
                int j = face[0];
                HalfEdge he = new HalfEdge();
                halfEdges.put(i+","+j, he);
                HalfEdge firstEdge = he;
                he.head = soup.vertexList.get(j);
                he.twin = halfEdges.get(j+","+i);
                if (he.twin != null) {
                    he.twin.twin = he;
                }
                for (int k = 1; k < face.length; k++) {
                    i = j;
                    j = face[k];
                    HalfEdge next = new HalfEdge();
                    halfEdges.put(i+","+j, next);
                    next.head = soup.vertexList.get(j);
                    next.twin = halfEdges.get(j+","+i);
                    if (next.twin != null) {
                        next.twin.twin = next;
                    }
                    he.next = next;
                    he = next;
                }
                he.next = firstEdge;
                faces.add(new Face(firstEdge));
            }
        }
        
        // TODO: 3 Compute vertex normals
        facePerVertex = new int[vertices.size()];
        for (Vertex v : vertices){
            v.n = new Vector3d(0,0,0);
        }

        for (HalfEdge he : halfEdges.values()) {
            he.head.n.add(he.leftFace.n);
            facePerVertex[vertices.indexOf(he.head)]++;
            he.head.he = he;
        }

        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i).n.scale(1.0/facePerVertex[i]);
        }
        
    }
    
    /**
     * Helper function for creating a half edge, and pairing it up with its twin if the
     * twin half edge has already been created.
     * @param soup 
     * @param i tail vertex index
     * @param j head vertex index
     * @return the half edge, paired with its twin if the twin was created.
     * @throws Exception
     */
    private HalfEdge createHalfEdge( PolygonSoup soup, int i, int j ) throws Exception {
        String p = i+","+j;
        if ( halfEdges.containsKey( p ) ){
            throw new Exception("non orientable manifold");
        }
        String twin = j+","+i;
        HalfEdge he = new HalfEdge();
        he.head = soup.vertexList.get(j);
        he.head.he = he; // make sure the vertex has at least one half edge that points to it.
        he.twin = halfEdges.get( twin );
        if ( he.twin != null ) he.twin.twin = he;
        halfEdges.put( p, he );        
        return he;        
    }    
    
    /** 
     * Reset the solutions for heat and distance
     */
    public void resetHeatAndDistanceSolution() {
    	for ( Vertex v : vertices ) {
    		v.u0 = v.constrained? 1 : 0;
    		v.ut = v.u0;
    		v.phi = 0;
    	}
    }
    
    /** 
     * Perform a specified number of projected Gauss-Seidel steps of the heat diffusion equation.
     * The current ut values stored in the vertices will be refined by this call.
     * @param GSSteps number of steps to take
     * @param t solution time
     */
    public void solveHeatFlowStep( int GSSteps, double t ) {    	
    	// Solve (A - t LC) u_t = u_0 with constrained vertices holding their ut value fixed
    	// Note that this is a backward Euler step of the heat diffusion.
    	for ( int i = 0; i < GSSteps; i++ ) {
    		for ( Vertex v : vertices ) {
    			if ( v.constrained ) continue;  // do nothing for the constrained vertex!
    			
    			// TODO: 7 write inner loop code for the PGS heat solve
    			v.ut = 0;
                int n = v.valence();
                HalfEdge start = v.he;
                for (int j = 0; j < n; j++) {
                    v.ut += v.he.twin.head.ut * v.he.twin.head.LCii;
                    start = start.next.next.twin;
                }
    			v.ut = (v.u0+t*v.ut)/(v.area-t*v.LCii);
    			
    			
    		}	
    	}
    }
    
    /**
     * Compute the gradient of heat at each face
     */
    public void updateGradu() {
    	// TODO: 8 update the gradient of u from the heat values, i.e., f.gradu for each Face f
    	
    	
    	
    	
    }
    
    /** 
     * Compute the divergence of normalized gradients at the vertices
     */
    public void updateDivx() {
    	// TODO: 9 Update the divergence of the normalized gradients, ie., v.divX for each Vertex v
    	
    	
    	
    	
    }
    
    /** Keep track of the maximum distance for debugging and colour map selection */
    double maxphi = 0 ;

    /**
     * Solves the distances
     * Uses Poisson equation, Laplacian of distance equal to divergence of normalized heat gradients.
     * This is step III in Algorithm 1 of the Geodesics in Heat paper, but here is done iteratively 
     * with a Gauss-Seidel solve of some number of steps to refine the solution whenever this method 
     * is called.
     * @param GSSteps number of Gauss-Seidel steps to take
     */
    public void solveDistanceStep( int GSSteps ) {		
    	for ( int i = 0; i < GSSteps; i++ ) {
    		for ( Vertex v : vertices ) {
    			// TODO: 10 Implement the inner loop of the Gauss-Seidel solve to compute the distances to each vertex, phi
    			
    			
    			
    			
    		}    		
    	}
    	
    	// Note that the solution to step III is unique only up to an additive constant,
    	// final values simply need to be shifted such that the smallest distance is zero. 
    	// We also identify the max phi value here to identify the maximum geodesic and to 
    	// use adjusting the colour map for rendering
    	double minphi = Double.MAX_VALUE;
    	maxphi = Double.MIN_VALUE;
		for ( Vertex v : vertices ) {
			if ( v.phi < minphi ) minphi = v.phi;
			if ( v.phi > maxphi ) maxphi = v.phi;
		}	
		maxphi -= minphi;
		for ( Vertex v : vertices ) {
			v.phi -= minphi;
		}
    }
    
   
    /**
     * Computes the cotangent Laplacian weights at each vertex.
	 * You can assume no boundaries and a triangular mesh! 
	 * You should store these weights in an array at each vertex,
	 * and likewise store the associated "vertex area", i.e., 1/3 of
	 * the surrounding triangles and NOT scale your Laplacian weights
	 * by the vertex area (see heat solve objective requirements).
     */
    public void computeLaplacian() {
        //compute area of each vertex
        
        
        double oneThird = 1.0/3.0;
    	for ( Vertex v : vertices ) {
    		// TODO: 6 Compute the Laplacian and store as vertex weights, and cotan operator diagonal LCii and off diagonal LCij terms.
    		v.area *= oneThird;
    		v.LCii = 0;
    		v.LCij = new double[ v.valence() ];

            HalfEdge he;
            double alpha,beta;
            he = v.he;
            for (int index = 0; index < v.LCij.length; index++) {
                alpha = angleWithNext(he.twin.next);
                beta = angleWithNext(he.next);
                
                v.LCij[index] = (1.0/Math.tan(alpha)+1.0/Math.tan(beta))/2;
                v.LCii += 1.0/Math.tan(alpha)+1.0/Math.tan(beta);
                he = he.twin.next;
            }
            
            v.LCii = v.LCii/2;
    	}
    }
    
    /** 
     * Computes the angle between the provided half edge and the next half edge
     * @param he specify which half edge
     * @return the angle in radians
     */
    private double angleWithNext( HalfEdge he ) {
        Point3d a = he.head.p,b = he.next.head.p,c = he.next.next.head.p;
        Vector3d ac = v3f.minus(c, a), bc = v3f.minus(c, b);
        return Math.acos(v3f.dot(ac,bc)/ac.length()/bc.length());
    }
    
    /**
     * Legacy drawing code for the half edge data structure by drawing each of its faces.
     * Legacy in that this code uses immediate mode OpenGL.  Per vertex normals are used
     * to draw the smooth surface if they are set in the vertices. 
     * @param drawable
     */
    public void display( GLAutoDrawable drawable ) {
        GL2 gl = drawable.getGL().getGL2();
        for ( Face face : faces ) {
            HalfEdge he = face.he;
            if ( he.head.n == null ) { // don't have per vertex normals? use the face
                gl.glBegin( GL2.GL_POLYGON );
                Vector3d n = he.leftFace.n;
                gl.glNormal3d( n.x, n.y, n.z );
                HalfEdge e = he;
                do {
                	Point3d p = e.head.p;
                    gl.glVertex3d( p.x, p.y, p.z );
                    e = e.next;
                } while ( e != he );
                gl.glEnd();
            } else {
                gl.glBegin( GL2.GL_POLYGON );                
                HalfEdge e = he;
                do {
                	Point3d p = e.head.p;
                    Vector3d n = e.head.n;
                    gl.glNormal3d( n.x, n.y, n.z );
                    gl.glVertex3d( p.x, p.y, p.z );
                    e = e.next;
                } while ( e != he );
                gl.glEnd();
            }
        }
    }
    
}
