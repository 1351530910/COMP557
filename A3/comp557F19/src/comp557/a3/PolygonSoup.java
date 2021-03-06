package comp557.a3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * Simple implementation of a loader for a polygon soup
 */
public class PolygonSoup {

    /** List of vertex objects used in the mesh. */
    public ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
    
    /** List of faces, where each face is a list of integer indices into the vertex list. */
    public ArrayList<int[]> faceList = new ArrayList<int[]>();
    
    /** Map for keeping track of how many n-gons we have for each n */
    private TreeMap<Integer,Integer> faceSidesHistogram = new TreeMap<Integer,Integer>();
    
    /** A string summarizing the face sides histogram */
    public String soupStatistics;


    double width,height,depth;
    boolean boundingBox = true;


    
    /**
     * Creates a polygon soup by loading an OBJ file
     * @param file
     */
    public PolygonSoup(String file) {
        try {
            FileInputStream fis = new FileInputStream( file );
            InputStreamReader isr = new InputStreamReader( fis );
            BufferedReader reader = new BufferedReader( isr );
            String line;
            while ((line = reader.readLine()) != null) {
                if ( line.startsWith("v ") ) {
                    parseVertex(line);
                } else if ( line.startsWith("f ") ) {
                    parseFace(line);
                } 
            }
            reader.close();
            isr.close();
            fis.close();
                        
            soupStatistics = file + "\n" + "faces = " +faceList.size() + "\nverts = " + vertexList.size() + "\n";
            for ( Map.Entry<Integer,Integer> e : faceSidesHistogram.entrySet() ) {
                soupStatistics += e.getValue() + " ";
                if ( e.getKey() == 3 ) {
                    soupStatistics += "triangles\n";
                } else if ( e.getKey() == 4 ) {
                    soupStatistics += "quadrilaterals\n";
                } else {
                    soupStatistics += e.getKey() + "-gons\n";
                }
            }
            System.out.println( soupStatistics );
            
            // TODO: 1 compute a bounding box and scale and center the geometry
            double top = 0,
            bottom = 0,
            near = 0,
            far = 0,
            left = 0,
            right = 0;

            for (Vertex v : vertexList) {
                if (v.p.y>top) 
                    top = v.p.y;
                if(v.p.y<bottom)
                    bottom = v.p.y;
                if(v.p.x>right)
                    right = v.p.x;
                if(v.p.x<left)
                    left = v.p.x;
                if(v.p.z>near)
                    near = v.p.z;
                if(v.p.z<far)
                    far = v.p.z;
            }
            width = right-left;
            height = top-bottom;
            depth = near-far;

            double largestDimension = width>height ? width:height;
            largestDimension = largestDimension > depth ? largestDimension:depth;
            double scale = 1/largestDimension*10.0;


            Vector3d displacement = new Vector3d(0.5*(right+left), 0.5*(top+bottom), 0.5*(near+far));

            for (int i = 0; i < vertexList.size(); i++) {
                vertexList.get(i).p.sub(displacement);
                vertexList.get(i).p.scale(scale);
            }

            width*=scale*0.5;
            height*=scale*0.5;
            depth*=scale*0.5;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Parses a vertex definition from a line in an obj file, and 
     * directly inserts it into the vertex list.
     * Assumes that there are three components.
     * @param newline
     * @return a new vertex object
     */
    private Vertex parseVertex(String newline) {        
        // Remove the tag "v "
        newline = newline.substring(2, newline.length());
        StringTokenizer st = new StringTokenizer(newline, " ");
        Vertex v = new Vertex();
        v.p.x = Double.parseDouble(st.nextToken());
        v.p.y = Double.parseDouble(st.nextToken());
        v.p.z = Double.parseDouble(st.nextToken());
        v.index = vertexList.size();
        vertexList.add( v );
        return v;
    }

    /**
     * Gets the list of indices for a face from a string in an obj file.
     * Simply ignores texture and normal information for simplicity
     * @param newline
     * @return list of indices
     */
    private int[] parseFace(String newline) {
        // Remove the tag "f "
        newline = newline.substring(2, newline.length());
        // vertex/texture/normal tuples are separated by spaces.
        StringTokenizer st = new StringTokenizer(newline, " ");
        int count = st.countTokens();
        int v[] = new int[count];
        for (int i = 0; i < count; i++) {
            // first token is vertex index... we'll ignore the rest (if it exists)
            StringTokenizer st2 = new StringTokenizer(st.nextToken(),"/");
            v[i] = Integer.parseInt(st2.nextToken()) - 1; // want zero indexed vertices!            
        }
        Integer n = faceSidesHistogram.get( count );
        if ( n == null ) {
            faceSidesHistogram.put( count, 1 );
        } else {
            faceSidesHistogram.put( count, n + 1 );
        }
        faceList.add( v );
        return v;
    }    

    /**
     * Draw the polygon soup using legacy immediate mode OpenGL
     * @param drawable
     */
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        if (boundingBox) {
            gl.glBegin(GL.GL_LINES);
            
            gl.glVertex3d(width,height,depth);
            gl.glVertex3d(-width,height,depth);
            gl.glVertex3d(width,-height,depth);
            gl.glVertex3d(-width,-height,depth);
            gl.glVertex3d(width,height,-depth);
            gl.glVertex3d(-width,height,-depth);
            gl.glVertex3d(width,-height,-depth);
            gl.glVertex3d(-width,-height,-depth);

            gl.glVertex3d(width,height,depth);
            gl.glVertex3d(width,-height,depth);
            gl.glVertex3d(-width,height,depth);
            gl.glVertex3d(-width,-height,depth);
            gl.glVertex3d(-width,height,-depth);
            gl.glVertex3d(-width,-height,-depth);
            gl.glVertex3d(width,height,-depth);
            gl.glVertex3d(width,-height,-depth);

            gl.glVertex3d(width,height,depth);
            gl.glVertex3d(width,height,-depth);
            gl.glVertex3d(width,-height,depth);
            gl.glVertex3d(width,-height,-depth);
            gl.glVertex3d(-width,height,depth);
            gl.glVertex3d(-width,height,-depth);
            gl.glVertex3d(-width,-height,depth);
            gl.glVertex3d(-width,-height,-depth);
            gl.glEnd();
        }
        
        // assume triangular faces!
        Vector3d v1 = new Vector3d();
        Vector3d v2 = new Vector3d();
        Vector3d n = new Vector3d();
        for ( int[] faceVertex : faceList ) {
            Point3d p0 = vertexList.get( faceVertex[0] ).p;
            Point3d p1 = vertexList.get( faceVertex[1] ).p;
            Point3d p2 = vertexList.get( faceVertex[2] ).p;
            v1.sub( p1,p0 );
            v2.sub( p2,p1 );
            n.cross( v1, v2 );
            gl.glBegin( GL2.GL_POLYGON );
            gl.glNormal3d( n.x, n.y, n.z );
            for ( int i = 0; i < faceVertex.length; i++ ) {
                Point3d p = vertexList.get( faceVertex[i] ).p;
                gl.glVertex3d( p.x, p.y, p.z );
            }
            gl.glEnd();
        }        
    }
}