package comp557.a1;
 		  	  				   
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

import javax.vecmath.Vector3d;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Loads an articulated character hierarchy from an XML file. 
 */
public class CharacterFromXML {

	public static GraphNode load( String filename ) {
		try {
			InputStream inputStream = new FileInputStream(new File(filename));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputStream);
			return createScene( null, document.getDocumentElement() ); // we don't check the name of the document elemnet
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load simulation input file.", e);
		}
	}
	
	/**
	 * Load a subtree from a XML node. Returns the root on the call where the parent
	 * is null, but otherwise all children are added as they are created and all
	 * other deeper recursive calls will return null.
	 * 
	 * @throws Exception
	 */
	public static GraphNode createScene(GraphNode parent, Node dataNode) throws Exception {
		NodeList nodeList = dataNode.getChildNodes();
		
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            Node n = nodeList.item(i);
            // skip all text, just process the ELEMENT_NODEs
            if ( n.getNodeType() != Node.ELEMENT_NODE ) continue;
            String nodeName = n.getNodeName();
            GraphNode node = null;
            if ( nodeName.equalsIgnoreCase( "node" ) ) {
            	node = CharacterFromXML.createJoint( n );
            } else if ( nodeName.equalsIgnoreCase( "geom" ) ) {        		
        		node = CharacterFromXML.createGeom( n ) ;            
            }
            // recurse to load any children of this node
            createScene( node, n );
            if ( parent == null ) {
            	// if no parent, we can only have one root... ignore other nodes at root level
            	return node;
            } else {
            	parent.add( node );
            }
        }
        return null;
	}
	
	/**
	 * ​‌​​​‌‌​​​‌‌​​​‌​​‌‌‌​​‌ Create a joint
	 * 
	 * TODO: Objective 5: Adapt commented code in createJoint() to create your joint
	 * nodes when loading from xml
	 * 
	 * @throws Exception
	 */
	public static GraphNode createJoint(Node dataNode) throws Exception {
		String type = dataNode.getAttributes().getNamedItem("type").getNodeValue();
		String name = dataNode.getAttributes().getNamedItem("name").getNodeValue();
		
		switch (type) {
			case "free" : 
				return new FreeJoint( name );
			case "spherical": 
				return new SphericalJoint( name ,getVector3dAttr(dataNode,"position"));
			case "rotary": 
				return new RotaryJoint(
					name, 
					getVector3dAttr(dataNode, "axis"), 
					getVector3dAttr(dataNode, "position")
					);
			default:
				throw new Exception("unsupported Graphnode Type");
		}
	}

	/**
	 * Creates a geometry DAG node 
	 * 
	 * TODO: Objective 5: Adapt commented code in greatGeom to create your geometry nodes when loading from xml
	 */
	public static GraphNode createGeom( Node dataNode ) {
		String type = dataNode.getAttributes().getNamedItem("type").getNodeValue();
		String name = dataNode.getAttributes().getNamedItem("name").getNodeValue();
		switch (type) {
			case "box":
				return new Cube(
					name, 	//name
					getVector3dAttr(dataNode,"center"), //center
					getVector3dAttr(dataNode, "orientation"),
					getVector3dAttr(dataNode,"scale"), //scaling
					getVector3dAttr(dataNode,"color")
					);
			case "sphere":
				return new Sphere(
					name, 	//name
					getVector3dAttr(dataNode,"center"), //center
					getVector3dAttr(dataNode, "orientation"),
					getVector3dAttr(dataNode,"scale"), //scaling
					getVector3dAttr(dataNode,"color")
					);		
			case "teapot":
					return new Teapot(
						name, 	//name
						getVector3dAttr(dataNode,"center"), //center
						getVector3dAttr(dataNode, "orientation"),
						getVector3dAttr(dataNode,"scale"), //scaling
						getVector3dAttr(dataNode,"color")
						);		
				
			default:
				break;
		}
		return null;		
	}
	
	/**
	 * Loads tuple3d attributes of the given name from the given node.
	 * @param dataNode
	 * @param attrName
	 * @return null if attribute not present
	 */
	public static Vector3d getVector3dAttr( Node dataNode, String attrName ) {
		Node attr = dataNode.getAttributes().getNamedItem( attrName);
		Vector3d tuple = null;
		if ( attr != null ) {
			Scanner s = new Scanner( attr.getNodeValue() );
			tuple = new Vector3d( s.nextDouble(), s.nextDouble(), s.nextDouble() );			
			s.close();
		}
		return tuple;
	}

}